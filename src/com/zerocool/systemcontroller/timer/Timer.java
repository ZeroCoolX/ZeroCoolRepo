package com.zerocool.systemcontroller.timer;

import org.apache.commons.lang3.time.StopWatch;

import com.zerocool.systemcontroller.event.AbstractEvent;
import com.zerocool.systemcontroller.eventlog.EventLog;
import com.zerocool.systemcontroller.participant.Participant;

public class Timer {
	
	private StopWatch stopwatch;
	private Participant[] totalParticipants;
	private AbstractEvent currentEvent;

	public Timer() {
		stopwatch = new StopWatch();
	}
	
	public Timer(long time) {
		
	}
	
	public Timer(long time, AbstractEvent event) {
		this(time);
		
	}
	
	public Timer(long time, AbstractEvent event, EventLog eventLog) {
		this(time, event);
		
	}
	
	
	// ----- functional methods ----- \\
	
	public void startEvent() {
		
	}
	
	public void endEvent() {
		
	}
	
	public void resetTime() {
		
	}
	
	
	// ----- accessors ----- \\
	
	public long getEventTime() {
		
		return -1;
	}
	
	public long getSystemTime() {
		
		return -1;
	}
	
	public long getCurrentSystemTime() {
		
		return -1;
	}
	
	public int getTotalParticipants() {
		
		return -1;
	}
	
	public AbstractEvent getCurrentEvent() {
		
		return null;
	}
	
	
	// ----- mutators ----- \\
	
	public void setSystemTime(long time) {
		
	}
	
	public void setEventLog(EventLog eventLog) {
		
	}
	
	public void setEvent(AbstractEvent event) {
		
	}
	
}
