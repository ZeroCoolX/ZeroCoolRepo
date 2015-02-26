package com.zerocool.systemcontroller.eventlog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.zerocool.systemcontroller.event.AbstractEvent;
import com.zerocool.systemcontroller.systemtime.SystemTime;

/**
 * @author ZeroCool
 * The class that is in charge of taking Event information and
 * creating output based on Event information
 */
public class EventLog {
	
	private File file;

	/**
	 * Creates a new instance of the {@link #EventLog} class
	 */
	public EventLog() {
		file = new File("eventOutput.txt");
	}
	
	/**
	 * Logs an Event into an output file
	 * @param event The event to log
	 * @param systemTime the system time
	 */
	public void logEvent(AbstractEvent event, SystemTime systemTime) {
		try{
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			String s = systemTime.toString() + " " + event.getEventName() +"\n" 
					+ event.getEventId() + " " + event.getType() + " " 
					+ SystemTime.formatTime(event.getEventTime());
			writer.println(s);
			writer.close();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	
	/**
	 * Gets the output file of the logged Event
	 * @return a File
	 */
	public File getFile() {
		return file;
	}
	
	public void exit() {
		
	}
}
