package com.zerocool.entities;

public class ParIndividual extends AbstractEvent {
	
	public ParIndividual(String eventName, long eventTime) {
		super();
		this.eventName = eventName + " " + LASTID;
		this.eventTime = eventTime;
		type = EventType.PARIND;
	}

	@Override
	public void triggered(long time, int channel) {
		if (channel % 2 == 0) {
			if (!this.finishChannelsUsed.contains(channel)) {
				finish(time, false);
				this.finishChannelsUsed.add(channel);
			}
		} else {
			if (!this.startChannelsUsed.contains(channel)) {
				start(time);
				this.startChannelsUsed.add(channel);
			}
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
	private void start(long startTime) {
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