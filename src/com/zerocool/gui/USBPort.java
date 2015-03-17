package com.zerocool.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class USBPort extends JPanel {

	private static final long serialVersionUID = 1L;

	public USBPort() {
		setBorder(null);
		createContents();
	}
	
	private void createContents() {
		port = new JLabel("[        ]");
		add(port);
		
		description = new JLabel("USB PORT");
		add(description);
	}
	
	private JLabel port;
	private JLabel description;	
}
