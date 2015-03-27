package com.zerocool.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class PowerIndicator extends JButton{	
	
	private final Dimension size = new Dimension(20, 20);
	
	private final Color chartreuse = new Color(127,255,0);
	private final Color crimson = new Color(220,20,60);

	public PowerIndicator() {
		createContents();
	}
	
	public void createContents(){
		
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		togglePower(false);
		
	}
	
	public void togglePower(boolean on){
		setBackground(on?chartreuse:crimson);
		setContentAreaFilled(false);
		setOpaque(true);
		setBorderPainted(false);	
	}

}
