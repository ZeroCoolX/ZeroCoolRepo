package com.zerocool.gui;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.zerocool.controllers.SystemController;

import net.miginfocom.swing.MigLayout;


public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 900;
	private static final int HEIGHT = 600;
	
	private final String title = "ChronoTimer 1009";
	private final String version = "v0.00";

	protected static SystemController admin;
	
	private boolean powerButtonPressed;
	
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		setTitle(title + " " + version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		createContents();
		pack();
		setVisible(true);
	}
	
	private void createContents() {
		admin = new SystemController();
		printerPanel = new Printer(admin);
		consolePanel = new Console(admin, printerPanel);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		// A while ago I found the best layout ever and it's extremely easy to use and get complicated
		//  layouts rather easily.  It's called 'MigLayout' and you can read more about it at their
		//  website (http://www.miglayout.com/).
		contentPane.setLayout(new MigLayout("", "[center]", "[] 15 []"));
		setContentPane(contentPane);
		
		// This JPanel represents the top view of the ChronoTimer 1009 GUI.  It will have 3 sub JPanels
		//	that contain all of the other components (Left, Center, Right).  I split it up in columns to
		//	make it easier.  Of those 3 sub panels, 2 of them (Center, Right) will have 2 subsub panels each.
		//	It just made sense to do this otherwise those panels would get kinda messy, and we ain't about dat
		//	life.
		topView = new JPanel();
		topView.setBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Top View", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		topView.setLayout(new MigLayout("gapx 0", "50 [] [] 50 [] 50", "[]"));
		
		// This panel is going to hold the Power, Function, Swap and Arrow buttons.
		leftPanel = new JPanel();
		leftPanel.setBorder(null);
		leftPanel.setLayout(new MigLayout("", "[left]", "10 [] 182 [] 15 [] 50 [] 81"));
		
		powerButton = new JButton("Power");
		powerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String time = admin.getSystemTime().toString();
				admin.executeCommand(!powerButtonPressed ? time + "\tON" : time + "\tOFF", false);
				powerButtonPressed = !powerButtonPressed;
			}
			
		});
		leftPanel.add(powerButton, "cell 0 0");
		
		functionButton = new JButton("Function");
		leftPanel.add(functionButton, "cell 0 1");
		
		arrowPanel = new Arrows(consolePanel);
		
		// Done with arrowPanel so add it to the Left Panel.
		leftPanel.add(arrowPanel, "cell 0 2");
		
		swapButton = new JButton("Swap");
		leftPanel.add(swapButton, "cell 0 3");
		
		// Done with leftPanel so add it to the Top View.
		topView.add(leftPanel, "cell 0 0");
		
		// This panel is going to be holding 2 sub panels.  The first contains all the channels and the
		//  second will contain the console.
		centerPanel = new JPanel();
		centerPanel.setBorder(null);
		centerPanel.setLayout(new MigLayout("", "[center]", "[] []"));
		
		// Holds all the channels and junk.
		channelPanel = new JPanel();
		channelPanel.setBorder(null);
		channelPanel.setLayout(new MigLayout("", "[center]", "[] 5 [] []"));
		
		titleLabel = new JLabel(title + "  ");
		titleLabel.setFont(new Font("Tahoma", Font.ITALIC, 15));
		channelPanel.add(titleLabel, "cell 0 0");
		
		startGroup = new ChannelGroup(true);
		channelPanel.add(startGroup, "cell 0 1");
		
		finishGroup = new ChannelGroup(false);
		channelPanel.add(finishGroup, "cell 0 2");
		
		// Done with the channelPanel so add it to the Center Panel.
		centerPanel.add(channelPanel, "cell 0 0");
		
		// TODO MOVED TO TOP
		
		// Done with the console so add it to the Center Panel.
		centerPanel.add(consolePanel, "cell 0 1, right");		
		// Done with centerPanel so add it to the Top View.
		topView.add(centerPanel, "cell 1 0");
		
		rightPanel = new JPanel();
		rightPanel.setBorder(null);
		rightPanel.setLayout(new MigLayout("gapy 0", "[center]", "[] 17 [] 25"));
		
		// TODO MOVED TO TOP
		
		// Done with the printerPanel so add it to the Right Panel.
		rightPanel.add(printerPanel, "cell 0 0");
		
		keyPanel = new KeyPad();
		
		// Done with the keyPanel so add it to the Right Panel;
		rightPanel.add(keyPanel, "cell 0 1");
		// Done with rightPanel so add it to the Top View.
		topView.add(rightPanel, "cell 2 0");
		// Done with topView so add it to the Content Pane.
		contentPane.add(topView, "cell 0 0");
		
		backView = new JPanel();
		backView.setBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Back View", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		backView.setLayout(new MigLayout("", "[] 15 [] 120 [] 388", "[] 25"));
		
		backChannel = new JLabel("CHAN");
		backView.add(backChannel, "cell 0 0, top");
		
		backCluster = new ChannelCluster();
		backView.add(backCluster, "cell 1 0");
		
		portPanel = new USBPort();
		backView.add(portPanel, "cell 2 0");
		
		contentPane.add(backView, "cell 0 1");
	}
	
	private JPanel contentPane;
		private JPanel topView;
			private JPanel leftPanel;
				private JButton powerButton;
				private JButton functionButton;
				private Arrows arrowPanel;
				private JButton swapButton;
			private JPanel centerPanel;
				private JPanel channelPanel;
					private JLabel titleLabel;
					private ChannelGroup startGroup;
					private ChannelGroup finishGroup;
				private Console consolePanel;
			private JPanel rightPanel;
				private Printer printerPanel;
				private KeyPad keyPanel;
		private JPanel backView;
			private JLabel backChannel;
			private ChannelCluster backCluster;
			private USBPort portPanel;
}
