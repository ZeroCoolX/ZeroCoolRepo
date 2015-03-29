package com.zerocool.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class PrinterView extends JTextArea {

	private static final long serialVersionUID = 1L;

	public PrinterView() {
		super();
		
		setPrefs();
	}
	
	private void setPrefs() {
		setLineWrap(true);
		setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		setBackground(Main.BLEACHED_ALMOND);
		setForeground(Color.DARK_GRAY);
		setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(15, 15, 15, 15)));
	}
	
	public void addText(String text) {
		setText(getText() + "\n" + text);
	}
	
	public void update() {
		// TODO things
	}
	
	public void toggleEnabled(boolean enabled) {
		setEnabled(enabled);
		setBackground(enabled ? Main.BLEACHED_ALMOND : Main.BLACK);
	}
	
}
