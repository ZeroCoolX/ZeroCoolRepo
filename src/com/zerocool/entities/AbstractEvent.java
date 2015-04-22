package com.zerocool.entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.zerocool.services.SystemTime;

public abstract class AbstractEvent {

	// First event will be 1, 2... so on.
	protected static int LASTID = 0;

	// just doing this for a test
	// type of this event
	protected EventType type;

	// name of this event
	protected String eventName;

	// participants actually competing in this event
	protected ArrayList<Participant> currentParticipants;
	protected Queue<Participant> startingQueue;
	protected Queue<Participant> runningQueue;
	protected Queue<Participant> finishedQueue;
	
	// eventTime stored the entire date but the specific miliseconds, seconds,
	// minutes, hours..etc can be accessed from such
	protected long eventTime;
	
	// sequentially increasing unique identifier
	protected int eventId;

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
		currentParticipants = new ArrayList<Participant>();
		startingQueue = new LinkedList<Participant>();
		runningQueue = new LinkedList<Participant>();
		finishedQueue = new LinkedList<Participant>();
	}

	/**
	 * USE FOR TESTING PURPOSES ONLY!
	 * 
	 * Resets the value of LASTID to 0.
	 */
	public void resetEventId() {
		LASTID = 0;
	}
	
	
	public static boolean isValidEventType(String type) {
		for (EventType et : EventType.values()) {
			if (et.name().equals(type)) {
				return true;
			}
		}
		
		return false;
	}
	
	// ----- override methods ----- \\
	
	/**
	 * Tells us a sensor was triggered so based on the type of Event, it either
	 * starts or finishes Participants.
	 * @param time - The time the sensor was triggered.
	 */
	public abstract void triggered(long time, int channel);

	/**
	 * Sets the next Participant in the running queue to 'Did Not Finish'.
	 */
	public abstract void setDnf(long time);


	// ----- functional methods ----- \\

	/**
	 * Sets the name of the event.
	 * @param eventName - The name to set the event to.
	 */
	public void setName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * Sets the time of the event.
	 * @param eventTime - The time to set the event to.
	 */
	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	/**
	 * Adds a participant to currentParticipants if not there already and adds them
	 * to the starting queue.
	 * @param participant - The Participant to add.
	 * @throws IllegalArgumentException - The Participant is null.
	 */
	public void addParticipant(Participant participant) throws IllegalArgumentException {
		if (participant == null || currentParticipants.contains(participant)) {
			throw new IllegalArgumentException("Participant can't be null or in the queue already.");
		}
		currentParticipants.add(participant);
		startingQueue.add(participant);
	}

	/**
	 * Indicates that the last start was a false start, so this method resets the
	 * starting queue and competingParticipants.
	 */
	public void resetEvent() {
		Queue<Participant> newStartingQueue = new LinkedList<Participant>();

		Participant par;
		while ((par = finishedQueue.poll()) != null) {
			newStartingQueue.add(par);
		}
		
		while ((par = runningQueue.poll()) != null) {
			par.setIsCompeting(false);
			newStartingQueue.add(par);
		}

		while ((par = startingQueue.poll()) != null) {
			newStartingQueue.add(par);
		}

		startingQueue = newStartingQueue;
	}
	
	/**
	 * Creates a new run for this event
	 * by emptying the event queues and current participants
	 * to make room for the next batch of participants.
	 */
	public void newRun() {
		currentParticipants = new ArrayList<Participant>();
		startingQueue = new LinkedList<Participant>();
		runningQueue = new LinkedList<Participant>();
		finishedQueue = new LinkedList<Participant>();
	}

	// ----- accessors ----- \\

	/**
	 * Gets the event name.
	 * @return The name of the event.
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * Gets the type of the event.
	 * @return The event type.
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * Gets the event time.
	 * @return The event time.
	 */
	public long getEventTime() {
		return eventTime;
	}
	
	/**
	 * Gets the formatted event time.
	 * @return The formatted event time.
	 */
	public String getFormattedEventTime() {
		return SystemTime.formatTime(eventTime);
	}

	/**
	 * Gets the event id.
	 * @return The event id.
	 */
	public int getEventId() {
		return eventId;
	}

	/**
	 * Gets the current Participants of the event.
	 * @return The current Participants.
	 */
	public ArrayList<Participant> getCurrentParticipants() {
		return currentParticipants;
	}

	/**
	 * Gets the starting queue of the event.
	 * @return - The starting queue.
	 */
	public Queue<Participant> getStartingQueue() {
		return startingQueue;
	}
	
	/**
	 * Gets the currently competing Participants of the event.
	 * @return - The competing Participants.
	 */
	public Queue<Participant> getRunningQueue() {
		return runningQueue;
	}

	/**
	 * Gets the finished Participants of the event.
	 * @return - The finished Participants.
	 */
	public Queue<Participant> getFinishedQueue() {
		 return finishedQueue;
	}
	
	/**
	 * Returns all the data needed from an event to print to the log file in the format:
	 * <EventName> '\n'
	 * <EventId> <EventType> <SystemTime> '\n'
	 * @return - The formatted data.
	 */
	public String getFormattedData() {
		return eventName + "\n" + eventId + " " + type + " " + getFormattedEventTime() + "\n";
	}


	// ----- helper methods ----- \\

	/**
	 * Sets a specified Participant to be competing, adds them to
	 * the current running list and adds the start time to their record.
	 * @param participant - The participant to start.
	 * @throws IllegalStateException - If the starting queue is empty.
	 */
	protected void startParticipant(long startTime) throws IllegalStateException {
		if (startingQueue.isEmpty()) {
			throw new IllegalStateException("There are no Participants to start!");
		}
		
		Participant participant = startingQueue.poll();
		participant.setIsCompeting(true);
		participant.getLastRecord().setStartTime(startTime);
		runningQueue.add(participant);
	}

	/**
	 * Finishes the given Participant by setting competing to false, setting their
	 * finish time, setting 'Did Not Finish' and adds them to the finished queue.
	 * @param participant - The Participant to finish.
	 * @param setDnf - True to set 'Did Not Finish' else false.
	 * @throws IllegalStateException - If the running queue is empty.
	 */
	protected void finishParticipant(long finishTime, boolean setDnf) throws IllegalStateException {
		if (runningQueue.isEmpty()) {
			throw new IllegalStateException("There are no Participants to finish!");
		}
		
		Participant participant = runningQueue.poll();
		participant.setIsCompeting(false);
		participant.getLastRecord().setFinishTime(finishTime);
		participant.getLastRecord().setDnf(setDnf);
		finishedQueue.add(participant);
	}
	
	/**
	 * Override for more method functionality.
	 * Gracefully shuts down.
	 */
	public void exit() {
		LASTID = -1;
		type = null;
		eventName = null;
		currentParticipants = null;
		startingQueue = null;
		runningQueue = null;
		finishedQueue = null;
		eventTime = -1;
		eventId = -1;
	}

}
