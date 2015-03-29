package com.zerocool.gui;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.panels.AbstractPanel;

import net.miginfocom.swing.MigLayout;


public class Console extends AbstractPanel {

	private static final long serialVersionUID = 1L;

	public Console(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		
		setBorder(null);
		setLayout(new MigLayout("", "[center]", "[] []"));
		createContents();
	}

	@Override
	protected void createContents() {
		view = new ConsoleView(admin, printer);
		
		view = new ConsoleView(admin, printer);
		scrollPane = new LightScrollPane(view, 200, 200);

		add(scrollPane, "cell 0 0");

		description = new JLabel("Queue / Running / Final Time");
		description.setFont(new Font("Tahoma", Font.PLAIN, 10));

		add(description, "cell 0 1");
	}
	
	@Override
	public void update() {
		// TODO update shit.
	}
	
	@Override
	public void toggleEnabled(boolean enabled) {
		view.toggleEnabled(enabled);
	}

	public void prevCommand() {
		view.prevCommand();
		view.setText(view.getView(false));
	}

	public void nextCommand() {
		view.nextCommand();
		view.setText(view.getView(false));
	}
	
	public String currentCommand() {
		return view.currentCommand();
	}
	
	public void setCommandArgComboView() {
		System.out.println("setting text view as: " + view.getView(true));
		view.setText(view.getView(true));
	}

	public void moveUp() {
		view.moveUp();
		view.setText(view.getView(false));
	}

	public void moveDown() {
		view.moveDown();
		view.setText(view.getView(false));
	}
	
	public void setNewText(String text){
		view.setText(text);
	}
	
	public boolean isScanPrompting() {
		return view.isScanPrompting();
	}
	
	public void promptScanner(int num){
		view.promptScanner(num);
	}
	
	private ConsoleView view;
	private LightScrollPane scrollPane;
	private JLabel description;

}
