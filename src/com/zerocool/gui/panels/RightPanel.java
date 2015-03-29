package com.zerocool.gui.panels;

import java.awt.Color;

import net.miginfocom.swing.MigLayout;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;

public class RightPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;

	public RightPanel(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		setBorder(null);
		setLayout(new MigLayout("fill, gapy 0", "[center]", "[] 0px:17px [] 0px:25px"));
		createContents();
	}

	@Override
	protected void createContents() {
		add(printer, "cell 0 0");
		
		keyPad = new KeyPad(main, admin, console, printer, channels, getBackground());
		add(keyPad, "cell 0 1");
	}

	@Override
	public void update() {
		printer.update();
		keyPad.update();
	}
	
	@Override
	public void toggleEnabled(boolean enabled) {
		printer.toggleEnabled(enabled);
		keyPad.toggleEnabled(enabled);
	}
	
	private KeyPad keyPad;
}
