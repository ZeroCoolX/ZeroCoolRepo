package com.zerocool.systemcontroller.event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zerocool.systemcontroller.event.AbstractEvent.EventType;
import com.zerocool.systemcontroller.participant.Participant;

public class ParIndividual extends AbstractEvent {

	protected EventType type;
	protected String name;
	protected long eventId;
	protected  ArrayList<Participant> currentParticipants;
	// dateFormat.format(date) prints (example) 2014/08/06 15:59:48
	protected DateFormat eventTimeFormat = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	// eventTime stored the entire date but the specific miliseconds, seconds,
	// minutes, hours..etc can be accessed from such
	protected long eventTime;

	public ParIndividual() {
		this("");
	}

	public ParIndividual(String name) {
		this(name, EventType.GRP);
	}

	public ParIndividual(String name, EventType type) {
		this(name, type, 0);
	}

	public ParIndividual(String name, EventType type, long eventTime) {
		this(name, type, eventTime, -1);
	}

	public ParIndividual(String name, EventType type, long eventTime, long eventId) {
		super();
		System.out.println("abstract eventid = " + eventId);
		System.out.println("type= " + type);
		System.out.println("name = " + name);
		this.type = type;
		this.name = name;
		this.eventTime = eventTime;
	}

	public void initializeEvent( ArrayList<Participant> participants) {
		// must check for null parameter
		currentParticipants = participants;
		// go through each participant and set their eventId and event name
		for (Participant curPar : currentParticipants) {
			curPar.getLastRecord().setEventName(name);
			curPar.getLastRecord().setEventId(eventId);
		}
	}

	public void startParticipants() {
		System.out.println("Starting ParIndividual Participants");
		// go through each participant and set the start time
		for (Participant curPar : currentParticipants) {
			curPar.setIsCompeting(true);
			curPar.getLastRecord().setStartTime(3425);
		}
	}

	public void finishParticipants() {
		System.out.println("Finishing ParIndividual Participants");
		for (Participant curPar : currentParticipants) {
			curPar.setIsCompeting(false);
			curPar.getLastRecord().setFinishTime(1123);
		}
	}

	public  ArrayList<Participant> getParticipants() {
		return currentParticipants;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	void setParticipants(ArrayList<Participant> participants){
		this.currentParticipants = participants;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

}