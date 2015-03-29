package com.zerocool.gui.panels;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.zerocool.gui.ChannelGroup;

import net.miginfocom.swing.MigLayout;

public class FrontChannelPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Font labelFont;
	private ChannelGroup channels;
		
	public FrontChannelPanel(ChannelGroup channels, Color background) {
		this.channels = channels;
		setBorder(null);
		setLayout(new MigLayout("", "[right] 0px:15px [center] 0px:15px [center] 0px:15px [center] 0px:15px [center]", "[] 0px:5px [] 0px:5px ["
				+ "] 0px:15px [] 0px:5px [] 0px:5px []"));
		setBackground(background);
		createContents();
	}
	
	private void createContents() {
		labelFont = new Font("Tahoma", Font.PLAIN, 14);
		
		start = new JLabel("Start");
		start.setFont(labelFont);
		add(start, "cell 0 1");
		
		enable = new JLabel("Enable/Disable");
		enable.setFont(labelFont);
		add(enable, "cell 0 2");
		
		finish = new JLabel("Finish");
		finish.setFont(labelFont);
		add(finish, "cell 0 4");
		
		disable = new JLabel("Enable/Disable");
		disable.setFont(labelFont);
		add(disable, "cell 0 5");
		
		for (int i = 0; i < channels.getChannelCount(); ++i) {
			addChannel(i + 1);
		}
		
	}
	
	private void addChannel(int channel) {
		int col = channel % 2 == 0 ? (channel / 2) : Math.floorDiv(channel, 2) + 1;
		int row = channel % 2 == 0 ? 3 : 0;
		channels.getChannelEnableButton(channel).setBackground(getBackground());
		System.out.println("cell " + col + " " + row);
		add(channels.getChannelLabel(channel), "cell " + col + " " + row++);
		System.out.println("cell " + col + " " + row);
		add(channels.getChannel(channel), "cell " + col + " " + row++);
		System.out.println("cell " + col + " " + row);
		add(channels.getChannelEnableButton(channel), "cell " + col + " " + row);
	}
	
	private JLabel start;
	private JLabel enable;
	private JLabel finish;
	private JLabel disable;
}
