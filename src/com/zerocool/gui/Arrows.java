package com.zerocool.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;


public class Arrows extends JPanel {

	private static final long serialVersionUID = 1L;

	private Console console;
	
	public Arrows(Console console) {
		this.console = console;
		
		setBorder(null);
		setLayout(new MigLayout("gapx 0, gapy 0", "[] [] 15 [] []", "[]"));
		
		createContents();
	}
	
	private void createContents() {
		left = new JButton("L");
		left.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				console.prevCommand();
			}
			
		});
		add(left);
		
		right = new JButton("R");
		right.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				console.nextCommand();
			}
			
		});
		add(right);
		
		down = new JButton("D");
		down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				console.moveDown();
			}
			
		});
		add(down);
		
		up = new JButton("U");
		up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				console.moveUp();
			}
			
		});
		add(up);
	}
	
	private JButton	left;
	private JButton right;
	private JButton down;
	private JButton up;
}
