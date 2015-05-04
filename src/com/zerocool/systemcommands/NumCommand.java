package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;

public class NumCommand implements Command {
	
	private SystemController controller;
	
	public NumCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Adds a Participant to the current event.
	 * 
	 */
	@Override
	public void execute(String... args) {

		//cmdNum(Integer.parseInt(args[0]));
		int participantId = Integer.parseInt(args[0]);
		controller.getTimer().addParticipantToStart(participantId);
	}

}
