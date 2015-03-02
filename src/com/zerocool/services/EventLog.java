package com.zerocool.services;

import java.io.BufferedWriter;
import java.io.File;
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

	/**
	 * Creates a new instance of the {@link #EventLog} class
	 */
	public EventLog() {
		eventFile = new File("eventOut.txt");
		participantFile = new File("participantOut.txt");
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
		} catch (Exception e) { };
	}
	
	/**
	 * @param event
	 * @param systemTime
	 */
	public void logParticipants(String eventParticipantData, SystemTime systemTime) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(participantFile));
			bw.write("Run   BIB      Time\n" + eventParticipantData);
			bw.close();
		} catch (Exception e) { };
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
