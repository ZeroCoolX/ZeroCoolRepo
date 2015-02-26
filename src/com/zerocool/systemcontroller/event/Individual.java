package com.zerocool.systemcontroller.event;

import java.util.ArrayList;

import com.zerocool.systemcontroller.participant.Participant;

public class Individual extends AbstractEvent {

	public Individual(String eventName, long eventTime) {
		super();
		this.eventName = eventName;
		this.eventTime = eventTime;
	}
	
	public Individual(String eventName, long eventTime, ArrayList<Participant> participants) {
		this(eventName, eventTime);
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
		// go through each participant and set the start time
		competingPar = startingQueue.remove();
		competingPar.setIsCompeting(true);
		competingPar.getLastRecord().setStartTime(startTime);
	}

	@Override
	public void startOneParticipant(Participant participant, long startTime) {
		participant.setIsCompeting(true);
		participant.getLastRecord().setStartTime(startTime);
	}

	@Override
	public void finishAllParticipants(long finishTime) {
		competingPar.setIsCompeting(false);
		competingPar.getLastRecord().setFinishTime(finishTime);
	}

	@Override
	public void finishOneParticipant(Participant participant, long finishTime) {
		participant.setIsCompeting(false);
		participant.getLastRecord().setFinishTime(finishTime);
	}

}
