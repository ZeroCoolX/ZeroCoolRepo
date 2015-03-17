package com.zerocool.gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;


public class Console extends JPanel {

	private static final long serialVersionUID = 1L;

	BufferedImage img;
	
	public Console() {
//		try {
//			img = ImageIO.read(new File("america.jpg"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		setBorder(null);
		setLayout(new MigLayout("", "[center]", "[] []"));
		createContents();
	}
	
	private void createContents() {		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
	//	textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setForeground(Color.DARK_GRAY);
		textArea.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(15, 15, 15, 15)));
		
		scrollPane = new LightScrollPane(textArea, 200, 200);
		
		add(scrollPane, "cell 0 0");
		
		description = new JLabel("Queue / Running / Final Time");
		description.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		add(description, "cell 0 1");
	}
	
//	@Override
//	public void paintComponent(final Graphics g) {
//		super.paintComponent(g);
//		
//		g.drawImage(img, 0, 0, null);
//	}
	
	private LightScrollPane scrollPane;
	private JTextArea textArea;
	private JLabel description;
	
}
