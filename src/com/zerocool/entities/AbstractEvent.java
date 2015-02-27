package com.zerocool.entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractEvent {
	
	// First event will be 0 then 1, 2... so on.
	protected static int LASTID;
	
	// just doing this for a test
	// type of this event
	protected EventType type;
	
	// name of this event
	protected String eventName;
	
	// sequentially increasing unique identifier
	protected int eventId;
	
	// participants actually competing in this event
	protected  ArrayList<Participant> currentParticipants;
	
	// eventTime stored the entire date but the specific miliseconds, seconds,
	// minutes, hours..etc can be accessed from such
	protected long eventTime;
	
	protected Queue<Participant> startingQueue;
	protected Participant competingPar;

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
		eventId = ++LASTID;
		startingQueue = new LinkedList<Participant>();
	}

	
	// ----- abstract functional methods ----- \\

	/**
	 * initializeEvent:
	 * 
	 * parameter: Collection of Participant objects are given as parameter
	 * function: Stores participant[] parameter Goes through list of
	 * participants and their respective Records setting the eventName and
	 * eventId
	 **/
	public abstract void initializeEvent(ArrayList<Participant> participants);

	/**
	 * Goes through all the current Participants and sets them to be competing
	 * and set's their start time.
	 * @param startTime - The start time of the race.
	 */
	public abstract void startAllParticipants(long startTime);
	
	/**
	 * Starts a specified Participant since some races not all Participants will be
	 * starting at the same time.
	 * @param participant - The Participant to start.
	 * @param startTime - The start time of the specific participant.
	 */
	public abstract void startOneParticipant(Participant participant, long startTime);

	/**
	 * Goes through all the current Participants and sets them to not be
	 * competing and set's their finish time.
	 * @param finishTime - The finish time of the race.
	 */
	public abstract void finishAllParticipants(long finishTime);
	
	/**
	 * Finishes a specified Participant because it is unlikely that all of them will tie.  >.<
	 * @param participant - The Participant to finish.
	 * @param finishTime - The finish time of the specific participant.
	 */
	public abstract void finishOneParticipant(Participant participant, long finishTime);

	
	// ----- functional methods ----- \\

	public void setName(String eventName) {
		this.eventName = eventName;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}
	
	public void setParticipants(ArrayList<Participant> participants){
		this.currentParticipants = participants;
	}

	
	// ----- accessors ----- \\

	public String getEventName() {
		return eventName;
	}

	public EventType getType() {
		return type;
	}

	public long getEventTime() {
		return eventTime;
	}

	public int getEventId() {
		return eventId;
	}

	public ArrayList<Participant> getParticipants() {
		return currentParticipants;
	}
	
	public void addNewParticipant(Participant par){
		startingQueue.add(par);
	}
	
	public void exit() {
		System.out.println("exiting event");
		LASTID = -1;
		type = null;
		eventName = null;
		eventId = -1;
		eventTime = -1;
		System.out.println("done event");
	}
	
}
