package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.Channel;

public class RecallCommand implements Command {

	private SystemController controller;
	
	public RecallCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/***
	 * Recalls the most recent trigger event.
	 */
	@Override
	public void execute(String... args) {
		int lastTrigger = controller.getLastTrigger();
		
		if (lastTrigger > 0 && lastTrigger < 9) {
			
			// Trigger the channel again if in range.
			controller.setLastTrigger(lastTrigger);
			Channel[] channels = controller.getChannels();
		
			channels[lastTrigger - 1].triggerSensor();
			
			// TODO This is not always true.  In PAR events it is possible that a top channel
			//	could finish participants.
			if (lastTrigger % 2 == 0) {
				controller.getEventLog().logParticipants(controller.getTimer().getEventParticipantData(), controller.getSystemTime());
			}
		}
	}
//	
//	private void cmdRcl() {
//		if (lastTrigger > 0 && lastTrigger < 9) {
//			cmdTrig(lastTrigger);
//		}
//	}

}
