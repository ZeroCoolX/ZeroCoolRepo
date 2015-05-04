package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.AbstractEvent.EventType;

public class EventCommand implements Command {

	private SystemController controller;
	
	public EventCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Creates a new Event based upon the given String.  If the String is not a valid
	 * event, shit hits the fan.
	 * 
	 */
	@Override
	public void execute(String... args) {
		// cmdEvent(args[0]);
		String event = args[0];
		controller.getTimer().createEvent(EventType.valueOf(event), event);
		controller.getEventLog().logEvent(controller.getTimer().getEventData(), controller.getSystemTime());
	}

}
