package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.Channel;

public class ExitCommand implements Command {

	private SystemController controller;
	
	public ExitCommand(SystemController controller) {
		this.controller = controller;
	}
	
	@Override
	public void execute(String... args) {
		controller.resetServer();
		controller.setOn(false);
		controller.setIsRunning(false);
		controller.getSystemTime().exit();
		controller.setSystemTime(null);
		controller.setIsPrinterOn(false);
		if (controller.getTimer() != null) {
			controller.getTimer().exit();
			controller.setTimer(null);
		}
		
		controller.getTaskList().clearTasks();
		controller.setTaskList(null);
		
		if (controller.getChannels() != null) {
			for (Channel chan : controller.getChannels()) {
				chan.exit();
			}
		}
		
		if (controller.getEventLog() != null) {
			controller.getEventLog().exit();
		}
		
		controller.setAutoDetect(null);
		controller.stopEventLoop();

		//cannot totally system exit for testing purposes...
		System.exit(1);
		
//		running = false;
//		systemTime.exit();
//		systemTime = null;
//		isPrinterOn = false;
//
//		if (currentTimer != null) {
//			currentTimer.exit();
//			currentTimer = null;
//		}
//
//		taskList.clearTasks();
//		taskList = null;
//
//		if (channels != null) {
//			for (Channel chan : channels) {
//				chan.exit();
//			}
//		}
//
//		if (eventLog != null) {
//			eventLog.exit();
//		}
//
//		detector = null;
//		loop.interrupt();
	}

}
