package com.zerocool.gui.buttons;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;

public class ChannelButton extends AbstractButton {

	private static final long serialVersionUID = 1L;
	
	private ToggleButton connectButton;
	private ToggleButton enableButton;
	private JLabel frontLabel;
	private JLabel backLabel;
	private Dimension size;
	
	private int id;
	
	public ChannelButton(Main main, SystemController admin, Console console, Printer printer, String text, int id) {
		super(main, admin, console, printer, text);
		this.id = id;
		connectButton = new ToggleButton(main, admin, console, printer, ToggleButton.Type.CONNECT, id);
		enableButton = new ToggleButton(main, admin, console, printer, ToggleButton.Type.ENABLE, id);
		frontLabel = createLabel();
		backLabel = createLabel();
		size = new Dimension(20, 20);
		setPrefs();
	}
	
	@Override
	protected void setPrefs() {
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (connectButton.isOn() && enableButton.isOn()) {
					System.out.println(id);
					if (admin.getTimer().getCurrentEvent().getRunningQueue().isEmpty() && id % 2 == 0) {
						console.setNewText("Participant competing queue empty...");
					} else if(admin.getTimer().getCurrentEvent().getStartingQueue().isEmpty() && id % 2 != 0) {
						console.setNewText("Participant starting queue empty...");
					} else {
						System.out.println("trigging");
						String totalCommand = admin.getSystemTime() + "\tTRIG " + id;
						printer.addText(totalCommand);
						admin.executeCommand(totalCommand, false);
					}
				}
			}
			
		});
		
	}
	
	@Override
	public void update() {
		connectButton.update();
		enableButton.update();
	}

	@Override
	public void toggleEnabled(boolean enabled) {
		setEnabled(enabled);
		connectButton.toggleEnabled(enabled);
		enableButton.toggleEnabled(enabled);
	}
	
	private JLabel createLabel() {
		JLabel l = new JLabel("" + id);
		l.setHorizontalAlignment(JLabel.CENTER);
		l.setFont(new Font("Tahoma", Font.PLAIN, 14));
		return l;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setBackground(getBackground());
		g2.clearRect(0, 0, width, height);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		g2.setPaint(isEnabled() ? connectButton.isOn() && enableButton.isOn() ? Main.GREEN : connectButton.isOn() || 
				enableButton.isOn() ? Main.DARK_ORANGE : Main.DEEP_SKY_BLUE : Main.BLACK);
		g2.fillRect(0, 0, width, height);
		
		g2.dispose();
	}
	
	public ToggleButton getConnectButton() {
		return connectButton;
	}
	
	public ToggleButton getEnableButton() {
		return enableButton;
	}
	
	public JLabel getJLabel(boolean front) {
		return front ? frontLabel : backLabel;
	}

}
