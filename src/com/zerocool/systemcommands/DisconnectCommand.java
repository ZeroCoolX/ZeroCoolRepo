package com.zerocool.systemcommands;


import com.zerocool.controllers.SystemController;
import com.zerocool.entities.Channel;

public class DisconnectCommand implements Command {
	
	private SystemController controller;
	
	public DisconnectCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Disconnects the given channel.
	 * 
	 * @throws IllegalArgumentException If the channel was not [1, 8].
	 */
	@Override
	public void execute(String... args) {
		// cmdDisc(Integer.parseInt(args[0]));
		
		int channel = Integer.parseInt(args[0]);
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Invalid channel number. Channels are 1-8.");
		}
		
		Channel[] channels = controller.getChannels();
		channels[channel - 1].disconnectSensor();
	}

}
