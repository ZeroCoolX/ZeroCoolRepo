package com.zerocool.systemcontroller.eventlog;

import java.io.File;

import com.zerocool.systemcontroller.event.AbstractEvent;
import com.zerocool.systemcontroller.timer.Timer;

public class EventLog {
	
	private File logFile;

	public EventLog() {
		
	}
	
	public void logTime(long time) {
		
	}
	
	public void logEvent(AbstractEvent event, Timer timer) {
		//write to file in FORMAT:
		
		//timer.getDate() event.getEventName()
		//event.getEventId event.getEventType() event.getEventTime()
	}
	
	public File getFile(){
		return logFile;
	}
	
	public void exit(){
		//not sure what to really do for this guy..
	}
	
}
