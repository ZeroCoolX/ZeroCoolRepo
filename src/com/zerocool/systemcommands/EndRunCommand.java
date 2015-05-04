package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;

public class EndRunCommand implements Command {
	
	private SystemController controller;
	
	public EndRunCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Ends the current running event if there is one.
	 **/
	@Override
	public void execute(String... args) {
		controller.getTimer().setDnf();
		controller.postResultsToServer();
//		currentTimer.setDnf();
//		
//		server.postToServer(currentTimer.getEventParticipantView());
	}

}
