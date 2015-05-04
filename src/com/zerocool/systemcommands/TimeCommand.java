package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;

public class TimeCommand implements Command {
	private SystemController controller;
	
	public TimeCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Sets the systemTime variable to the given hour, minute, and second
	 * from the given String.
	 * 
	 **/
	@Override
	public void execute(String... args) {
		//args[0]
		controller.getSystemTime().setTime(args[0]);
		controller.getSystemTime().start();
//		// set the current time
//		systemTime.setTime(time);
//		systemTime.start();
	}

}
