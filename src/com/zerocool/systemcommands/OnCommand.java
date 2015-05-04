package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.AbstractEvent.EventType;

public class OnCommand implements Command {

	private SystemController controller;
	
	public OnCommand(SystemController controller) {
		this.controller = controller;
	}
	
	@Override
	public void execute(String... args) {

		// When the command ON is entered/read then the time needs to start.
		// As
		// of now upon instantiation of this class the systemTime is
		// started...not sure if that should happen HERE or THERE
		// What happens when OFF is entered...then ON again right after it?
		// IDK
		// we need to converse on this
		// printer set to false for default state
		if (controller.getEventLog() == null) {
			controller.createEventLog();
		}
		if (controller.getTimer() == null) {
			controller.createTimer();
			//NO event should be logging because...there isn't en event to be logged till the user specifies what event to run with the cmd "for example": EVENT IND
			
			// same thing as cmdEvent("IND")
			controller.getTimer().createEvent(EventType.valueOf("IND"), "IND");
			controller.getEventLog().logEvent(controller.getTimer().getEventData(), controller.getSystemTime());
		}
		if (controller.getChannels() == null) {
			controller.createChannels();
		}
		if (controller.getAutoDetect() == null) {
			 controller.createAutoDetect();
		}
		// printer set to false for insurance
		controller.setIsPrinterOn(false);

	}

}
