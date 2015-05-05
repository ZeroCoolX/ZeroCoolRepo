package com.zerocool.gui.buttons;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;

public class PowerButton extends AbstractButton {

	private static final long serialVersionUID = 1L;

	public enum Type { Main, Printer };
	
	private Type type;
	private boolean on;
	
	public PowerButton(Main main, SystemController admin, Console console, Printer printer, String text, Type type) {
		super(main, admin, console, printer, text);
		this.type = type;
		setPrefs();
	}
	
	@Override
	protected void setPrefs() {
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (type.equals(Type.Main)) {
					try {
						admin.addTask(on ? admin.getSystemTime() + "\tOFF" : admin.getSystemTime() + "\tON");
					} catch (Exception exception) {
						printer.printInvalidCommandErrorMessage(exception.getMessage());
					}
					
					reset();
				} else if (type.equals(Type.Printer)) {
					admin.setIsPrinterOn(!admin.getIsPrinterOn());
				}
			}
			
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		FontMetrics fontMetrics = getFontMetrics(getFont());
		String text = getText();
		int width = getWidth();
		int height = getHeight();
		
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setBackground(getBackground());
		g2.clearRect(0, 0, width, height);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		g2.setPaint(new GradientPaint(0, 0, Color.WHITE, 0, height, isEnabled() ? on ? Main.GREEN : Main.RED : Main.BLACK));
		g2.fillRect(0, 0, width, height);
		g2.setPaint(null);
		
		int xText = (width - fontMetrics.stringWidth(text)) / 2;
		int yText = (height / 2) + (fontMetrics.getHeight() / 4) + 1;
		
		g2.setColor(Color.BLACK);
		g2.drawString(text, xText, yText);
		g2.dispose();
	}

	@Override
	public void update() {
		if (type.equals(Type.Printer)) {
			on = admin.getIsPrinterOn();
		} else if (type.equals(Type.Main)) {
			if (!admin.isOn() && on) {
				reset();
			}
		}
	}
	
	private void reset() {
		printer.clearScreen();
		console.resetTask();
		main.toggleEnabled(!on);
		on = !on;
	}

	@Override
	public void toggleEnabled(boolean enabled) {
		setEnabled(type.equals(Type.Main) ? true : enabled);
	}

}
