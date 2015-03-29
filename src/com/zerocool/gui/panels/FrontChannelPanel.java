package com.zerocool.gui.panels;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;

import net.miginfocom.swing.MigLayout;

public class FrontChannelPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	
	private Font labelFont;
		
	public FrontChannelPanel(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		setBorder(null);
		setLayout(new MigLayout("", "[right] 0px:15px [center] 0px:15px [center] 0px:15px [center] 0px:15px [center]", 
				"[] 0px:5px [] 0px:5px [] 0px:15px [] 0px:5px [] 0px:5px []"));
		createContents();
	}
	
	@Override
	protected void createContents() {
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
		add(channels.getChannelLabel(channel, true), "cell " + col + " " + row++);
		add(channels.getChannel(channel), "cell " + col + " " + row++);
		add(channels.getChannelEnableButton(channel), "cell " + col + " " + row);
	}
	
	@Override
	public void update() {
		channels.update();
	}
	
	@Override
	public void toggleEnabled(boolean enabled) {
		channels.toggleEnabled(enabled);
	}
	
	private JLabel start;
	private JLabel enable;
	private JLabel finish;
	private JLabel disable;
}
