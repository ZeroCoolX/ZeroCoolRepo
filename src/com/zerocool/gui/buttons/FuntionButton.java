package com.zerocool.gui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;

public class FuntionButton extends AbstractButton {

	private static final long serialVersionUID = 1L;

	public FuntionButton(Main main, SystemController admin, Console console, Printer printer, String text) {
		super(main, admin, console, printer, text);

		setPrefs();
	}

	@Override
	protected void setPrefs() {
		addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(console.currentCommand());
				//	String totalLine = (admin.getSystemTime() + "\t" + console.currentCommand() + " " + console.getConsoleView().getArgs());
				if (admin.getIsPrinterOn()) {//meaning the printer is turned on
					//		printer.addText(totalLine);
				}
				//	admin.executeCommand(totalLine, false);//STILL NEED TO GET THE NUMBER CONCATENATED ONTO THE COMMMAND!!!!!
				console.setNewText(">" + console.currentCommand());
			}

		});
	}

	@Override
	public void update() {
		// DO NOTHING
	}

	@Override
	public void toggleEnabled(boolean enabled) {
		setEnabled(enabled);
	}

}
