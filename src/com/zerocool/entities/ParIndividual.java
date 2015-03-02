package com.zerocool.entities;

public class ParIndividual extends AbstractEvent {

	public ParIndividual(String eventName, long eventTime) {
		super();
		this.eventName = eventName;
		this.eventTime = eventTime;
		type = EventType.PARIND;
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
	public void finishParticipant(Participant participant, long finishTime, boolean dnf) {
		super.finishParticipant(participant, finishTime, dnf);
		participant.setIsCompeting(false);
		competingParticipants.remove(participant);
		participant.getLastRecord().setFinishTime(finishTime);
		if(dnf){
			participant.getLastRecord().setDnf(true);
		}else{
			participant.getLastRecord().setDnf(false);
		}
	}
	
	/**
	 * Override for more method functionality.  Although there are no new variables, possibly later.
	 * Exits gracefully.
	 */
	public void exit() {
		super.exit();
	}

}