package com.zerocool.entities;

/**
 * @author ZeroCool
 * The Record Class.
 * 
 * This class creates a record of a participant's
 * performance in one event.
 */
public class Record {

	private String eventName;
	
	private int eventId;
	
	private long startTime;
	private long finishTime;
	private long elapsedTime;
	
	private boolean dnf;
	
	/**
	 * Creates a new instance of the Record class without values.
	 */
	public Record() {
		eventId = -1;
		startTime = -1;
		finishTime = -1;
		elapsedTime = -1;
	}
	
	/**
	 * Create a new instance of the Record class with specified values.
	 * @param eventName - The name of the event.
	 * @param eventId - The ID of the event.
	 */
	public Record(String eventName, int eventId) {
		this();
		this.eventName = eventName;
		this.eventId = eventId;
	}
	
	/**
	 * Creates a new instance of the Record class with values
	 * @param startTime The participant's start time
	 * @param finishTime The participant's finish time
	 * @param dnf If the participant did not finish
	 * @param eventId The event's id number
	 * @param eventName The name of the event
	 */
	public Record(String eventName, int eventId, long startTime, long finishTime, boolean dnf) {
		this(eventName, eventId);
		setStartTime(startTime);
		setFinishTime(finishTime);
		this.dnf = dnf;
	}

	
	// ----- Access ----- \\
	
	/**
	 * Checks whether the Participant finished the race er naw.
	 * @return - True if not finished else false.
	 */
	public boolean getDnf() {
		return this.dnf;
	}
	
	/**
	 * Gets the finish time.
	 * @return - -1 if the race was not finished else the finish time.
	 */
	public long getFinishTime() {
		return dnf ? -1 : finishTime;
	}
	
	/**
	 * Gets the start time of the race.
	 * @return - The start time.
	 */
	public long getStartTime() {
		return this.startTime;
	}
	
	/**
	 * Gets the time it took to complete the race.
	 * @return - -1 if the race was not finished or finish time was not entered yet else the
	 * 	total time from start to finish.
	 */
	public long getElapsedTime() {
		return dnf ? -1 : elapsedTime;
	}
	
	
	/**
	 * Gets the name of the Event this record is for.
	 * @return - The event name of the record.
	 */
	public String getEventName() {
		return this.eventName;
	}
	
	/**
	 * Gets the id of the event this record is for.
	 * @return - The event id of the record.
	 */
	public int getEventId() {
		return this.eventId;
	}

	
	// ----- Mutate ----- \\
	
	/**
	 * Sets the name of the Event this record is for.
	 * @param eventName - The name of the event.
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	/**
	 * Sets the start time of the race.  The start time can't be negative and
	 * if there was a finish time set it can't be greater than it.
	 * @param startTime - The time to set the start time to.
	 * @throws IllegalArgumentException - Start time is negative or greater than finish time.
	 */
	public void setStartTime(long startTime) {
		if (startTime < 0 || (finishTime > -1 && startTime > finishTime)) {
			throw new IllegalArgumentException("Start Time (" + startTime + ") can't be negative or greater than"
					+ " the Finish Time (" + finishTime + ").");
		}
		
		this.startTime = startTime;
		elapsedTime = finishTime > -1 ? (finishTime - startTime) : elapsedTime;
	}
	
	/**
	 * Sets the finish time of the race.  The finish time can't be negative and
	 * finish time has to be greater than the start time.
	 * @param finishTime - The time to set the finish time to.
	 * @throws IllegalArgumentException - Finish time is negative or smaller than start time.
	 */
	public void setFinishTime(long finishTime) {
		if (finishTime < 0 || finishTime < startTime) {
			throw new IllegalArgumentException("Finish Time (" + finishTime + ") can't be negative or less "
					+ "than the Start Time (" + startTime + ").");
		}
		
		this.finishTime = finishTime;
		elapsedTime = startTime > -1 ? (finishTime - startTime) : elapsedTime;
	}
	
	/**
	 * Sets whether the participant finished the event or not.
	 * @param dnf - True if not finished else false if finished (default).
	 */
	public void setDnf(boolean dnf) {
		this.dnf = dnf;
	}
	
	/**
	 * Sets the id of the event.
	 * @param eventId - The id to set the event id to.
	 */
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	
	/**
	 * Resets all the variables to 'gracefully exit'.
	 */
	public void exit() {
		eventName = null;
		eventId = -1;
		startTime = -1;
		finishTime = -1;
		elapsedTime = -1;
		dnf = false;
	}
	
}