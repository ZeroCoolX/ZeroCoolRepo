package com.zerocool.gui.panels;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;
import com.zerocool.gui.buttons.AbstractButton;


public class USBPort extends AbstractButton {

	private static final long serialVersionUID = 1L;
	
	private Dimension size;

	public USBPort(Main main, SystemController admin, Console console, Printer printer, String text) {
		super(main, admin, console, printer, text);
		
		size = new Dimension(55 + getFontMetrics(getFont()).stringWidth(text), 16);
		setPrefs();
	}
	
	@Override
	protected void setPrefs() {
		setBorder(null);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Something?  Idk
			}
			
		});
	}
	
	@Override
	public void update() {
		//if (admin.getAutoDetect().driveConnected()) {
			repaint();
		//}
	}

	@Override
	public void toggleEnabled(boolean enabled) {
		setEnabled(enabled);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		FontMetrics fontMetrics = getFontMetrics(getFont());
		String text = getText();
		int width = getWidth();
		int height = getHeight();
		
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setBackground(Main.GREY);
		g2.clearRect(0, 0, width, height);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		g2.setPaint(Color.DARK_GRAY);
		g2.fillRect(0, 0, 50, height);
		g2.setPaint(isEnabled() ? admin.getAutoDetect().driveConnected() ? Main.GREEN : Main.RED : Main.BLACK);
		g2.fillRect(5, 5, 50 - 10, height - 10);
		
		g2.setPaint(Main.BLACK);
		g2.drawRect(0, 0, 50 - 1, height - 1);
		
		g2.setFont(getFont());
		g2.setColor(Color.BLACK);
		g2.drawString(text, 50 + 5, (height / 2) + (fontMetrics.getHeight() / 4) + 1);
		
		g2.dispose();
	}
	
}
