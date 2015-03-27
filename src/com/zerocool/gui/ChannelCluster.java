package com.zerocool.gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.zerocool.controllers.SystemController;

import net.miginfocom.swing.MigLayout;


public class ChannelCluster extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel[] labels;
	private JRadioButton[] radios;
	private SystemController privateAdmin;
	private final Color dark_grey = new Color(105,105,105);
	
	public ChannelCluster(SystemController privateAdmin) {
		this.privateAdmin = privateAdmin;
		setBorder(null);
		setLayout(new MigLayout("", "[center] [center] [center] [center]", "[] [] [] []"));
		setBackground(dark_grey);
		createContents();
	}
	
	private void createContents() {
		labels = new JLabel[8];
		radios = new JRadioButton[8];
		
		for (int i = 0; i < labels.length; ++i) {
			labels[i] = new JLabel("" + (i + 1));
			labels[i].setForeground(Color.WHITE);
			add(labels[i], "cell " + i % 4 + " " + (i < 4 ? 0 : 2));
			
			radios[i] = new JRadioButton();
			radios[i].setActionCommand(""+(i+1));
			radios[i].addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected()){
						System.out.println("toggle channel " + e.getActionCommand());
						privateAdmin.executeCommand(privateAdmin.getSystemTime() + "\tCONN GATE "+e.getActionCommand(), false);
					}else{
						System.out.println("toggle channel " + e.getActionCommand());
						privateAdmin.executeCommand(privateAdmin.getSystemTime() + "\tDISC "+e.getActionCommand(), false);
					}
				}
			});
			add(radios[i], "cell " + i % 4 + " " + (i < 4 ? 1 : 3));
		}
	}
	
	
	
}
