package com.zerocool.gui.panels;

import java.awt.Color;

import javax.swing.JPanel;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Observer;
import com.zerocool.gui.Printer;

public abstract class AbstractPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	protected SystemController admin;
	protected Console console;
	protected Printer printer;
	protected ChannelGroup channels;
	
	public AbstractPanel(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super();
		this.main = main;
		this.admin = admin;
		this.console = console;
		this.printer = printer;
		this.channels = channels;
		setBackground(background);
	}
	
	protected abstract void createContents();
	
	public abstract void toggleEnabled(boolean enabled);
}
