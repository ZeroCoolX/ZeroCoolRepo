package com.zerocool.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.zerocool.controllers.SystemController;


public class ChannelButton extends JButton {

	private static final long serialVersionUID = 1L;
	private final Color deep_sky_blue = new Color(0,191,255);
	private final Color dark_orange = new Color(255,140,0);
	
	private final int id;
	
	private final SystemController privateAdmin;

	Dimension size;
	
	private boolean connected = false;
	
	public ChannelButton(final SystemController privateAdmin,final int id) {
		this.id = id;
		size = new Dimension(20, 20);
		this.privateAdmin = privateAdmin;
		setText("");
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		//toggleConnection(false);
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				connected = !connected;
				privateAdmin.executeCommand(privateAdmin.getSystemTime() + (id%2==0?"\tFIN "+privateAdmin.getTimer().getCurrentEvent().getCompetingParticipants().peek().getId():"\tSTART"), false);
				//toggleConnection(connected);
			}
			
		});
	}
	
	public void toggleConnection(boolean on){
		setBackground(on?dark_orange:deep_sky_blue);
		setContentAreaFilled(false);
		setOpaque(true);
		setBorderPainted(false);	
	}
	
	
	
}
