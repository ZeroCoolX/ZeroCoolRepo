package com.zerocool.services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.Participant;

/**
 * @author ZeroCool
 * The class that is in charge of taking Event information and
 * creating output based on Event information
 */
public class EventLog {
	
	private File eventFile;
	private File participantFile;
	private Charset charset;

	/**
	 * Creates a new instance of the {@link #EventLog} class
	 */
	public EventLog() {
		eventFile = new File("eventOutput.txt");
		participantFile = new File("competitorOut.txt");
		charset = Charset.forName("US-ASCII");
	}
	
	/**
	 * Logs an Event into an output file
	 * @param event The event to log
	 * @param systemTime the system time
	 */
	public void logEvent(AbstractEvent event, SystemTime systemTime) {
		
		try(PrintWriter writer = new PrintWriter(Files.newBufferedWriter(eventFile.toPath(),charset,
				StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE))) {
			String s = systemTime.toString() + " " + event.getEventName() +"\n" 
					+ event.getEventId() + " " + event.getType().toString() + " " 
					+ SystemTime.formatTime(event.getEventTime());
			writer.println(s);
			writer.close();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	
	/**
	 * @param event
	 * @param systemTime
	 */
	public void logParticipants(AbstractEvent event, SystemTime systemTime) {
		String fileLine = "Run   BIB      Time\n";
		List<Participant> participants = event.getParticipants();
		for(Participant p: participants) {
			fileLine += p.getFormattedData(event.getEventId()) + "\n";
		}
		
		try(PrintWriter writer = new PrintWriter(Files.newBufferedWriter(participantFile.toPath(),charset,
			StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE))) {
			writer.println(fileLine);
			writer.close();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
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
	
	public void exit() {
		eventFile = null;
		participantFile = null;
		charset = null;
	}
}
