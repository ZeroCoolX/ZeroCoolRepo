package com.zerocool.entities;

public class Group extends AbstractEvent {

	public Group(String eventName, long eventTime) {
		super();
		this.eventName = eventName;
		this.eventTime = eventTime;
		type = EventType.GRP;
	}
	
	/**
	 * If there are not Participants in the running queue (meaning no one is running),
	 * then it starts all the Participants from the starting queue otherwise it
	 * finishes the next Participant from the running queue.
	 */
	@Override
	public void triggered(long time, int channel) {
		System.out.println("GRP triggered");
		if (runningQueue.isEmpty()) {
			System.out.println("start");
			start(time);
		} else {
			System.out.println("finish");
			finish(time);
		}
	}

	/**
	 * Sets the next Participant in the running queue to 'Did Not Finish'.
	 */
	@Override
	public void setDnf(long time) {
		finishParticipant(time, true);
	}
	
	/**
	 * Loops through all the Participants in the starting queue and starts them.
	 * @param startTime - The time the Participants started.
	 */
	private void start(long startTime) {
		while(!startingQueue.isEmpty()) {
			startParticipant(startTime);
		}
	}
	
	/**
	 * Takes the first Participant from the running queue and finishes them.
	 * @param finishTime - The time the Participant finished the race.
	 * @param setDNF - True to set 'Did Not Finish' else false.
	 */
	private void finish(long finishTime) {
		finishParticipant(finishTime, false);
	}
	
	/**
	 * Override for more method functionality.  
	 * Although there are no new variables, possibly later.  Exits gracefully.
	 */
	public void exit() {
		super.exit();
	}

}