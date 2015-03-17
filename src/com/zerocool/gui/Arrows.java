package com.zerocool.gui;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;


public class Arrows extends JPanel {

	private static final long serialVersionUID = 1L;

	public Arrows() {
		setBorder(null);
		setLayout(new MigLayout("gapx 0, gapy 0", "[] [] 15 [] []", "[]"));
		
		createContents();
	}
	
	private void createContents() {
		left = new JButton("L");
		add(left);
		
		right = new JButton("R");
		add(right);
		
		down = new JButton("D");
		add(down);
		
		up = new JButton("U");
		add(up);
	}
	
	private JButton	left;
	private JButton right;
	private JButton down;
	private JButton up;
}
