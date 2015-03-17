package com.zerocool.gui;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;


public class KeyPad extends JPanel {

	private static final long serialVersionUID = 1L;

	private Key[] keys;
	
	public KeyPad() {
		setBorder(null);
		setLayout(new MigLayout("gapy 0, gapx 0", "[fill] [fill] [fill]", "[] [] [] [] []"));
		
		createContents();
	}
	
	private void createContents() {
		keys = new Key[13];
		
		String[] keyNames = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#", "< Back" };
		for (int i = 0; i < keys.length; ++i) {
			keys[i] = new Key(keyNames[i]);
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
	
	private class Key extends JButton {
		
		private static final long serialVersionUID = 1L;
		
		private Dimension size;
		private String print;
		
		public Key(String print) {
			super(print);
			
			size = new Dimension(40, 40);
		//	setMinimumSize(size);
		//	setMaximumSize(size);
			setPreferredSize(size);
			
			this.print = print;
		}
		
	}
}