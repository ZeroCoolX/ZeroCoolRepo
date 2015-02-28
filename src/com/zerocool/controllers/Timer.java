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
			System.out.println("FUCK");
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
