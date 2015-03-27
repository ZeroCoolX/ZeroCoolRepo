package com.zerocool.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class ChannelButton extends JButton {

	private static final long serialVersionUID = 1L;
	private final Color deep_sky_blue = new Color(0,191,255);
	private final Color light_coral = new Color(240,128,128);

	Dimension size;
	
	private boolean connected = false;
	
	public ChannelButton() {
		size = new Dimension(20, 20);
		
		setText("");
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		toggleConnection(false);
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				connected = !connected;
				toggleConnection(connected);
			}
			
		});
	}
	
	public void toggleConnection(boolean on){
		setBackground(on?light_coral:deep_sky_blue);
		setContentAreaFilled(false);
		setOpaque(true);
		setBorderPainted(false);	
	}
	
	
	
}
