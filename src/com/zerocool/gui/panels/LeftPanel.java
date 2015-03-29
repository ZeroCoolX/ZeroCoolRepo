package com.zerocool.gui.panels;

import java.awt.Color;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;
import com.zerocool.gui.buttons.FuntionButton;
import com.zerocool.gui.buttons.PowerButton;
import com.zerocool.gui.buttons.SwapButton;

import net.miginfocom.swing.MigLayout;

public class LeftPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	
	public LeftPanel(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		setBorder(null);
		setLayout(new MigLayout("fill", "[center]", "0px:10px [] 0px:182px [] 0px:15px [] 0px:50px [] 0px:81px"));
		createContents();
	}
	
	@Override
	protected void createContents() {
		powerButton = new PowerButton(main, admin, console, printer, "Power", PowerButton.Type.Main);
		add(powerButton, "cell 0 0");
		
		functionButton = new FuntionButton(main, admin, console, printer, "Function");
		add(functionButton, "cell 0 1");
		
		arrows = new ArrowPanel(main, admin, console, printer, channels, getBackground());
		add(arrows, "cell 0 2");
		
		swapButton = new SwapButton(main, admin, console, printer, "Swap");
		add(swapButton, "cell 0 3");
	}

	@Override
	public void update() {
		powerButton.update();
		functionButton.update();
		arrows.update();
		swapButton.update();
	}
	
	@Override
	public void toggleEnabled(boolean enabled) {
		powerButton.toggleEnabled(enabled);
		functionButton.toggleEnabled(enabled);
		arrows.toggleEnabled(enabled);
		swapButton.toggleEnabled(enabled);
	}
	
	private PowerButton powerButton;
	private FuntionButton functionButton;
	private ArrowPanel arrows;
	private SwapButton swapButton;
}
