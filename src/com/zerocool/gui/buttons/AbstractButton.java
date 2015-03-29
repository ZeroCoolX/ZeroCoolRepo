package com.zerocool.gui.buttons;

import javax.swing.JButton;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Observer;
import com.zerocool.gui.Printer;

public abstract class AbstractButton extends JButton implements Observer {

	private static final long serialVersionUID = 1L;

	protected Main main;
	protected SystemController admin;
	protected Console console;
	protected Printer printer;

	public AbstractButton(Main main, SystemController admin, Console console, Printer printer, String text) {
		super(text);
		this.main = main;
		this.admin = admin;
		this.console = console;
		this.printer = printer;
	}
	
	protected abstract void setPrefs();
	
	public abstract void toggleEnabled(boolean enabled);
	
}
