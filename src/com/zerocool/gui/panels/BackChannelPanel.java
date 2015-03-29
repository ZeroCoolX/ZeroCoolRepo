package com.zerocool.gui.panels;
import java.awt.Color;

import javax.swing.JPanel;

import com.zerocool.gui.ChannelGroup;

import net.miginfocom.swing.MigLayout;


public class BackChannelPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ChannelGroup channels;
	
	public BackChannelPanel(ChannelGroup channels, Color background) {
		this.channels = channels;
		setBorder(null);
		setLayout(new MigLayout("", "[center] [center] [center] [center]", "[] [] [] []"));
		setBackground(background);
		createContents();
	}
	
	private void createContents() {
		for (int i = 0; i < channels.getChannelCount(); ++i) {
			channels.getChannelConnectButton(i + 1).setBackground(getBackground());
			add(channels.getChannelLabel(i + 1), "cell " + i % 4 + " " + (i < 4 ? 0 : 2));
			add(channels.getChannelConnectButton(i + 1), "cell " + i % 4 + " " + (i < 4 ? 1 : 3));
		}
	}
	
}
