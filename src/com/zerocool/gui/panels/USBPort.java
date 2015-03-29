package com.zerocool.gui.panels;

import java.awt.Color;

import javax.swing.JLabel;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;


public class USBPort extends AbstractPanel {

	private static final long serialVersionUID = 1L;

	public USBPort(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		setBorder(null);
		createContents();
	}
	
	@Override
	protected void createContents() {
		port = new JLabel("[         ]");
		add(port);
		
		description = new JLabel("USB PORT");
		add(description);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toggleEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}
	
	private JLabel port;
	private JLabel description;
}
