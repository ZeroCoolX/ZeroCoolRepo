package com.zerocool.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;


public class ChannelCluster extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel[] labels;
	private JRadioButton[] radios;
	
	public ChannelCluster() {
		setBorder(null);
		setLayout(new MigLayout("", "[center] [center] [center] [center]", "[] [] [] []"));
		
		createContents();
	}
	
	private void createContents() {
		labels = new JLabel[8];
		radios = new JRadioButton[8];
		
		for (int i = 0; i < labels.length; ++i) {
			labels[i] = new JLabel("" + (i + 1));
			add(labels[i], "cell " + i % 4 + " " + (i < 4 ? 0 : 2));
			
			radios[i] = new JRadioButton();
			radios[i].addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					//need to connect the channel...or maybe this doesn't need ot happen here...idk
				}
			});
			add(radios[i], "cell " + i % 4 + " " + (i < 4 ? 1 : 3));
		}
	}
	
	
	
}
