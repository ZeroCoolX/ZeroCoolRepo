package com.zerocool.systemcommands;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.Channel;

public class ToggleCommand implements Command {

	private SystemController controller;
	
	public ToggleCommand(SystemController controller) {
		this.controller = controller;
	}
	
	/**
	 * Turns the given channel on if it was off or off if it was on.
	 * 
	 * @throws IllegalArgumentException If the channel was not [1, 8].
	 */
	@Override
	public void execute(String... args) {
		int channel = Integer.parseInt(args[0]);
		// cmdTog(Integer.parseInt(args[0]));

		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Invalid channel number. Channels are 1-8.");
		}
		
		Channel[] channels = controller.getChannels();
		channels[channel - 1].setState(!channels[channel - 1].getState());
	}

}
