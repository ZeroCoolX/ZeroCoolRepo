package com.zerocool.systemcontroller.event;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zerocool.systemcontroller.participant.Participant;

public abstract class AbstractEvent {
	// just doing this for a test
	// type of this event
	protected EventType type;
	// name of this event
	protected String name;
	// sequentially increasing unique identifier
	protected long eventId;
	// participants actually competing in this event
	public  ArrayList<Participant> currentParticipants;
	/*
	 * TOTALLY subject to change. Depends on what Jeremy is using in his Timer
	 * class. I also like Calendar. System.Time. Doesn't matter to me.
	 */
	// dateFormat.format(date) prints (example) 2014/08/06 15:59:48
	protected DateFormat eventTimeFormat = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	// eventTime stored the entire date but the specific miliseconds, seconds,
	// minutes, hours..etc can be accessed from such
	protected Date eventTime;

	/**
	 * Type Descriptions:
	 * 
	 * IND: Individual timed events (such as ski races, bobsled runs) PARIND:
	 * Parallel events (skiing) with individual starts GRP: Group events (cross
	 * country skiing or running) with individual finishes PARGRP: Group
	 * parallel events (swimming) with one start and individual finishes
	 **/
	public enum EventType {
		IND, PARIND, GRP, PARGRP
	};

	/**
	 * Constructors:
	 * 
	 * Multiple (parameter number specific) constructors allow for testers to
	 * initialize an event with the desired data. Default constructor creates a
	 * new instance of the desired event with no name, the Type specific
	 * EventType, the date right now at this instance, and an eventId For
	 * testing purposes multilevel constructors exist as well
	 * **/
	protected AbstractEvent() {
		this("");
	}

	protected AbstractEvent(String name) {
		this(name, EventType.IND);
	}

	protected AbstractEvent(String name, EventType type) {
		this(name, type, new Date());
	}

	protected AbstractEvent(String name, EventType type, Date eventTime) {
		this(name, type, eventTime, -1);
	}

	protected AbstractEvent(String name, EventType type, Date eventTime,
			long eventId) {
		System.out.println("abstract eventid = " + eventId);
		eventId += 1;
		this.type = type;
		this.name = name;
		this.eventTime = eventTime;
	}

	// ----- functional methods ----- \\

	/**
	 * initializeEvent:
	 * 
	 * parameter: Collection of Participant objects are given as parameter
	 * function: Stores participant[] parameter Goes through list of
	 * participants and their respective Records setting the eventName and
	 * eventId
	 **/
	abstract void initializeEvent( ArrayList<Participant> participants);

	/**
	 * startParticipant
	 * 
	 * function: Goes through list of participants setting isCompeting to true
	 * and sets the participants Record's startTime
	 ***/
	abstract void startParticipants();

	/**
	 * finishParticipant
	 * 
	 * function: Goes through list of participants setting isCompeting to false
	 * and sets the participants Record's finishTime
	 ***/
	abstract void finishParticipant();

	// ----- functional methods ----- \\

	public void setName(String name) {
		this.name = name;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	void setEventId(long eventId) {
		this.eventId = eventId;
	}

	// ----- accessors ----- \\

	public String getName() {
		return name;
	}

	public EventType getType() {
		return type;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public long getEventId() {
		return eventId;
	}

	public ArrayList<Participant> getParticipants() {
		return currentParticipants;
	}
}
