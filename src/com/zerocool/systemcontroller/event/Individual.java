package com.zerocool.systemcontroller.event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zerocool.systemcontroller.event.AbstractEvent.EventType;
import com.zerocool.systemcontroller.participant.Participant;

public class Individual extends AbstractEvent {

	protected EventType type;
	protected String name;
	protected long eventId;
	// only one Participant is needed since this is an Individual event
	protected  ArrayList<Participant> currentParticipants;
	// dateFormat.format(date) prints (example) 2014/08/06 15:59:48
	protected DateFormat eventTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	// eventTime stored the entire date but the specific miliseconds, seconds,
	// minutes, hours..etc can be accessed from such
	protected long eventTime;
	SimpleDateFormat f = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");

	public Individual() {
		this("");
	}

	public Individual(String name) {
		this(name, EventType.IND);
	}

	public Individual(String name, EventType type) {
		this(name, type, 0);
	}

	public Individual(String name, EventType type, long eventTime) {
		this(name, type, eventTime, -1);
	}

	public Individual(String name, EventType type, long eventTime, long eventId) {
		super();
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
		System.out.println("Starting Individual Participants");
		// go through each participant and set the start time
		for (Participant curPar : currentParticipants) {
			curPar.setIsCompeting(true);
			curPar.getLastRecord().setStartTime(3399);
		}
	}
	
	public void startParticipant(Participant par){
		par.setIsCompeting(true);
		par.getLastRecord().setStartTime(3399);
	}

	public void finishParticipants() {
		System.out.println("Finishing Individual Participants");
		for (Participant curPar : currentParticipants) {
			curPar.setIsCompeting(false);
			curPar.getLastRecord().setFinishTime(3);
		}
	}
	
	public void finishParticipant(Participant par){
		par.setIsCompeting(false);
		par.getLastRecord().setFinishTime(3);
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

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}
	
	public void setParticipants(ArrayList<Participant> participants){
		this.currentParticipants = participants;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

}
