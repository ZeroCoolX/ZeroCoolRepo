package com.zerocool.entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class AbstractEvent {

	// First event will be 1, 2... so on.
	protected static int LASTID;

	// just doing this for a test
	// type of this event
	protected EventType type;

	// name of this event
	protected String eventName;

	// participants actually competing in this event
	protected ArrayList<Participant> currentParticipants;
	protected ArrayList<Participant> competingParticipants;

	protected Queue<Participant> startingQueue;
	
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
		startingQueue = new LinkedList<Participant>();
		currentParticipants = new ArrayList<Participant>();
		competingParticipants = new ArrayList<Participant>();
	}


	// ----- override methods ----- \\

	/**
	 * No need to override because it just calls startNextParticipants() anyway.
	 * Starts all the Participants in the starting queue.
	 * @param startTime - The time the Participant started.
	 */
	public void startAllParticipants(long startTime) {
		startNextParticipants(startingQueue.size(), startTime);
	}

	/**
	 * Override in subclass for more functionality.
	 * Starts the next Participant in the starting queue.
	 * @param startTime - The time the Participant started.
	 * @throws IllegalStateException - There are no Participants in
	 * 	the starting queue.
	 */
	public void startNextParticipant(long startTime) {
		if (startingQueue.isEmpty()) {
			throw new IllegalStateException("There are no participants in the starting queue.");
		}
	}

	/**
	 * No need to override this one because it just calls startNextParticipant() anyway.
	 * Starts a specified number of participants from the starting queue.
	 * @param numOfParticipants - The number of participants to start.
	 * @param startTime - The time the Participants started.
	 * @throws IllegalArgumentException - THe specified number of Participants to start
	 * 	is less than zero or is bigger than the number of Participants in the starting queue.
	 * @throws IllegalStateException - There are no Participants in
	 * 	the starting queue.
	 */
	public void startNextParticipants(int numOfParticipants, long startTime) {
		if (numOfParticipants < 0 || numOfParticipants > startingQueue.size()) {
			throw new IllegalArgumentException("Start queue does not have " + numOfParticipants + ".");
		}

		for (int i = 0; i < numOfParticipants; i++) {
			startNextParticipant(startTime);
		}
	}

	/**
	 * No need to override this one because it just calls finishParticipant() anyway.
	 * Finish all the Participants currently competing.
	 * @param finishTime - The time at which the Participants finished.
	 */
	public void finishAllParticipants(long finishTime) {
		for (Participant par : competingParticipants) {
			finishParticipant(par, finishTime);
		}
	}

	/**
	 * Override in subclass for more detailed functionality.
	 * Finish a specific Participant.
	 * @param participant - The Participant to finish.
	 * @param finishTime - The time at which the Participant finished.
	 * @throws IllegalArgumentException - The Participant is null.
	 * @throws IllegalStateException - There are no Participants currently
	 * 	competing.
	 * @throws IllegalStateException - The Participant is not currently
	 * 	competing.
	 */
	public void finishParticipant(Participant participant, long finishTime) {
		if (participant == null) {
			throw new IllegalArgumentException("Participant can't be null.");
		}
		if (competingParticipants.isEmpty()) {
			throw new IllegalStateException("There are no participants competing.");
		}
		if (!competingParticipants.contains(participant) || !participant.getIsCompeting()) {
			throw new IllegalStateException("Not a valid competing Participant.");
		}
	}


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
	public void addParticipantToStart(Participant participant) {
		if (participant == null) {
			throw new IllegalArgumentException("Participant can't be null.");
		}
		
		if (!currentParticipants.contains(participant)) {
			currentParticipants.add(participant);
		}
		
		startingQueue.add(participant);
	}

	/**
	 * Indicates that the last start was a false start, so this method resets the
	 * starting queue and competingParticipants.
	 */
	public void resetCompeting() {
		Queue<Participant> newStartingQueue = new LinkedList<Participant>();

		for (Participant par : competingParticipants) {
			par.setIsCompeting(false);
			newStartingQueue.add(par);
		}

		while (!startingQueue.isEmpty()) {
			newStartingQueue.add(startingQueue.poll());
		}

		competingParticipants.clear();
		startingQueue = newStartingQueue;
	}

	/**
	 * Sets all competing Participants to DNF.
	 */
	public void setAllDNF() {
		for (Participant par : competingParticipants) {
			setOneDNF(par);
		}
	}
	
	/**
	 * Sets a competing Participant to DNF.
	 * @param participant - The Participant to set to DNF.
	 * @throws IllegalArgumentException - The Participant is null.
	 * @throws IllegalStateException - The Participant is not currently
	 * 	competing.
	 */
	public void setOneDNF(Participant participant) {
		if (participant == null) {
			throw new IllegalArgumentException("Participant can't be null.");
		}
		if (!competingParticipants.contains(participant) || !participant.getIsCompeting()) {
			throw new IllegalStateException("Not a valid competing Participant.");
		}
		
		participant.setIsCompeting(false);
		participant.getLastRecord().setDnf(true);
		competingParticipants.remove(participant);
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
	public abstract EventType getType();

	/**
	 * Gets the event time.
	 * @return The event time.
	 */
	public long getEventTime() {
		return eventTime;
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
	public List<Participant> getCurrentParticipants() {
		return currentParticipants;
	}

	/**
	 * Gets the currently competing Participants of the event.
	 * @return The competing Participants.
	 */
	public List<Participant> getCompetingParticipants() {
		return competingParticipants;
	}

	/**
	 * Gets the starting queue of the event.
	 * @return The starting queue.
	 */
	public Queue<Participant> getStartingQueue() {
		return this.startingQueue;
	}


	// ----- helper methods ----- \\

	/**
	 * Sets a specified Participant to be competing and adds them to
	 * the currently competing list.
	 * @param participant - The participant to set to competing.
	 */
	protected void addCompetingParticipant(Participant participant) {
		participant.setIsCompeting(true);
		competingParticipants.add(participant);
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
		competingParticipants = null;
		startingQueue = null;
		eventTime = -1;
		eventId = -1;
	}

}
