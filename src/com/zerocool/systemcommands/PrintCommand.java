package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;

public class PrintCommand implements Command {
	
	private SystemController controller;
	
	public PrintCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Prints a specified run.
	 */
	@Override
	public void execute(String... args) {
		controller.setShouldPrint(true);
	}

}
