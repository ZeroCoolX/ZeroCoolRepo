package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;

public class ElapsedCommand implements Command {
	private SystemController controller;
	
	public ElapsedCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Prints the elapsed time of all the finished participants.
	 */
	@Override
	public void execute(String... args) {
		String elapsedText = "\nElapsed Time: " + controller.getTimer().getEventParticipantElapsedData() + "\n";
		System.out.println(elapsedText);
	}

}
