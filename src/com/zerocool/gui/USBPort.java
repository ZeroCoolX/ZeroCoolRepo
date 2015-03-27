package com.zerocool.gui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class USBPort extends JPanel {

	private static final long serialVersionUID = 1L;
	private final Color dark_grey = new Color(105,105,105);

	public USBPort() {
		setBorder(null);
		setBackground(dark_grey);
		setForeground(Color.WHITE);
		createContents();
	}
	
	private void createContents() {
		port = new JLabel("[         ]");
		port.setForeground(Color.WHITE);
		add(port);
		
		description = new JLabel("USB PORT");
		description.setForeground(Color.WHITE);
		add(description);
	}
	
	public void setNewText(String text){
		port.setText(text);
	}
	
	private JLabel port;
	private JLabel description;	
}
