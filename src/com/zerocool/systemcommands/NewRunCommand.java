package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;

public class NewRunCommand implements Command {
	
	private SystemController controller;
	
	public NewRunCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Creates a new Run.
	 **/
	@Override
	public void execute(String... args) {
		controller.getTimer().createNewRun();
	}

}
