package com.zerocool.gui.views;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;
import com.zerocool.gui.USBPort;
import com.zerocool.gui.panels.AbstractPanel;
import com.zerocool.gui.panels.BackChannelPanel;

public class BotView extends AbstractPanel {

	private static final long serialVersionUID = 1L;

	public BotView(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		
		setBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Back View", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new MigLayout("fill", "[] 0px:15px [] 0px:120px [] 0px:50px", "[] 0px:25px"));
		createContents();
	}

	@Override
	protected void createContents() {
		chan = new JLabel("CHAN");
		add(chan, "cell 0 0, top");
		
		backChannelPanel = new BackChannelPanel(main, admin, console, printer, channels, getBackground());
		add(backChannelPanel, "cell 1 0");
		
		usbPort = new USBPort(main, admin, console, printer, channels, getBackground());
		add(usbPort, "cell 2 0");
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void toggleEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	private JLabel chan;
	private BackChannelPanel backChannelPanel;
	private USBPort usbPort;
}
