package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;
import com.zerocool.controllers.Timer;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.services.EventLog;

public class ResetCommand implements Command {
	private SystemController controller;
	
	public ResetCommand(SystemController controller) {
		this.controller = controller;
	}

	@Override
	public void execute(String... args) {
		controller.setEventLog(new EventLog());
		controller.setTimer(new Timer(controller.getSystemTime(), EventType.IND, EventType.IND + ""));
		controller.getEventLog().logEvent(controller.getTimer().getEventData(), controller.getSystemTime());
		controller.setChannels(controller.populateChannels());
		controller.setIsPrinterOn(false);
		
//		eventLog = new EventLog();
//		currentTimer = new Timer(systemTime, EventType.IND, EventType.IND + "");
//		eventLog.logEvent(currentTimer.getEventData(), systemTime);
//
//		channels = populateChannels();
//		// printer set to false for insurance
//		isPrinterOn = false;
	}

}
