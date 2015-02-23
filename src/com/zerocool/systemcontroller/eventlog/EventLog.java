package com.zerocool.systemcontroller.eventlog;

import com.zerocool.systemcontroller.event.AbstractEvent;
import com.zerocool.systemcontroller.timer.Timer;

public class EventLog {

	public EventLog() {
		
	}
	
	public void logTime(long time) {
		
	}
	
	public void logEvent(AbstractEvent event, Timer timer) {
		//write to file in FORMAT:
		
		//timer.getDate() event.getEventName()
		//event.getEventId event.getEventType() event.getEventTime()
	}
	
}
