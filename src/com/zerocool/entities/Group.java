package com.zerocool.entities;

public class Group extends AbstractEvent {

	public Group(String eventName, long eventTime) {
		super();
		this.eventName = eventName;
		this.eventTime = eventTime;
		type = EventType.GRP;
	}
	
	/**
	 * Overriding for more method functionality.
	 * This should start all the Participants in the starting queue by
	 * adding them to the competing list and setting their start time.
	 */
	@Override
	public void start(long startTime) {
		super.start(startTime);
		
		Participant par;
		while (!startingQueue.isEmpty()) {
			par = startingQueue.poll();
			addCompetingParticipant(par);
			par.getLastRecord().setStartTime(startTime);
		}
	}

	/**
	 *  Overriding for more method functionality.
	 *  Sets a participant to 'Finished' by setting their competing to false,
	 *  removed them from CompetingParticipants, adds their finish time and
	 *  sets their DNF.
	 */
	@Override
	public void finish(Participant participant, long finishTime, boolean setDNF) {
		super.finish(participant, finishTime, setDNF);
		participant.setIsCompeting(false);
		competingParticipants.remove(participant);
		participant.getLastRecord().setFinishTime(finishTime);
		participant.getLastRecord().setDnf(setDNF);
	}
	
	/**
	 * Override for more method functionality.  
	 * Although there are no new variables, possibly later.  Exits gracefully.
	 */
	public void exit() {
		super.exit();
	}

}