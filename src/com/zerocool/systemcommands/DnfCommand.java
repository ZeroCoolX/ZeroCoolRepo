package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;
import com.zerocool.controllers.Timer;

public class DnfCommand implements Command {

	private SystemController controller;
	
	public DnfCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * End the participant within event..but...not as cool as the REGULAR finish.
	 */
	@Override
	public void execute(String... args) {
		Timer timer = controller.getTimer();
		timer.setDnf();
		
		if (timer.getCurrentEvent().getRunningQueue().isEmpty()) {
			controller.getEventLog().logParticipants(timer.getEventParticipantData(), controller.getSystemTime());
		}
	}

}
