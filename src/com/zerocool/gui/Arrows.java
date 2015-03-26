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
		setLayout(new MigLayout("gapx 0px, gapy 0px", "[center] 0px:15px [center]", "[] 0px:15px [] 0px:15px []"));
		
		createContents();
	}
	
	private void createContents() {
		up = new JButton("U");
		up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				console.moveUp();
			}
			
		});
		add(up, "cell 0 0, span 2");
		
		left = new JButton("L");
		left.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				console.prevCommand();
			}
			
		});
		add(left, "cell 0 1");
		
		right = new JButton("R");
		right.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				console.nextCommand();
			}
			
		});
		add(right, "cell 1 1");
		
		down = new JButton("D");
		down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				console.moveDown();
			}
			
		});
		add(down, "cell 0 2, span 2");
	}
	
	private JButton	left;
	private JButton right;
	private JButton down;
	private JButton up;
}
