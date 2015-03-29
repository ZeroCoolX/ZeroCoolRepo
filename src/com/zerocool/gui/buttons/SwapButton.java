package com.zerocool.gui.buttons;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;

public class SwapButton extends AbstractButton {

	private static final long serialVersionUID = 1L;

	public SwapButton(Main main, SystemController admin, Console console, Printer printer, String text) {
		super(main, admin, console, printer, text);

		setPrefs();
	}

	@Override
	protected void setPrefs() {
		// TODO Add ActionListener!
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
