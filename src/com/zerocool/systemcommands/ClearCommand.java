package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;

public class ClearCommand implements Command {
	
	private SystemController controller;
	
	public ClearCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/***
	 * Clears the participant id in the arguments from the event's starting queue.
	 */
	@Override
	public void execute(String... args) {
		//currentTimer.clear(participantId);
		// cmdClr(Integer.parseInt(args[0]));
		int participantId = Integer.parseInt(args[0]);
		controller.getTimer().clear(participantId);
	}

}
