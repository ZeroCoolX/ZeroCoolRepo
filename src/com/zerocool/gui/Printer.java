package com.zerocool.gui;
import java.awt.Color;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.buttons.PowerButton;
import com.zerocool.gui.panels.AbstractPanel;

import net.miginfocom.swing.MigLayout;


public class Printer extends AbstractPanel {

	private static final long serialVersionUID = 1L;
		
	public Printer(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		
		setBorder(null);
		setLayout(new MigLayout("insets 0px", "[center]", "[] 0px:15px []"));
		createContents();
	}
	
	@Override
	protected void createContents() {
		powerButton = new PowerButton(main, admin, console, this, "Printer Pwr", PowerButton.Type.Printer);
		add(powerButton, "cell 0 0");
		
		view = new PrinterView(admin);
		scrollPane = new LightScrollPane(view, 150, 150);
		
		add(scrollPane, "cell 0 1");
	}
	
	@Override
	public void update() {
		powerButton.update();
		view.update();
	}
	
	@Override
	public void toggleEnabled(boolean enabled) {
		powerButton.setEnabled(enabled);
		view.toggleEnabled(enabled);
	}
	
	public void addText(String text) {
		view.addText(text);
	}
	
	public void clearScreen() {
		view.setText("");
	}
	
	private PowerButton powerButton;
	private PrinterView view;
	private LightScrollPane scrollPane;

}
