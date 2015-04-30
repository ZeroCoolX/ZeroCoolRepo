package com.zerocool.entities;

public class Individual extends AbstractEvent {

	public Individual(String eventName, long eventTime) {
		super();
		this.eventName = eventName + " " + LASTID;
		this.eventTime = eventTime;
		type = EventType.IND;
	}

	/**
	 * If there are not Participants in the running queue (meaning no one is running),
	 * then it starts the next Participant from the starting queue otherwise it
	 * finishes the next Participant from the running queue.
	 */
	@Override
	public void triggered(long time, int channel) throws IllegalStateException {
		if (channel % 2 == 0) {
			finish(time, false);
		} else {
			start(time);
		}
	}

	/**
	 * Sets the next Participant in the running queue to 'Did Not Finish'.
	 */
	@Override
	public void setDnf(long time) {
		while (!runningQueue.isEmpty()) {
			finish(time, true);
		}
	}
	
	/**
	 * Takes the first Participant from the starting queue and starts them.
	 * @param startTime - The time the Participant started the race.
	 */
	private void start(long startTime) throws IllegalStateException {
		startParticipant(startTime);
	}

	/**
	 * Takes the first Participant from the running queue and finishes them.
	 * @param finishTime - The time the Participant finished the race.
	 * @param setDNF - True to set 'Did Not Finish' else false.
	 */
	private void finish(long finishTime, boolean setDnf) {
		finishParticipant(finishTime, setDnf);
	}
	
	/**
	 * Override for more method functionality.  
	 * Although there are no new variables, possibly later.  Exits gracefully.
	 */
	public void exit() {
		super.exit();
	}
}
