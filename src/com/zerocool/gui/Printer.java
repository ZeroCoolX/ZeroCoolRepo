package com.zerocool.gui;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;


public class Printer extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public Printer() {
		setBorder(null);
		setLayout(new MigLayout("insets 0", "[center]", "[] 15 []"));
		createContents();
	}
	
	private void createContents() {		
		powerButton = new JButton("Printer Pwr");
		add(powerButton, "cell 0 0");
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
	//	textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		textArea.setBackground(Color.WHITE);
		textArea.setForeground(Color.DARK_GRAY);
		textArea.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(15, 15, 15, 15)));
		
		scrollPane = new LightScrollPane(textArea, 150, 150);
		
		add(scrollPane, "cell 0 1");
	}
	
	private JButton powerButton;
	private LightScrollPane scrollPane;
	private JTextArea textArea;
}