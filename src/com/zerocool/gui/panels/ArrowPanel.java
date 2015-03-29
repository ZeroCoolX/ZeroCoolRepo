package com.zerocool.gui.panels;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;

import net.miginfocom.swing.MigLayout;


public class ArrowPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	
	public ArrowPanel(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		setBorder(null);
		setLayout(new MigLayout("gapx 0px, gapy 0px", "[center] 0px:15px [center]", "[] 0px:15px [] 0px:15px []"));
		createContents();
	}

	@Override
	protected void createContents() {
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
	
	@Override
	public void update() {
		// do nothing
	}

	@Override
	public void toggleEnabled(boolean enabled) {
		left.setEnabled(enabled);
		right.setEnabled(enabled);
		down.setEnabled(enabled);
		up.setEnabled(enabled);
	}
	
	private JButton	left;
	private JButton right;
	private JButton down;
	private JButton up;
}
