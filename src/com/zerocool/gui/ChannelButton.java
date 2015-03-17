package com.zerocool.gui;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class ChannelButton extends JButton {

	private static final long serialVersionUID = 1L;

	Dimension size;
	
	public ChannelButton() {
		size = new Dimension(20, 20);
		
		setText("");
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	
	
}
