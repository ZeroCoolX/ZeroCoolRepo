package com.zerocool.systemcontroller.eventlog;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import com.zerocool.systemcontroller.event.AbstractEvent;
import com.zerocool.systemcontroller.systemtime.SystemTime;
import com.zerocool.systemcontroller.timer.Timer;

public class EventLog {
	
	private Path logFile;

	public EventLog() {
	}
	
	public void logEvent(AbstractEvent event,Timer timer, SystemTime system) {
		
		Charset charset = Charset.forName("US-ASCII");
	
		try (BufferedWriter writer = Files.newBufferedWriter(logFile, charset)) {
			String s = system.toString() + event.getName() +"\n" 
					+ event.getEventId() + " " + event.getType() + " " 
					+ system.formatTime(event.getEventTime());
			writer.write(s, 0, s.length());
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	public String getFile(){
		return logFile.toFile().toString();
	}
	
	public void exit(){
		
	}
	
}
