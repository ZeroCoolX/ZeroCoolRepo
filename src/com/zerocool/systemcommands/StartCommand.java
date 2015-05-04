package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.Channel;

public class StartCommand implements Command {
	private SystemController controller;
	
	public StartCommand(SystemController controller) {
		this.controller = controller;
	}
	
	@Override
	public void execute(String... args) {
		// same as trigger channel 1
		int channel = 1;
		controller.setLastTrigger(channel);
		Channel[] channels = controller.getChannels();
		// lastTrigger = channel;
		
		channels[channel - 1].triggerSensor();
		
		// TODO This is not always true.  In PAR events it is possible that a top channel
		//	could finish participants.
		if (channel % 2 == 0) {
			controller.getEventLog().logParticipants(controller.getTimer().getEventParticipantData(), controller.getSystemTime());
		}
	}
}
