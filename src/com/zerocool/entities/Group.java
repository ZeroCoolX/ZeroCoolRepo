package com.zerocool.entities;

public class Group extends AbstractEvent {

	public Group(String eventName, long eventTime) {
		super();
		this.eventName = eventName;
		this.eventTime = eventTime;
		type = EventType.GRP;
	}
	
	// Override for more method functionality.
	@Override
	public void startNextParticipant(long startTime) {
		super.startNextParticipant(startTime);
		Participant par = startingQueue.poll();
		addCompetingParticipant(par);
		par.getLastRecord().setStartTime(startTime);
		
	}

	// Overriding for more method functionality.
	@Override
	public void finishParticipant(Participant participant, long finishTime, boolean setDNF) {
		super.finishParticipant(participant, finishTime, setDNF);
		participant.setIsCompeting(false);
		competingParticipants.remove(participant);
		participant.getLastRecord().setFinishTime(setDNF ? -1 : finishTime);
		participant.getLastRecord().setDnf(setDNF);
	}
	
	/**
	 * Override for more method functionality.  Although there are no new variables, possibly later.
	 * Exits gracefully.
	 */
	public void exit() {
		super.exit();
	}

}