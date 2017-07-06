package com.jpmc.processor.model;

public class Product {

	String name;
	Double price = 0.0;
	Double saleCount = 0.0;

	public Product() {

	}

	public Product(String name, Double price, Double saleCount) {
		super();
		this.name = name;
		this.price = price;
		this.saleCount = saleCount;
	}

	public String getName() {
		return name;
	}

	public Product setName(String name) {
		this.name = name;
		return this;
	}

	public Double getPrice() {
		return price;
	}

	public Product setPrice(Double price) {
		this.price = price;
		return this;
	}

	public Double getSaleCount() {
		return saleCount;
	}

	public Product setSaleCount(Double saleCount) {
		this.saleCount = saleCount;
		return this;
	}

	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;
		if (!(o instanceof Product)) {
			return false;
		}

		Product product = (Product) o;

		return product.name.equals(name);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + name.hashCode();
		result = (int) (31 * result + price);
		return result;
	}

}
