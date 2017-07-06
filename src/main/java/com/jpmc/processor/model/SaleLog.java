package com.jpmc.processor.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class SaleLog {

	enum ADJUSTMENT_TYPE {
		ADD, SUBSTRACT, MULTIPLY
	};

	private static Logger log = Logger.getLogger(SaleLog.class);

	List<String> adjustmentReports = new ArrayList<String>();

	public void createAdjustment(String adjustment) {
		adjustmentReports.add("Adjust : " + adjustment);
	}

	public void loadAdjustmentReports() {

		log.info(adjustmentReports);

	}

}
