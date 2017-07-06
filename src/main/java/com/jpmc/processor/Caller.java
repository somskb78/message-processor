package com.jpmc.processor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.jpmc.processor.model.NotificationMessage;
import com.jpmc.processor.service.CallBackService;
import com.jpmc.processor.service.impl.CallBackServiceImpl;

/**
 * client
 * 
 * @author s
 *
 */
public class Caller {

	private static Logger log = Logger.getLogger(Caller.class);

	public void register(CallBackService callback, NotificationMessage message) {
		callback.publishMessage(message);
	}

	public static void main(String[] args) {

		Caller caller = new Caller();
		CallBackService callBack = new CallBackServiceImpl();

		File f = new File("messages.txt");

		if (!f.canRead()) {
			f = new File("src/main/resources/messages.txt");
		}

		List<String> lines = null;
		try {
			lines = FileUtils.readLines(f, "UTF-8");

			for (String line : lines) {
				NotificationMessage message = new NotificationMessage();
				message.setMessage(line);
				log.debug(message.getMessage());
				caller.register(callBack, message);
			}

			callBack.loadSaleMessages();
			callBack.loadAdjustmentMessages();
		} catch (IOException e) {
			log.error(e);
		}
	}
}