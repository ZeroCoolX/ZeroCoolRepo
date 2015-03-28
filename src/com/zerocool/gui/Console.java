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
	private Color dark_slate_green = new Color(47,79,79);
	private final Color gainsboro = new Color(220,220,220);
	private static boolean scanPrompting = false;
	private int scanNumToConnect = 0;
	
	BufferedImage img;

	public Console(SystemController systemController, Printer printer) {
		//		try {
		//			img = ImageIO.read(new File("america.jpg"));     //<------- america? hahahah MURRICA! 
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		admin = systemController;
		this.printer = printer;
		
		setBorder(null);
		setLayout(new MigLayout("", "[center]", "[] []"));
		setBackground(gainsboro);
		createContents();
	}

	private void createContents() {
		view = new ConsoleView(admin);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		//	textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		textArea.setBackground(dark_slate_green);
		textArea.setForeground(Color.WHITE);
		textArea.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(15, 15, 15, 15)));
		// TEMPORARY! =OOOOOOOOOOO
		textArea.addFocusListener(new FocusListener() {
			
				@Override
				public void focusGained(FocusEvent e) {
					if(!scanPrompting){
						textArea.setText("");
					}
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
					String text = (admin.getSystemTime().toString() + "\t" + textArea.getText().trim().toUpperCase());
					
					if(!scanPrompting){
						admin.executeCommand(text, false);
						textArea.setText("");
					}else{
						System.out.println("inside console");
						String []tempText = textArea.getText().trim().toUpperCase().split("[>]");
						text = (admin.getSystemTime().toString() + "\tCONN" + tempText[1] +" "+ scanNumToConnect);
						System.out.println(text);
						admin.executeCommand(text, false);
						scanPrompting = false;
						textArea.setText(">"+view.currentCommand());
					}
					if (admin.getIsPrinterOn()) {//meaning the printer is turned on
						printer.addText(text);
					}
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
	
	public void toggleScreen(boolean power){
		textArea.setText((power ? "type command then enter" : ""));
	}

	public void prevCommand() {
		view.prevCommand();
		textArea.setText(view.getView(false));
	}

	public void nextCommand() {
		view.nextCommand();
		textArea.setText(view.getView(false));
	}
	
	public String currentCommand() {
		return view.currentCommand();
	}
	
	public void setCommandArgComboView(){
		System.out.println("setting text view as: " + view.getView(true));
		textArea.setText(view.getView(true));
	}

	public void moveUp() {
		view.moveUp();
		textArea.setText(view.getView(false));
	}

	public void moveDown() {
		view.moveDown();
		textArea.setText(view.getView(false));
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
	
	public void setNewText(String text){
		textArea.setText(text);
	}
	
	public boolean isScanPrompting(){
		return scanPrompting;
	}
	
	public void promptScanner(int num){
		scanNumToConnect = num;
		scanPrompting = true;
		textArea.setText("Select scanner type:" +
				"\nGATE" +
				"\nEYE" +
				"\nPAD" +
				"\n> ");
	}
	
	public void toggleEnabled(boolean powerOn){
		textArea.setEnabled(powerOn);
	}
	
	public ConsoleView getConsoleView(){
		return view;
	}

}
