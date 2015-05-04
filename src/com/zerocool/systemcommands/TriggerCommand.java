package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.Channel;

public class TriggerCommand implements Command {
	
	private SystemController controller;
	
	public TriggerCommand(SystemController controller) {
		this.controller = controller;
	}
	
	@Override
	public void execute(String... args) {
		// cmdTrig(Integer.parseInt(args[0]));
		int channel = Integer.parseInt(args[0]);
		
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Invalid channel number. Channels are 1-8.");
		}
		
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
