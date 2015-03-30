package com.zerocool.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;

import net.miginfocom.swing.MigLayout;


public class KeyPad extends AbstractPanel {

	private static final long serialVersionUID = 1L;

	private Key[] keys;

	public KeyPad(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		setBorder(null);
		setLayout(new MigLayout("gapy 0px, gapx 0px", "[fill] [fill] [fill]", "[] [] [] [] []"));
		createContents();
	}
	
	@Override
	protected void createContents() {
		keys = new Key[13];
		
		String[] keyNames = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#", "< Back" };
		for (int i = 0; i < keys.length; ++i) {
			keys[i] = new Key(keyNames[i]);
			keys[i].addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					String print = ((Key) e.getSource()).getPrint();
					System.out.println("pressing number button : " + print);
					//Basically need to append the number pressed to the end of the current command in the ConsoleView console. 
					//console.getConsoleView().setCommandArgCombo("" + print);
					//console
				}
				
			});
		}
		
		add(keys[0], "cell 0 0");
		add(keys[1], "cell 1 0");
		add(keys[2], "cell 2 0");
		add(keys[3], "cell 0 1");
		add(keys[4], "cell 1 1");
		add(keys[5], "cell 2 1");
		add(keys[6], "cell 0 2");
		add(keys[7], "cell 1 2");
		add(keys[8], "cell 2 2");
		add(keys[9], "cell 0 3");
		add(keys[10], "cell 1 3");
		add(keys[11], "cell 2 3");
		add(keys[12], "cell 0 4 2 1");
	}
	
	@Override
	public void update() {
		// DO NOTHING
	}
	
	@Override
	public void toggleEnabled(boolean enabled) {
		for (Key k : keys) {
			k.setEnabled(enabled);
		}
	}
	
	private class Key extends JButton {
		
		private static final long serialVersionUID = 1L;
		
		private Dimension size;
		private String print;
		
		public Key(String print) {
			super(print);
			
			size = new Dimension(40, 40);
			setPreferredSize(size);
			this.print = print;
		}
		
		public String getPrint() {
			return print;
		}
		
	}
}