package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;

public class OffCommand implements Command {

	private SystemController controller;
	
	public OffCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Keep the SystemTime running but set everything else to null.
	 **/
	@Override
	public void execute(String... args) {
		// When the command OFF is entered/read then the time needs to stop.
		controller.setEventLog(null);
		controller.getTimer().resetEventId();
		controller.setTimer(null);
		controller.setChannels(null);
		controller.setAutoDetect(null);
		controller.setIsPrinterOn(false);
	}

}
