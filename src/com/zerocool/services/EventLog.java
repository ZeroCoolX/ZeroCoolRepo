package com.zerocool.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author ZeroCool
 * The class that is in charge of taking Event information and
 * creating output based on Event information
 */
public class EventLog {
	
	private File eventFile;
	private File participantFile;

	private String lastOutput;
	
	private boolean newOutput;
	
	/**
	 * Creates a new instance of the {@link #EventLog} class
	 */
	public EventLog() {
		eventFile = new File("eventOut.txt");
		try {
			eventFile.createNewFile();
		} catch (IOException e) { }
		
		participantFile = new File("participantOut.txt");
		try {
			participantFile.createNewFile();
		} catch (IOException e) { }
	}
	
	/**
	 * Logs an Event into an output file
	 * @param event The event to log
	 * @param systemTime the system time
	 */
	public void logEvent(String eventData, SystemTime systemTime) {
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(eventFile));
			bw.write(systemTime + " " + eventData);
			bw.close();
			lastOutput = systemTime + " " + eventData;
			newOutput = true;
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	/**
	 * @param event
	 * @param systemTime
	 */
	public void logParticipants(String participantData, SystemTime systemTime) {
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(participantFile));
			bw.write("Run   BIB   Time\n");
			bw.write(participantData);
			bw.close();
			lastOutput = participantData;
			newOutput = true;
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	public String read() {
		String printData = "";

		try {
			
		BufferedReader fr = new BufferedReader(new FileReader(eventFile));
		printData += "EVENTLOG\nDATA__________\n\n";

		while (fr.ready()) {
			printData += fr.readLine() + "\n" + fr.readLine() + "\n";
		}
		
		fr.close();

		printData += "\n\n";

		fr = new BufferedReader(new FileReader(participantFile));
		printData += "PARTICIPANT\nDATA__________\n\n";

		while (fr.ready()) {
			printData += fr.readLine()+"\n"+fr.readLine()+"\n";
		}
		
		fr.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return printData;
	}
	
	/**
	 * Gets the output file of the logged Event
	 * @return a File
	 */
	public File getEventFile() {
		return eventFile;
	}

	/**
	 * Gets the output file of the particpant's times
	 * @return
	 */
	public File getParticipantFile() {
		return participantFile;
	}
	
	public String getLastOutput() {
		newOutput = false;
		return lastOutput;
	}
	
	public boolean isNewOutput() {
		return newOutput;
	}
	
	/**
	 * Resets variables and deletes the output text files
	 */
	public void exit() {
		// Attempts to delete both files
		try {
			Files.delete(eventFile.toPath());
		} catch (IOException e) { };
		
		try {
			Files.delete(participantFile.toPath());
		} catch (IOException e) { };
		
		eventFile = null;
		participantFile = null;
	}
}
