package com.jpmc.processor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.jpmc.processor.model.NotificationMessage;
import com.jpmc.processor.model.Product;
import com.jpmc.processor.model.SaleLog;
import com.jpmc.processor.service.CallBackService;

public class CallBackServiceImpl implements CallBackService {

	private static Logger log = Logger.getLogger(CallBackServiceImpl.class);

	SaleLog saleLog = new SaleLog();

	List<Product> saleLogList = new ArrayList<Product>();

	Integer REPORT_DETAIL_COUNTER = 0;
	Integer PAUSE_COUNTER = 0;

	public void publishMessage(NotificationMessage notification) {

		parseMessage(notification);

	}

	public void loadSaleReports() {

		for (Product product : saleLogList) {
			log.info("Sale for " + product.getName() + " count "
					+ product.getSaleCount() + " price " + product.getPrice());
		}

	}

	/**
	 * Message Type 1 – contains the details of 1 sale E.g apple at 10p
	 * 
	 * Message Type 2 – contains the details of a sale and the number of
	 * occurrences of that sale. E.g 20 sales of apples at 10p each.
	 * 
	 * Message Type 3 – contains the details of a sale and an adjustment
	 * operation to be applied to all stored sales of this product type.
	 * Operations can be add, subtract, or multiply e.g Add 20p apples would
	 * instruct your application to add 20p to each sale of apples you have
	 * recorded.
	 * 
	 * @param notification
	 * @return
	 */
	private void parseMessage(NotificationMessage notification) {
		parse(notification.getMessage(), saleLogList);

	}

	private void parse(String message, List<Product> products) {

		String[] productArray = message.split("at");

		if (productArray.length == 1) {
			String[] adjustProducts = productArray[0].split("to");
			saleLog.createAdjustment(message);
			parseMessage3(adjustProducts, products);
		} else if (productArray.length == 2) {
			if (Character.isDigit(productArray[0].charAt(0))) {
				parseMessage2(productArray, products);
			} else {
				parseMessage1(productArray, products);
			}
		} else {
			log.error("Invalid Message");
		}

	}

	private void parseMessage1(String[] itemArray, List<Product> products) {
		Product currentProduct = new Product();

		currentProduct.setName(itemArray[0].trim());
		currentProduct.setPrice(Double.parseDouble(itemArray[1].replaceAll("p",
				"")));
		currentProduct.setSaleCount(1.0);

		products.add(currentProduct);

	}

	private void parseMessage2(String[] productArray, List<Product> products) {
		Product currentProduct = new Product();

		String[] temp = productArray[0].split(" ");

		currentProduct.setSaleCount(Double.parseDouble(temp[0]));
		currentProduct.setPrice(Double.parseDouble(productArray[1].replaceAll(
				"p", "")));
		currentProduct.setName(temp[1]);

		ListIterator<Product> iterator = products.listIterator();
		while (iterator.hasNext()) {
			Product product = iterator.next();

			if (currentProduct.getName().trim()
					.startsWith(product.getName().trim())) {

				currentProduct.setPrice((product.getPrice() * product
						.getSaleCount())
						+ (currentProduct.getPrice() * currentProduct
								.getSaleCount()));
				currentProduct.setSaleCount(currentProduct.getSaleCount()
						+ product.getSaleCount());

				iterator.remove();
				iterator.add(currentProduct);
			}

		}

	}

	private void parseMessage3(String[] productArray, List<Product> products) {
		Product currentProduct = new Product();

		String[] temp = productArray[0].split(" ");

		Double price = Double.parseDouble(temp[1].replaceAll("p", ""));

		String name = productArray[1];

		if (temp[0].equalsIgnoreCase("add")) {
			ListIterator<Product> iterator = products.listIterator();
			while (iterator.hasNext()) {
				Product product = iterator.next();

				if (product.getName().trim().startsWith(name.trim())) {

					currentProduct.setName(product.getName().trim());
					currentProduct.setPrice(product.getPrice()
							+ (price * product.getSaleCount()));
					currentProduct.setSaleCount(product.getSaleCount());

					iterator.remove();
					iterator.add(currentProduct);

				}

			}

		} else if (temp[0].equalsIgnoreCase("subs")) {
			ListIterator<Product> iterator = products.listIterator();
			while (iterator.hasNext()) {
				Product product = iterator.next();

				if (product.getName().trim().startsWith(name.trim())) {

					currentProduct.setName(product.getName().trim());
					currentProduct.setPrice(product.getPrice()
							- (price * product.getSaleCount()));
					currentProduct.setSaleCount(product.getSaleCount());

					iterator.remove();
					iterator.add(currentProduct);
				}

			}
		} else if (temp[0].equalsIgnoreCase("mult")) {
			ListIterator<Product> iterator = products.listIterator();
			while (iterator.hasNext()) {
				Product product = iterator.next();

				if (product.getName().trim().startsWith(name.trim())) {

					currentProduct.setName(product.getName().trim());
					currentProduct.setPrice(product.getPrice()
							* (price * product.getSaleCount()));
					currentProduct.setSaleCount(product.getSaleCount());

					iterator.remove();
					iterator.add(currentProduct);
				}

			}
		}

	}

	@Override
	public void loadSaleMessages() {
		for (Product product : saleLogList) {
			log.info("Sale for " + product.getName() + " count "
					+ product.getSaleCount() + " price " + product.getPrice());
		}

	}

	@Override
	public void loadAdjustmentMessages() {
		saleLog.loadAdjustmentReports();

	}

}
