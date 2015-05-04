package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;

public class SwapCommand implements Command {
	
	private SystemController controller;
	
	public SwapCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/****
	 * Swaps the first two participants in the starting queue.
	 * 
	 ****/
	@Override
	public void execute(String... args) {
		controller.getTimer().swap();
	}
}
