package com.zerocool.systemcontroller.timer;

import java.util.ArrayList;

import com.zerocool.systemcontroller.event.AbstractEvent;
import com.zerocool.systemcontroller.event.AbstractEvent.EventType;
import com.zerocool.systemcontroller.event.Group;
import com.zerocool.systemcontroller.event.Individual;
import com.zerocool.systemcontroller.event.ParGroup;
import com.zerocool.systemcontroller.event.ParIndividual;
import com.zerocool.systemcontroller.participant.Participant;
import com.zerocool.systemcontroller.systemtime.SystemTime;

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
	
	public void startEvent() {
		currentEvent.initializeEvent();
		currentEvent.startAllParticipants(systemTime.getTime());
	}
	
	public void endEvent() { 
		currentEvent.finishAllParticipants(systemTime.getTime());
	}
	
	
	// ----- accessors ----- \\
	
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
	
	public void createEvent(EventType type, String eventName, ArrayList<Participant> participants) {
		createEvent(type, eventName);
		addToTotal(participants);
	}

	public void addNewParticipant(int participant) {
		Participant newPar = new Participant(participant, "" + participant);
		totalParticipants.add(newPar);
		currentEvent.addNewParticipant(newPar);
	}
	
	
	// ----- private methods ----- \\
	
	private void addToTotal(ArrayList<Participant> participants) {
		for (Participant par : participants) {
			if (!totalParticipants.contains(par)) {
				totalParticipants.add(par);
			}
		}
	}
	
	private void addToTotal(Participant participant) {
		if (!totalParticipants.contains(participant)) {
			totalParticipants.add(participant);
		}
	}
	
	
	/**
	 * Exits gracefully.
	 */
	public void exit() {
		System.out.println("exiting timer");
		for (Participant par: totalParticipants) {
			par.exit();
		}
		currentEvent.exit();
		System.out.println("exiting timer");
	}
	
}
