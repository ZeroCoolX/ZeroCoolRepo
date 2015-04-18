package com.zerocool.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.zerocool.controllers.SystemController;
import com.zerocool.services.SystemTime;

public class ConsoleView extends JTextArea {

	private static final long serialVersionUID = 1L;

	private ArrayList<Participant> waiting;
	private ArrayList<Participant> running;
	private ArrayList<Participant> finished;
	private String[] cmds;
	private SystemController admin;

	private int index;
	private int currentLine;
	
	private String commandArgCombo = "";	
	
	private static boolean scanPrompting = false;

	private String args = "";

	public ConsoleView(SystemController admin) {
		waiting = new ArrayList<Participant>();
		running = new ArrayList<Participant>();
		finished = new ArrayList<Participant>();
		this.admin = admin;
		//boolean parameter indicates if the extended command list should be used or not. 
		//The NON extended list has cammands like: EVENT, CONN...etc  The extended list has commands like EVENT IND, EVENT PARIND, EVENT GRP, EVENT PARGRP, CONN GATE, CONN EYE...etc
		cmds = admin.getCommandList(true);
		index = -1;
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
			setText(getView(true));
		}
	}
	
	public String getTime() {
		return admin.getSystemTime().toString();
	}

	public void toggleEnabled(boolean enabled) {
		setEnabled(enabled);
		setBackground(enabled ? Main.DARK_SLATE_GREEN : Main.BLACK);
	}

	public void prevCommand() {
		args = "";
		moveIndex(index - 1);
	}

	public String getArgs() {
		return args;
	}

	public String currentCommand() {
		return getCurrentCommand();
	}

	public void nextCommand() {
		args = "";
		moveIndex(index + 1);
	}

	public void moveUp() {
		moveLine(currentLine - 1);
	}

	public void moveDown() {
		moveLine(currentLine + 1);
	}

	public void addParticipant(String num, String addTime) {
		waiting.add(new Participant(num, addTime));
	}

	public void startParticipant() {

	}

	//ON|OFF|EXIT|RESET|TIME|TOGGLE|CONN GATE|CONN EYE|CONN PAD|DISC|EVENT IND|EVENT GRP|EVENT PARIND|EVENT PARGRP|NEWRUN|ENDRUN|PRINT|EXPORT|
	//NUM|CLR|SWAP|RCL|START|FIN|TRIG|ELAPSED|CANCEL|DNF
	
	private void moveIndex(int next) {
		index = next < -1 ? cmds.length - 1 : next > cmds.length - 1 ? -1 : next;
	}

	private void moveLine(int next) {
		int totalLines = waiting.size() + running.size() + finished.size();
		currentLine = next < 0 ? totalLines : next > totalLines ? 0 : next;
		index = -1;
	}

	private String getCurrentCommand() {
		return index == - 1 ? "" : cmds[index];
	}

	public String getView(boolean useCombo) {
		String text = getTime();

		if (waiting.isEmpty() && running.isEmpty() && finished.isEmpty()) {
			text += " > " +  (useCombo ? getCommandArgCombo() : getCurrentCommand());
		} else {
			int line = 0;
			for (Participant par : waiting) {
				text += par.print();
				if (line == currentLine) {
					text += " > " +  (useCombo ? getCommandArgCombo() : getCurrentCommand());
				}
				text += "\n";
				line++;
			}

			text += "\n";

			for (Participant par : running) {
				text += par.print();
				if (line == currentLine) {
					text += " > " +  (useCombo ? getCommandArgCombo() : getCurrentCommand());
				}
				text += "\n";
				line++;
			}

			text += "\n";

			for (Participant par : finished) {
				text += par.print();
				if (line == currentLine) {
					text += " > " +  (useCombo ? getCommandArgCombo() : getCurrentCommand());
				}
				text += "\n";
				line++;
			}
		}

		return text;
	}

	private class Participant {

		private String num;
		private String addedTime;
		private String startTime;
		private String endTime;
		private String action;

		public Participant(String id, String time) {
			num = id;
			addedTime = time;
			startTime = "";
			endTime = "";
			action = "";
		}

		public String print() {
			return num + "  " + getTime() + " " + action + " ";
		}

		private String getTime() {
			return action.equals("") ? addedTime : action.equals("R") ? elapsedTime() : endTime;
		}

		private String elapsedTime() {
			return "" + (admin.getSystemTime().getTime() - SystemTime.getTimeInMillis(startTime));
		}
		


	}

	public void setCommandArgCombo(String arg) {
		args += arg;		
		commandArgCombo = args;		
	}
	
	public String getCommandArgCombo() {		
		return commandArgCombo;		
	}
	
	public boolean isScanPrompting() {		
		return scanPrompting;		
	}		
			
	public void promptScanner(int num) {	
		scanPrompting = true;		
		setText("Select scanner type:" +		
				"\nGATE" +		
				"\nEYE" +		
				"\nPAD" +		
				"\n> ");		
	}

}
