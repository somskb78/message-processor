package com.jpmc.processor.service;

import com.jpmc.processor.model.NotificationMessage;

public interface CallBackService {

	public void publishMessage(NotificationMessage messsage);
	
	public void loadSaleMessages();
	
	public void loadAdjustmentMessages();
}
