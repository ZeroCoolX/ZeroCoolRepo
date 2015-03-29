package com.zerocool.gui.buttons;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.Console;

public class ToggleButton extends JRadioButton {

	private static final long serialVersionUID = 1L;

	public enum Type { CONNECT, ENABLE };
	
	private final Color red = new Color(178, 34, 34);
	private final Color green = new Color(34, 178, 34);
	private final Color black = new Color(13, 13, 13);
	
	private SystemController admin;
	private Console console;
	private Dimension size;
	
	private Type type;
	private int id;
	private boolean enabled;
	
	public ToggleButton(SystemController admin, Console console, Type type, int id) {
		super();
		this.admin = admin;
		this.console = console;
		this.type = type;
		this.id = id;
		size = new Dimension(20, 20);
		setPrefs();
	}
	
	private void setPrefs() {
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (type.equals(Type.CONNECT)) {
					admin.executeCommand(admin.getSystemTime() + (enabled ? "\tDISC " : "\tCONN GATE ") + id, false);
				} else if (type.equals(Type.ENABLE)) {
					admin.executeCommand(admin.getSystemTime() + "\tTOGGLE " + id, false);
				}
				enabled = !enabled;
			}
			
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		int ovalX =  Math.floorDiv(width, 4);
		int ovalY = Math.floorDiv(height, 4);
		int ovalWidth = Math.floorDiv(width, 2);
		int ovalHeight = Math.floorDiv(height, 2);
		
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setBackground(getBackground());
		g2.clearRect(0, 0, width, height);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		g2.setPaint(enabled ? green : red);
		g2.fillOval(ovalX, ovalY, ovalWidth + 1, ovalHeight + 1);
		
		g2.setPaint(black);
		g2.drawOval(ovalX, ovalY, ovalWidth, ovalHeight);
		
		g2.dispose();
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
}
