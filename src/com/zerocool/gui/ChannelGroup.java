package com.zerocool.gui;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.zerocool.controllers.SystemController;

import net.miginfocom.swing.MigLayout;


public class ChannelGroup extends JPanel {

	private static final long serialVersionUID = 1L;

	private Font labelFont;
	private SystemController privateAdmin;
	
	private String[] arguments;
	
	public ChannelGroup(boolean start, SystemController privateAdmin) {
		this.privateAdmin = privateAdmin;
		arguments = start ? new String[] { "Start", "1", "3", "5", "7" } : new String[] { "Finish", "2", "4", "6", "8" };
		setBorder(null);
		setLayout(new MigLayout("", "[right] 15 [center] 15 [center] 15 [center] 15 [center]", "[] 5 [] 5 []"));
		createContents();
	}
	
	private void createContents() {
		labelFont = new Font("Tahoma", Font.PLAIN, 14);
		
		
		start = new JLabel(arguments[0]);
		start.setFont(labelFont);
		add(start, "cell 0 1");
		
		channelNum1 = new JLabel(arguments[1]);
		channelNum1.setFont(labelFont);
		add(channelNum1, "cell 1 0");
		
		channelNum2 = new JLabel(arguments[2]);
		channelNum2.setFont(labelFont);
		add(channelNum2, "cell 2 0");

		channelNum3 = new JLabel(arguments[3]);
		channelNum3.setFont(labelFont);
		add(channelNum3, "cell 3 0");
		
		channelNum4 = new JLabel(arguments[4]);
		channelNum4.setFont(labelFont);
		add(channelNum4, "cell 4 0");
		
		
		ArrayList<JRadioButton> enables = new ArrayList<JRadioButton>();

		enable1 = new JRadioButton("");
		enable1.setActionCommand(arguments[1]);
		enables.add(enable1);
		
		enable2 = new JRadioButton("");
		enable2.setActionCommand(arguments[2]);
		enables.add(enable2);
		
		enable3 = new JRadioButton("");
		enable3.setActionCommand(arguments[3]);
		enables.add(enable3);
		
		enable4 = new JRadioButton("");
		enable4.setActionCommand(arguments[4]);
		enables.add(enable4);
		
		
		
		channel1 = new ChannelButton();
		add(channel1, "cell 1 1");
		
		channel2 = new ChannelButton();
		add(channel2, "cell 2 1");
		
		channel3 = new ChannelButton();
		add(channel3, "cell 3 1");
		
		channel4 = new ChannelButton();
		add(channel4, "cell 4 1");
		
		enable = new JLabel("Enable/Disable");
		enable.setFont(labelFont);
		add(enable, "cell 0 2");
		
		

		add(enable1, "cell 1 2");
		
		add(enable2, "cell 2 2");
		
		add(enable3, "cell 3 2");
		
		add(enable4, "cell 4 2");
		
		for(JRadioButton jrad: enables){
			jrad.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					privateAdmin.executeCommand(privateAdmin.getSystemTime() + "\tTOGGLE "+e.getActionCommand(), false);
				}
				
			});
		}
	}
	
	private JLabel start;
	private JLabel channelNum1;
	private JLabel channelNum2;
	private JLabel channelNum3;
	private JLabel channelNum4;
	private ChannelButton channel1;
	private ChannelButton channel2;
	private ChannelButton channel3;
	private ChannelButton channel4;
	private JLabel enable;
	private JRadioButton enable1;
	private JRadioButton enable2;
	private JRadioButton enable3;
	private JRadioButton enable4;
}
