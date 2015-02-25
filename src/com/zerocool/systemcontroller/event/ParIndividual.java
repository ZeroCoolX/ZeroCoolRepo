package com.zerocool.systemcontroller.event;

import java.util.ArrayList;

import com.zerocool.systemcontroller.participant.Participant;

public class ParIndividual extends AbstractEvent {

	public ParIndividual(String eventName, long eventTime) {
		super();
		this.eventName = eventName;
		this.eventTime = eventTime;
	}
	
	public ParIndividual(String eventName, long eventTime, ArrayList<Participant> participants) {
		this(eventName, eventTime);
		initializeEvent(participants);
	}

	@Override
	public void initializeEvent(ArrayList<Participant> participants) {
		// must check for null parameter
		if (participants == null) {
			throw new IllegalArgumentException("List of participants can't be null!");
		}

		currentParticipants = participants;

		// go through each participant and set their eventId and event name
		for (Participant curPar : currentParticipants) {
			curPar.createNewRecord(eventName, eventId);
		}
	}

	@Override
	public void startAllParticipants(long startTime) {
		System.out.println("Starting ParIndividual Participants");
		// go through each participant and set the start time
		for (Participant curPar : currentParticipants) {
			curPar.setIsCompeting(true);
			curPar.getLastRecord().setStartTime(startTime);
		}
	}

	@Override
	public void startOneParticipant(Participant participant, long startTime) {
		participant.setIsCompeting(true);
		participant.getLastRecord().setStartTime(startTime);
	}
	
	@Override
	public void finishAllParticipants(long finishTime) {
		System.out.println("Finishing ParIndividual Participants");
		for (Participant curPar : currentParticipants) {
			curPar.setIsCompeting(false);
			curPar.getLastRecord().setFinishTime(finishTime);
		}
	}

	@Override
	public void finishOneParticipant(Participant participant, long finishTime) {
		participant.setIsCompeting(false);
		participant.getLastRecord().setFinishTime(finishTime);
	}

}