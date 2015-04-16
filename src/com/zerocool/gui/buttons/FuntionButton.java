package com.zerocool.gui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
				String totalLine = (admin.getSystemTime() + "\t" + console.getView().getArgs());
				//console.getView().setCommandArgCombo(" "+console.currentCommand());		
				console.setCommandArgComboView();

				try {
					admin.executeCommand(totalLine, false);
				} catch (IllegalArgumentException exception) {
					console.printErrorMessage(exception.getMessage());
				}
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
