package com.zerocool.entities;

public class ParIndividual extends AbstractEvent {

	public ParIndividual(String eventName, long eventTime) {
		super();
		this.eventName = eventName;
		this.eventTime = eventTime;
		type = EventType.PARIND;
	}

	@Override
	public void triggered(long time, int channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDnf(long time) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 *  Overriding for more method functionality.
	 *  Grabs first participant from the startingQueue, adds them to the
	 *  competing list and sets their start time.
	 */
//	@Override
//	public void start(long startTime) {
//		super.start(startTime);
//		Participant par = startingQueue.poll();
//		addCompetingParticipant(par);
//		par.getLastRecord().setStartTime(startTime);
//		
//	}

	/**
	 *  Overriding for more method functionality.
	 *  Sets a participant to 'Finished' by setting their competing to false,
	 *  removed them from CompetingParticipants, adds their finish time and
	 *  sets their DNF.
	 */
//	@Override
//	public void finish(Participant participant, long finishTime, boolean setDNF) {
//		super.finish(participant, finishTime, setDNF);
//		participant.setIsCompeting(false);
//		competingParticipants.remove(participant);
//		participant.getLastRecord().setFinishTime(finishTime);
//		participant.getLastRecord().setDnf(setDNF);
//	}
	
	/**
	 * Override for more method functionality.  
	 * Although there are no new variables, possibly later.  Exits gracefully.
	 */
	public void exit() {
		super.exit();
	}

}