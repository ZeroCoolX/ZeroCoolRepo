package com.zerocool.systemcontroller.timer;

import java.util.ArrayList;

import com.zerocool.systemcontroller.event.AbstractEvent;
import com.zerocool.systemcontroller.eventlog.EventLog;
import com.zerocool.systemcontroller.participant.Participant;
import com.zerocool.systemcontroller.systemtime.SystemTime;

public class Timer {
	
	private SystemTime systemTime;
	private ArrayList<Participant> totalParticipants;
	private AbstractEvent currentEvent;
	private EventLog eventLog;

	public Timer() {
		// do nothing
	}
	
	public Timer(SystemTime systemTime) {
		this.systemTime = systemTime;
	}
	
	public Timer(SystemTime systemTime, AbstractEvent event) {
		this(systemTime);
		currentEvent = event;
	}
	
	public Timer(SystemTime systemTime, AbstractEvent event, ArrayList<Participant> participants) {
		this(systemTime, event);
		totalParticipants = participants;
	}
	
	public Timer(SystemTime systemTime, AbstractEvent event, ArrayList<Participant> participants, EventLog eventLog) {
		this(systemTime, event, participants);
		this.eventLog = eventLog;
	}
	
	
	
	// ----- functional methods ----- \\
	
	public void startEvent() { 
		//Must actually initialize the event
		currentEvent.initializeEvent(totalParticipants);
		//Then start the event (which also fills in the event name and eventId of each participants record)
		currentEvent.startAllParticipants(systemTime.getTime());
	}
	
	public void endEvent() { 
		//Finish all participants (for this sprint this will work)
		//Further on, when we're doing more that just one single participant we need functionality to start and finish specific participants
		currentEvent.finishAllParticipants(systemTime.getTime());
	}
	
	
	// ----- accessors ----- \\
	
	public long getEventTime() {
		return currentEvent.getEventTime();
	}
	
	
	public AbstractEvent getEvent() {
		return currentEvent;
	}
	
	public ArrayList<Participant> getTotalParticipants() { 
		return totalParticipants; 
	}
	
	public AbstractEvent getCurrentEvent() { 
		return currentEvent; 
	}
	
	
	// ----- mutators ----- \\
	
	public void setEventLog(EventLog eventLog) { 
		this.eventLog = eventLog; 
	}
	
	public void setEvent(AbstractEvent event) { 
		currentEvent = event;
	}
	
	public void exit(){
		systemTime.exit();
		for(Participant par: totalParticipants){
			par.exit();
		}
		currentEvent.exit();
		eventLog.exit();
	}
	
}
