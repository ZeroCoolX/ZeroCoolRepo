package com.zerocool.gui.panels;
import java.awt.Color;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;

import net.miginfocom.swing.MigLayout;


public class BackChannelPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	
	public BackChannelPanel(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		setBorder(null);
		setLayout(new MigLayout("", "[center] [center] [center] [center]", "[] [] [] []"));
		createContents();
	}
	
	@Override
	protected void createContents() {
		for (int i = 0; i < channels.getChannelCount(); ++i) {
			channels.getChannelConnectButton(i + 1).setBackground(getBackground());
			add(channels.getChannelLabel(i + 1, false), "cell " + i % 4 + " " + (i < 4 ? 0 : 2));
			add(channels.getChannelConnectButton(i + 1), "cell " + i % 4 + " " + (i < 4 ? 1 : 3));
		}
	}

	@Override
	public void update() {
		// TODO
	}

	@Override
	public void toggleEnabled(boolean enabled) {
		// TODO
	}
	
}
