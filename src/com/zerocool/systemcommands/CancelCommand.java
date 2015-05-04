package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;

public class CancelCommand implements Command {
	private SystemController controller;
	
	public CancelCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Cancels the current run and resets it.
	 */
	@Override
	public void execute(String... args) {
		// TODO Auto-generated method stub
		controller.getTimer().cancelStart();
	}

}
