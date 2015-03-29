package com.zerocool.gui.views;

import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;
import com.zerocool.gui.panels.AbstractPanel;
import com.zerocool.gui.panels.CenterPanel;
import com.zerocool.gui.panels.LeftPanel;
import com.zerocool.gui.panels.RightPanel;

public class TopView extends AbstractPanel {

	private static final long serialVersionUID = 1L;

	public TopView(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);

		setBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Top View", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new MigLayout("fill, gapx 0px", "0px:50px [] [] 0px:50px [] 0px:50px", "[]"));
		createContents();
	}

	@Override
	protected void createContents() {
		// This panel holds the Power, Function, Swap and Arrow buttons.
		leftPanel = new LeftPanel(main, admin, console, printer, channels, getBackground());
		add(leftPanel, "cell 0 0");

		// This panel holds the Title, Front Channels and Console.
		centerPanel = new CenterPanel(main, admin, console, printer, channels, getBackground());
		add(centerPanel, "cell 1 0");

		// This panel holds the Printer and Key Pad.
		rightPanel = new RightPanel(main, admin, console, printer, channels, getBackground());
		add(rightPanel, "cell 2 0");
	}

	@Override
	public void update() {
		leftPanel.update();
		centerPanel.update();
		rightPanel.update();
	}

	@Override
	public void toggleEnabled(boolean enabled) {
		leftPanel.setEnabled(enabled);
		centerPanel.setEnabled(enabled);
		rightPanel.setEnabled(enabled);
	}

	private LeftPanel leftPanel;
	private CenterPanel centerPanel;
	private RightPanel rightPanel;

}
