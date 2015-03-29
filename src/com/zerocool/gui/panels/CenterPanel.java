package com.zerocool.gui.panels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;

public class CenterPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;

	public CenterPanel(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		setBorder(null);
		setLayout(new MigLayout("fill", "[]", "[] 0px: 5px [] []"));
		createContents();
	}

	@Override
	protected void createContents() {
		title = new JLabel(Main.TITLE + "  ");
		title.setFont(new Font("Tahoma", Font.ITALIC, 15));
		add(title, "cell 0 0, center");
		
		frontChannelPanel = new FrontChannelPanel(main, admin, console, printer, channels, getBackground());
		add(frontChannelPanel, "cell 0 1");
		
		add(console, "cell 0 2, right");
	}
	
	@Override
	public void update() {
		frontChannelPanel.update();
	}
	
	@Override
	public void toggleEnabled(boolean enabled) {
		frontChannelPanel.setEnabled(enabled);
	}

	private JLabel title;
	private FrontChannelPanel frontChannelPanel;
}
