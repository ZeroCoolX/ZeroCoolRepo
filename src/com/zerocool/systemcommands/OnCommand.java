package com.zerocool.systemcommands;

import com.zerocool.controllers.AutoDetect;
import com.zerocool.controllers.SystemController;
import com.zerocool.controllers.Timer;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.services.EventLog;

public class OnCommand implements Command {

	private SystemController controller;
	
	public OnCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Instantiates all of the system controller's variables
	 * to an initial state.
	 */
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
			controller.setEventLog(new EventLog());
		}
		if (controller.getTimer() == null) {
			controller.setTimer(new Timer(controller.getSystemTime(), EventType.IND, EventType.IND.toString()));
			//NO event should be logging because...there isn't en event to be logged till the user specifies what event to run with the cmd "for example": EVENT IND
			
			// same thing as cmdEvent("IND")
			controller.getTimer().createEvent(EventType.valueOf("IND"), "IND");
			controller.getEventLog().logEvent(controller.getTimer().getEventData(), controller.getSystemTime());
		}
		if (controller.getChannels() == null) {
			controller.setChannels(controller.populateChannels());
		}
		if (controller.getAutoDetect() == null) {
			 controller.setAutoDetect(new AutoDetect());
		}
		// printer set to false for insurance
		controller.setIsPrinterOn(false);

	}

}
