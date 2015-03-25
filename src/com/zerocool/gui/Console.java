package com.zerocool.gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.zerocool.controllers.SystemController;

import net.miginfocom.swing.MigLayout;


public class Console extends JPanel {

	private static final long serialVersionUID = 1L;

	private SystemController admin;
	private ConsoleView view;
	private Printer printer;
	
	BufferedImage img;

	public Console(SystemController systemController, Printer printer) {
		//		try {
		//			img = ImageIO.read(new File("america.jpg"));
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		admin = systemController;
		this.printer = printer;
		
		setBorder(null);
		setLayout(new MigLayout("", "[center]", "[] []"));
		createContents();
	}

	private void createContents() {
		view = new ConsoleView(admin);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		//	textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setForeground(Color.DARK_GRAY);
		textArea.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(15, 15, 15, 15)));
		textArea.setText("type command then enter");
		// TEMPORARY! =OOOOOOOOOOO
		textArea.addFocusListener(new FocusListener() {
			
				@Override
				public void focusGained(FocusEvent e) {
					textArea.setText("");
				}

				@Override
				public void focusLost(FocusEvent e) {
					// do nothing
				}
			
		});
		textArea.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent key) {
				if (key.getKeyCode() == KeyEvent.VK_ENTER) {
					String text = textArea.getText().trim();
					
					if (admin.getIsPrinterOn()) {//meaning the printer is turned on
						printer.addText(text);
					}
					
					admin.executeCommand(admin.getSystemTime().toString() + "\t" + text.toUpperCase(), false);
					textArea.setText("");
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// do nothing
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// do nothing
			}
			
		});
		
		scrollPane = new LightScrollPane(textArea, 200, 200);

		add(scrollPane, "cell 0 0");

		description = new JLabel("Queue / Running / Final Time");
		description.setFont(new Font("Tahoma", Font.PLAIN, 10));

		add(description, "cell 0 1");
	}

	public void prevCommand() {
		view.prevCommand();
		textArea.setText(view.getView());
	}

	public void nextCommand() {
		view.nextCommand();
		textArea.setText(view.getView());
	}

	public void moveUp() {
		view.moveUp();
		textArea.setText(view.getView());
	}

	public void moveDown() {
		view.moveDown();
		textArea.setText(view.getView());
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
