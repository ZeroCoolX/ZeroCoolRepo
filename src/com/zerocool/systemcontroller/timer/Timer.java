package com.zerocool.systemcontroller.timer;

import org.apache.commons.lang3.time.StopWatch;

import com.zerocool.systemcontroller.event.AbstractEvent;
import com.zerocool.systemcontroller.eventlog.EventLog;
import com.zerocool.systemcontroller.participant.Participant;

public class Timer {
	
	private StopWatch stopwatch;
	private Participant[] totalParticipants;
	private AbstractEvent currentEvent;
	private EventLog eventLogger;

	public Timer() {
		stopwatch = new StopWatch();
	}
	
	public Timer(AbstractEvent event) {
		this();
		this.currentEvent=event;
	}
	
	public Timer(long time, AbstractEvent event, EventLog eventLog) {
		this(event);
		this.eventLogger=eventLog;
	}
	
	
	// ----- functional methods ----- \\
	
	public void startEvent() { stopwatch.start(); }
	
	public void endEvent() { stopwatch.stop(); }
	
	public void resetTime() { stopwatch.reset(); }
	
	
	// ----- accessors ----- \\
	
	public long getEventTime() { return stopwatch.getStartTime(); }
	
	public Participant[] getTotalParticipants() { return totalParticipants; }
	
	public AbstractEvent getCurrentEvent() { return currentEvent; }
	
	
	// ----- mutators ----- \\
	
	public void setEventLog(EventLog eventLog) { eventLogger=eventLog; }
	
	public void setEvent(AbstractEvent event) { currentEvent=event;}
	
}
