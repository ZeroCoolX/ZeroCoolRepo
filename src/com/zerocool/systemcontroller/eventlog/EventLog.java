package com.zerocool.systemcontroller.eventlog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import com.zerocool.systemcontroller.event.AbstractEvent;
import com.zerocool.systemcontroller.systemtime.SystemTime;
import com.zerocool.systemcontroller.timer.Timer;

public class EventLog {
	
	private File file;

	public EventLog() {
		file = new File("eventOutput.txt");
	}
	
	public void logEvent(AbstractEvent event, SystemTime systemTime) {
		
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			String s = systemTime.toString() + event.getName() +"\n" 
					+ event.getEventId() + " " + event.getType() + " " 
					+ systemTime.formatTime(event.getEventTime());
			writer.println(s);
			writer.close();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	
	public File getFile(){
		return file;
	}
	
	public void exit(){
		
	}
}
