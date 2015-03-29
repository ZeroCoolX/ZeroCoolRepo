package com.zerocool.gui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.ChannelGroup;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;
import com.zerocool.gui.buttons.PowerButton;

import net.miginfocom.swing.MigLayout;

public class LeftPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	
	public LeftPanel(Main main, SystemController admin, Console console, Printer printer, ChannelGroup channels, Color background) {
		super(main, admin, console, printer, channels, background);
		setBorder(null);
		setLayout(new MigLayout("fill", "[center]", "0px:10px [] 0px:182px [] 0px:15px [] 0px:50px [] 0px:81px"));
		createContents();
	}
	
	@Override
	protected void createContents() {
		powerButton = new PowerButton(main, admin, console, printer, "Power", PowerButton.Type.Main);
		add(powerButton, "cell 0 0");
		
		functionButton = new JButton("Function");
		functionButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!console.isScanPrompting()){
					System.out.println(console.currentCommand());
					String totalLine = (admin.getSystemTime() + "\t" + console.currentCommand() + " " + console.getConsoleView().getArgs());
					if (admin.getIsPrinterOn()) {//meaning the printer is turned on
						printer.addText(totalLine);
					}
					admin.executeCommand(totalLine, false);//STILL NEED TO GET THE NUMBER CONCATENATED ONTO THE COMMMAND!!!!!
					console.setNewText(">" + console.currentCommand());
				}
			}
			
		});
		add(functionButton, "cell 0 1");
		
		arrows = new ArrowPanel(main, admin, console, printer, channels, getBackground());
		add(arrows, "cell 0 2");
		
		swapButton = new JButton("Swap");
		add(swapButton, "cell 0 3");
	}

	@Override
	public void update() {
		powerButton.update();
		arrows.update();
	}
	
	@Override
	public void toggleEnabled(boolean enabled) {
		powerButton.setEnabled(enabled);
		arrows.setEnabled(enabled);
	}
	
	private PowerButton powerButton;
	private JButton functionButton;
	private ArrowPanel arrows;
	private JButton swapButton;
}
