package com.zerocool.gui.buttons;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.zerocool.controllers.SystemController;
import com.zerocool.gui.Console;
import com.zerocool.gui.Main;
import com.zerocool.gui.Printer;

public class ToggleButton extends AbstractButton {

	private static final long serialVersionUID = 1L;

	public enum Type { CONNECT, ENABLE };
	
	private Dimension size;
	
	private Type type;
	private int id;
	private boolean on;
	private boolean previousState;
	
	public ToggleButton(Main main, SystemController admin, Console console, Printer printer, Type type, int id) {
		super(main, admin, console, printer, "");
		this.type = type;
		this.id = id;
		size = new Dimension(20, 20);
		setPrefs();
	}
	
	@Override
	protected void setPrefs() {
		setBorder(null);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String totalCommand = "";
				if (type.equals(Type.CONNECT)) {
					totalCommand = admin.getSystemTime() + (on ? " DISC " : " CONN GATE ") + id;
				} else if (type.equals(Type.ENABLE)) {
					totalCommand = admin.getSystemTime() + " TOGGLE " + id;
				}
				
				try {
					admin.addTask(totalCommand);
				} catch (IllegalArgumentException ex) {
					ex.printStackTrace();
					// TODO
					//console.printErrorMessage(ex.getMessage());
				}
				
				main.repaint();
			}
			
		});
	}
	
	@Override
	public void update() {
		on = type.equals(Type.CONNECT) ? admin.isSensorActive(id) : admin.isChannelActive(id);
		if (previousState != on) {
			previousState = on;
			main.repaint();
		}
	}

	@Override
	public void toggleEnabled(boolean enabled) {
		setEnabled(enabled);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		int ovalX =  (width / 4);
		int ovalY = (height / 4);
		int ovalWidth = (width / 2);
		int ovalHeight = (height / 2);
		
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setBackground(getBackground());
		g2.clearRect(0, 0, width, height);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		g2.setPaint(isEnabled() ? on ? Main.GREEN : Main.RED : Main.BLACK);
		g2.fillOval(ovalX, ovalY, ovalWidth + 1, ovalHeight + 1);
		
		g2.setPaint(Main.BLACK);
		g2.drawOval(ovalX, ovalY, ovalWidth, ovalHeight);
		
		g2.dispose();
	}
	
	public boolean isOn() {
		return on;
	}
	
}
