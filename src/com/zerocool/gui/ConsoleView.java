package com.zerocool.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.zerocool.controllers.SystemController;

public class ConsoleView extends JTextArea {

	private static final long serialVersionUID = 1L;

	private String[] commandList;
	private String[] argumentList;
	private String startingQueue;
	private String runningQueue;
	private String finishedQueue;
	private SystemController admin;
	
	private int commandIndex;
	private int argumentIndex;

	public ConsoleView(SystemController admin) {
		this.admin = admin;
		argumentList = new String[2];
		commandList = admin.getCommandList();
		commandIndex = -1;
		argumentIndex = -1;
		setPrefs();
	}

	private void setPrefs() {
		setLineWrap(true);
		setEditable(false);
		setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		setBackground(Main.DARK_SLATE_GREEN);
		setForeground(Color.WHITE);
		setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(15, 15, 15, 15)));
	}

	public void update() {
		// TODO things
		if (isEnabled()) {
			startingQueue = admin.getStartingQueue();
			runningQueue = admin.getRunningQueue();
			finishedQueue = admin.getFinishedQueue();
			setText(getView());
		}
	}

	public void toggleEnabled(boolean enabled) {
		setEnabled(enabled);
		setBackground(enabled ? Main.DARK_SLATE_GREEN : Main.BLACK);
	}

	public void addArgument(String arg) {
		argumentList[1] += arg;
	}
	
	public void prevCommand() {
		clearArgs();
		commandIndex = moveIndex(commandIndex - 1, commandList.length);
	}

	public void nextCommand() {
		clearArgs();
		commandIndex = moveIndex(commandIndex + 1, commandList.length);
	}

	public void moveUp() {
		String[] args = admin.getCommadArgs(getCurrentCommand());
		if (args != null) {
			argumentIndex = moveIndex(argumentIndex - 1, args.length);
			argumentList[0] = argumentIndex != -1 ? args[argumentIndex] : "";
		}
	}

	public void moveDown() {
		String[] args = admin.getCommadArgs(getCurrentCommand());
		if (args != null) {
			argumentIndex = moveIndex(argumentIndex + 1, args.length);
			argumentList[0] = argumentIndex != -1 ? args[argumentIndex] : "";
		}
	}
	
	public void resetTask() {
		commandIndex = -1;
		argumentIndex = -1;
		clearArgs();
	}
	
	private int moveIndex(int next, int length) {
		return next < -1 ? length - 1 : next > length - 1 ? -1 : next;
	}

	public String[] getArgs() {
		return argumentList;
	}
	
	public String getTime() {
		return admin.getSystemTime().toString();
	}
	
	public String getCurrentCommand() {
		return commandIndex != - 1 ? commandList[commandIndex] : "";
	}

	public String getCurrentTask() {
		return getCurrentCommand() + " " + Arrays.toString(argumentList).replaceAll("[\\[\\]null,]", "").trim();
	}
	
	public String getView() {
		String text = getTime() + " > " + getCurrentTask() + "\n\n";

		text += startingQueue + "\n";
		text += runningQueue + "\n";
		text += finishedQueue + "\n";
		
		return text;
	}
	
	private void clearArgs() {
		argumentList[0] = null;
		argumentList[1] = null;
	}

}
