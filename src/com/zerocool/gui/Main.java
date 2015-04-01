package com.zerocool.gui;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.views.BotView;
import com.zerocool.gui.views.TopView;

import net.miginfocom.swing.MigLayout;


public class Main extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "ChronoTimer 1009";
	public static final String VERSION = "vSprint 2";
		
	public static final Color BLACK = new Color(13, 13, 13);
	public static final Color BLEACHED_ALMOND = new Color(255, 235, 205);
	public static final Color DARK_SLATE_GREEN = new Color(47, 79, 79);
	public static final Color DEEP_SKY_BLUE = new Color(0, 191, 255);
	public static final Color DARK_ORANGE = new Color(255, 140, 0);
	public static final Color GAINSBORO = new Color(220, 220, 220);
	public static final Color GREEN = new Color(34, 178, 34);
	public static final Color GREY = new Color(178, 178, 178);
	public static final Color RED = new Color(178, 34, 34);

	private static final int WIDTH = 900;
	private static final int HEIGHT = 600;
	
	private SystemController admin;
	private Console console;
	private Printer printer;
	private ChannelGroup channels;
	
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		setTitle(TITLE + " " + VERSION);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, WIDTH, HEIGHT);
		createContents();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		admin.addObserver(this);
	}
	
	private void createContents() {
		admin = new SystemController();
		printer = new Printer(this, admin, console, null, null, GAINSBORO);
		admin.setPrinter(printer);
		console = new Console(this, admin, null, printer, null, GAINSBORO);
		channels = new ChannelGroup(this, admin, console, printer);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		// A while ago I found the best layout ever and it's extremely easy to use and get complicated
		//  layouts rather easily.  It's called 'MigLayout' and you can read more about it at their
		//  website (http://www.miglayout.com/).
		contentPane.setLayout(new MigLayout("fill", "[center]", "[] 0px:15px []"));
		setContentPane(contentPane);
		contentPane.setBackground(GAINSBORO);
		// This JPanel represents the top view of the ChronoTimer 1009 GUI.  It will have 3 sub JPanels
		//	that contain all of the other components (Left, Center, Right).  I split it up in columns to
		//	make it easier.  Of those 3 sub panels, 2 of them (Center, Right) will have 2 subsub panels each.
		//	It just made sense to do this otherwise those panels would get kinda messy, and we ain't about dat
		//	life.
		topView = new TopView(this, admin, console, printer, channels, GAINSBORO);
		contentPane.add(topView, BorderLayout.NORTH);
		
		botView = new BotView(this, admin, console, printer, channels, GREY);
		contentPane.add(botView, BorderLayout.SOUTH);
		
		toggleEnabled(false);
	}
	
	@Override
	public void update() {
		topView.update();
		botView.update();
	}
	
	public void toggleEnabled(boolean enabled) {
		topView.toggleEnabled(enabled);
		botView.toggleEnabled(enabled);
	}
	
	private JPanel contentPane;
		private TopView topView;
		private BotView botView;
}
