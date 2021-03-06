package com.zerocool.gui;

import javax.swing.JLabel;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.buttons.ChannelButton;
import com.zerocool.gui.buttons.ToggleButton;

public class ChannelGroup {

	private ChannelButton[] channels;
	
	public ChannelGroup(Main main, SystemController admin, Console console, Printer printer) {
		channels = new ChannelButton[8];
		for (int i = 0; i < 8; ++i) {
			channels[i] = new ChannelButton(main, admin, console, printer, "", i + 1);
		}
	}
	
	public ChannelButton getChannel(int channel) {
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Not a valid channel!");
		}
		
		return channels[channel - 1];
	}
	
	public ToggleButton getChannelConnectButton(int channel) {
		return getChannel(channel).getConnectButton();
	}
	
	public ToggleButton getChannelEnableButton(int channel) {
		return getChannel(channel).getEnableButton();
	}
	
	public JLabel getChannelLabel(int channel, boolean front) {
		return getChannel(channel).getJLabel(front);
	}
	
	public int getChannelCount() {
		return channels.length;
	}
	
	public void update() {
		for (ChannelButton cb : channels) {
			cb.update();
		}
	}
	
	public void toggleEnabled(boolean enabled) {
		for (ChannelButton cb : channels) {
			cb.toggleEnabled(enabled);
		}
	}
}
