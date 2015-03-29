package com.zerocool.gui.buttons;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;

public class ChannelButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	private SystemController admin;
	private Console console;
	private ToggleButton connectButton;
	private ToggleButton enableButton;
	private JLabel frontLabel;
	private JLabel backLabel;
	private Dimension size;
	
	private int id;
	
	public ChannelButton(SystemController admin, Console console, int id) {
		super("");
		this.admin = admin;
		this.console = console;
		this.id = id;
		connectButton = new ToggleButton(admin, console, ToggleButton.Type.CONNECT, id);
		enableButton = new ToggleButton(admin, console, ToggleButton.Type.ENABLE, id);
		frontLabel = createLabel();
		backLabel = createLabel();
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
				if (connectButton.isEnabled() && enableButton.isEnabled()) {
					if (admin.getTimer().getCurrentEvent().getCompetingParticipants().isEmpty() && id % 2 == 0) {
						console.setNewText("Participant competing queue empty...");
					} else {
						admin.executeCommand(admin.getSystemTime() + (id % 2 == 0 ? "\tFIN " + admin.getTimer().getCurrentEvent().getCompetingParticipants().peek().getId() : "\tSTART"), false);
					}
				}
			}
			
		});
		
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
		g2.setPaint(connectButton.isEnabled() ? Main.DARK_ORANGE : Main.DEEP_SKY_BLUE);
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
