package com.zerocool.gui.buttons;

import java.awt.AlphaComposite;
import java.awt.Color;
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

public class ChannelButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	private final Color deep_sky_blue = new Color(0,191,255);
	private final Color dark_orange = new Color(255,140,0);
	
	private SystemController admin;
	private Console console;
	private ToggleButton connectButton;
	private ToggleButton enableButton;
	private Dimension size;
	
	private int id;
	
	public ChannelButton(SystemController admin, Console console, int id) {
		super("");
		this.admin = admin;
		this.console = console;
		this.id = id;
		connectButton = new ToggleButton(admin, console, ToggleButton.Type.CONNECT, id);
		enableButton = new ToggleButton(admin, console, ToggleButton.Type.ENABLE, id);
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
	
	@Override
	protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setBackground(getBackground());
		g2.clearRect(0, 0, width, height);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		g2.setPaint(connectButton.isEnabled() ? dark_orange : deep_sky_blue);
		g2.fillRect(0, 0, width, height);
		
		g2.dispose();
	}
	
	public ToggleButton getConnectButton() {
		return connectButton;
	}
	
	public ToggleButton getEnableButton() {
		return enableButton;
	}
	
	public JLabel getJLabel() {
		JLabel label = new JLabel("" + id);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		return label;
	}

}
