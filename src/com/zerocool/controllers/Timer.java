package com.zerocool.controllers;

import java.util.ArrayList;

import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Group;
import com.zerocool.entities.Individual;
import com.zerocool.entities.ParGroup;
import com.zerocool.entities.ParIndividual;
import com.zerocool.entities.Participant;
import com.zerocool.services.SystemTime;

public class Timer {
	
	private ArrayList<Participant> totalParticipants;
	private SystemTime systemTime;
	private AbstractEvent currentEvent;
	
	public Timer(SystemTime systemTime) {
		this.systemTime = systemTime;
	}
	
	public Timer(SystemTime systemTime, EventType type, String eventName) {
		this(systemTime);
		createEvent(type, eventName);
	}
	
	public Timer(SystemTime systemTime, EventType type, String eventName, ArrayList<Participant> participants) {
		this(systemTime);
		createEvent(type, eventName, participants);
		totalParticipants = participants;
	}	
	
	
	// ----- functional methods ----- \\
	
	/**
	 * Starts all the Participants in the starting queue.
	 * @throws IllegalStateException() if there are no Participants in
	 * 	the starting queue.
	 */
	public void startAllParticipants() {
		currentEvent.startAllParticipants(systemTime.getTime());
	}
	
	/**
	 * Starts the next participant in the starting queue.
	 * @throws IllegalStateException - There are no Participants in
	 * 	the starting queue.
	 */
	public void startNextParticipant() {
		currentEvent.startNextParticipant(systemTime.getTime());
	}
	
	/**
	 * Starts a specified number of participants from the starting queue.
	 * @param numOfParticipants - The number of participants to start.
	 * @param startTime - The time the Participants started.
	 * @throws IllegalArgumentException - THe specified number of Participants to start
	 * 	is less than zero or is bigger than the number of Participants in the starting queue.
	 * @throws IllegalStateException - There are no Participants in
	 * 	the starting queue.
	 */
	public void startNextParticipants(int numOfParticipants) {
		currentEvent.startNextParticipants(numOfParticipants, systemTime.getTime());
	}
	
	/**
	 * Finish all the Participants currently competing.
	 * @param finishTime - The time at which the Participants finished.
	 * @throws IllegalStateException - There are no Participants currently
	 * 	competing.
	 * @throws IllegalStateException - The Participant is not currently
	 * 	competing.
	 */
	public void finishAllParticipants() { 
		currentEvent.finishAllParticipants(systemTime.getTime());
	}
	
	/**
	 * Finish a specific Participant.
	 * @param participant - The Participant to finish.
	 * @param finishTime - The time at which the Participant finished.
	 * @throws IllegalArgumentException - The Participant is null.
	 * @throws IllegalStateException - There are no Participants currently
	 * 	competing.
	 * @throws IllegalStateException - The Participant is not currently
	 * 	competing.
	 */
	public void finishParticipant(Participant participant) {
		currentEvent.finishParticipant(participant, systemTime.getTime());
	}
	
	/**
	 * Indicates a false start so it resets the data back to before the start was called.
	 */
	public void cancelStart() {
		currentEvent.resetCompeting();
	}
	
	/**
	 * Sets all competing Participants to DNF.
	 */
	public void setAllDNF() {
		currentEvent.setAllDNF();
	}
	
	/**
	 * Sets a competing Participant to DNF.
	 * @param participant - The Participant to set to DNF.
	 * @throws IllegalArgumentException - The Participant is null.
	 * @throws IllegalStateException - The Participant is not currently
	 * 	competing.
	 */
	public void setOneDNF(Participant participant) {
		currentEvent.setOneDNF(participant);
	}
	
	
	// ----- accessors ----- \\
	
	/**
	 * Gets the current event's time.
	 * @return The current event's time.
	 */
	public long getEventTime() {
		return currentEvent.getEventTime();
	}
	
	public ArrayList<Participant> getTotalParticipants() { 
		return totalParticipants; 
	}
	
	public Participant findParticipant(int participantId) {
		for (Participant par : totalParticipants) {
			if (par.getID() == participantId) {
				return par;
			}
		}
		
		return null;
	}
	
	public AbstractEvent getCurrentEvent() { 
		return currentEvent;
	}
	
		
	// ----- mutators ----- \\
	
	/**
	 * 
	 * @param type
	 * @param eventName
	 */
	public void createEvent(EventType type, String eventName) {
		//IND, PARIND, GRP, PARGRP
		switch(type) {
		case IND:
			currentEvent = new Individual(eventName, systemTime.getTime());
			break;
		case PARIND:
			currentEvent = new ParIndividual(eventName, systemTime.getTime());
			break;
		case GRP:
			currentEvent = new Group(eventName, systemTime.getTime());
			break;
		case PARGRP:
			currentEvent = new ParGroup(eventName, systemTime.getTime());
			break;
		default:
			throw new IllegalArgumentException("Invalid Event Type");
		}
	}
	
	/**
	 * 
	 * @param type
	 * @param eventName
	 * @param participants
	 */
	public void createEvent(EventType type, String eventName, ArrayList<Participant> participants) {
		createEvent(type, eventName);
		for (Participant par : participants) {
			addParticipantToStart(par);
		}
	}

	/**
	 * Adds a participant to totalParticipants, currentParticipants, creates a new record and
	 * add them to the starting queue.
	 * @param participant - The Participant to add.
	 * @throws IllegalArgumentException - The Participant is null.
	 */
	public void addParticipantToStart(Participant participant) {
		if (participant == null) {
			throw new IllegalArgumentException("Participant can't be null.");
		}
		totalParticipants.add(participant);
		participant.createNewRecord(currentEvent.getEventName(), currentEvent.getEventId());
		currentEvent.addParticipantToStart(participant);
	}
	
	/**
	 * Adds a participant to totalParticipants, currentParticipants, creates a new record and
	 * add them to the starting queue by a given id.
	 * @param participantId - The id to create a new Participant from.
	 */
	public void addParticipantToStart(int participantId) {
		addParticipantToStart(new Participant(participantId, "" + participantId));
	}
	
	
	/**
	 * Exits gracefully.
	 */
	public void exit() {
		for (Participant par: totalParticipants) {
			par.exit();
		}
		currentEvent.exit();
	}
	
}
