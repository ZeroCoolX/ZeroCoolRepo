package com.zerocool.gui;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.buttons.PowerButton;

import net.miginfocom.swing.MigLayout;


public class Printer extends JPanel {

	private static final long serialVersionUID = 1L;
		
	public Printer(Main main, SystemController admin, Console console, Color background) {
		powerButton = new PowerButton(main, admin, console, this, "Printer Pwr", PowerButton.Type.Printer);
		setBorder(null);
		setLayout(new MigLayout("insets 0px", "[center]", "[] 0px:15px []"));
		setBackground(background);
		createContents();
	}
	
	private void createContents() {
		add(powerButton, "cell 0 0");
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		textArea.setBackground(Main.BLEACHED_ALMOND);
		textArea.setForeground(Color.DARK_GRAY);
		textArea.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(15, 15, 15, 15)));
		
		scrollPane = new LightScrollPane(textArea, 150, 150);
		
		add(scrollPane, "cell 0 1");
	}
	
	public void addText(String text) {
		textArea.setText(textArea.getText() + "\n" + text);
	}
	
	public void toggleEnabled(boolean powerOn){
		textArea.setEnabled(powerOn);
		powerButton.setEnabled(powerOn);
	}
	
	public void clearScreen(){
		textArea.setText("");
	}
	
	private JButton powerButton;
	private LightScrollPane scrollPane;
	private JTextArea textArea;
}
