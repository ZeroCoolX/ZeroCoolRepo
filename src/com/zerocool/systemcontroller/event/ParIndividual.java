package com.zerocool.systemcontroller.event;

import java.util.ArrayList;

import com.zerocool.systemcontroller.participant.Participant;

public class ParIndividual extends AbstractEvent {

	public ParIndividual() {
		super();
		type = EventType.PARIND;
	}

	public ParIndividual(String name) {
		this();
		this.name = name;
	}

	public ParIndividual(String name, long eventTime) {
		this(name);
		this.eventTime = eventTime;
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
			curPar.createNewRecord(name, eventId);
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