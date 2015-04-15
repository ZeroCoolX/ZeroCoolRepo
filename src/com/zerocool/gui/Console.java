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
		view = new ConsoleView(admin);
		
		scrollPane = new LightScrollPane(view, 225, 225);

		add(scrollPane, "cell 0 0");

		description = new JLabel("Queue / Running / Final Time");
		description.setFont(new Font("Tahoma", Font.PLAIN, 10));

		add(description, "cell 0 1");
	}
	
	@Override
	public void update() {
		view.getView(true);
		view.update();
	}
	
	@Override
	public void toggleEnabled(boolean enabled) {
		view.toggleEnabled(enabled);
	}

	public void prevCommand() {
		view.prevCommand();
	}

	public void nextCommand() {
		view.nextCommand();
	}
	
	public String currentCommand() {
		return view.currentCommand();
	}

	public void moveUp() {
		view.moveUp();
	}

	public void moveDown() {
		view.moveDown();
	}
	
	public void setNewText(String text){
		view.setText(text);
	}
	
	public ConsoleView getView(){
		return view;
	}
	
	public void setCommandArgComboView(){		
 		//System.out.println("setting text view as: " + view.getView(true) + " "+ view.getCommandArgCombo());		 
		//view.setText(view.getView(true) + " "+ view.getCommandArgCombo());
		
 		System.out.println("setting text view as: " + view.getView(true));		 
		view.setText(view.getView(true));
 	}
	
	public boolean isScanPrompting() {		
		return view.isScanPrompting();		
	}		
					
	public void promptScanner(int num){		
		view.promptScanner(num);		
	}
	
	public void printErrorMessage(String error) {
		printer.printInvalidCommandErrorMessage(error);
	}
	
	private ConsoleView view;
	private LightScrollPane scrollPane;
	private JLabel description;

}
