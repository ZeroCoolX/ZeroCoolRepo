package com.zerocool.controllers;

import java.util.ArrayList;
import java.util.Iterator;

import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Group;
import com.zerocool.entities.Individual;
import com.zerocool.entities.ParGroup;
import com.zerocool.entities.ParIndividual;
import com.zerocool.entities.Participant;
import com.zerocool.entities.ParticipantView;
import com.zerocool.services.SystemTime;

public class Timer {

	private ArrayList<Participant> totalParticipants;
	private SystemTime systemTime;
	private AbstractEvent currentEvent;
	private ArrayList<AbstractEvent> totalEvents;

	public Timer(SystemTime systemTime) {
		this.systemTime = systemTime;
		totalParticipants = new ArrayList<Participant>();
		totalEvents = new ArrayList<AbstractEvent>();
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

	// USE FOR TESTING PURPOSES ONLY!
	public void resetEventId() {
		currentEvent.resetEventId();
	}

	// USE ONLY FOR TESTING PURPOSES!
	public AbstractEvent getCurrentEvent() {
		return currentEvent;
	}

	/**
	 * Starts the Event.  Depending on the Event depends on the type of
	 * start.
	 * 
	 * @throws IllegalStateException - There are no Participants in
	 * 	the starting queue.
	 */
	public void triggered(int channel) throws IllegalStateException {
		currentEvent.triggered(systemTime.getTime(), channel);
	}

	public void setDnf() {
		currentEvent.setDnf(systemTime.getTime());
	}

	/**
	 * Indicates a false start so it resets the data back to before the start was called.
	 */
	public void cancelStart() {
		currentEvent.resetEvent();
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
			if (par.getId() == participantId) {
				return par;
			}
		}

		return null;
	}

	public String getEventData() {
		return currentEvent.getFormattedData();
	}
	
	public String getStartingQueue() {
		return printQueue(currentEvent.getStartingQueue().toArray(), "");
	}
	
	public String getRunningQueue() {
		return printQueue(currentEvent.getRunningQueue().toArray(), "R");
	}
	
	public String getFinishedQueue() {
		return printQueue(currentEvent.getFinishedQueue().toArray(), "F");
	}
	
	private String printQueue(Object[] queue, String end) {
		String print = "";
		for (Object obj : queue) {
			Participant par = (Participant) obj;
			print += par.getId() + "     ";
			if (end.equals("")) {
				print += SystemTime.formatTime(currentEvent.getEventTime());
			} else if (end.equals("R")) {
				print += SystemTime.formatTime(systemTime.getTime() - par.getLastRecord().getStartTime()) + "   " + end;
			} else if (end.equals("F")) {
				print += par.getLastRecord().getDnf() ? "DNF" : (SystemTime.formatTime(par.getLastRecord().getElapsedTime()) + "   " + end);
			}
			print += "\n";
		}
		
		return print;
	}

	public String getEventParticipantData() {
		String data = "";

		for (Iterator<Participant> it = currentEvent.getFinishedQueue().iterator(); it.hasNext();) {
			data += it.next().getFormattedData() + "\n";
		}

		return data;
	}

	public String getEventParticipantElapsedData() {
		String data = "";

		for (Iterator<Participant> it = currentEvent.getFinishedQueue().iterator(); it.hasNext();) {
			data += it.next().getElapsedFormattedData() + "\n";
		}

		return data;
	}

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
		totalEvents.add(currentEvent);
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
	
	public ArrayList<ParticipantView> getTotalParticipantView() {
		ArrayList<ParticipantView> list = new ArrayList<ParticipantView>();
		for (Participant par : totalParticipants) {
			list.add(new ParticipantView(par));
		}
		
		return list;
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

		if (!totalParticipants.contains(participant)) {
			totalParticipants.add(participant);
		}

		participant.createNewRecord(currentEvent.getEventName(), currentEvent.getEventId());
		currentEvent.addParticipant(participant);
	}

	/**
	 * Creates a new Participant if one does not already exist with the given id.
	 * Adds the participant to totalParticipants, currentParticipants, creates a new record and
	 * add them to the starting queue.
	 * @param participantId - The id to create a new Participant from.
	 */
	public void addParticipantToStart(int participantId) {
		addParticipantToStart("Aaron " + participantId, participantId);
	}

	public AbstractEvent getLastEvent() {
		return totalEvents.get(totalEvents.size() - 1);
	}

	public ArrayList<AbstractEvent> getTotalEvents() {
		return totalEvents;
	}

	/**
	 * Creates a new Participant if one does not already exist with the given id.
	 * Adds the participant to totalParticipants, currentParticipants, creates a new record and
	 * add them to the starting queue.
	 * @param name - The name of the new Participant.
	 * @param id - The id of the new Participant.
	 */
	public void addParticipantToStart(String name, int id) {
		Participant par = findParticipant(id);

		if (par == null) {
			par = new Participant(name, id);
		}

		addParticipantToStart(par);
	}

	public void createNewRun() {
		currentEvent.newRun();
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
