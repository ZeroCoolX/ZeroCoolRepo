package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.Channel;

public class ConnectCommand implements Command {
	
	private SystemController controller;
	
	public ConnectCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Connects a sensor to the given channel.
	 * 
	 * @throws IllegalArgumentException If the channel was not [1, 8].
	 */
	@Override
	public void execute(String... args) {
		// cmdConn(args[0], Integer.parseInt(args[1]));
		
		int channel = Integer.parseInt(args[1]);
		String sensorType = args[0];
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Sensor cannot connect. Invalid channel number, channels are 1-8.");
		}
		
		Channel[] channels = controller.getChannels();
		channels[channel - 1].addSensor(sensorType);
	}

}
