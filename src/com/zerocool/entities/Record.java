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
	 * Creates a new instance of the Record class without values
	 */
	public Record() {
		elapsedTime = -1;
	}
	
	/**
	 * Create a new instance of the Record class with specified values.
	 * @param eventName - The name of the event.
	 * @param eventId - The ID of the event.
	 */
	public Record(String eventName, int eventId) {
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
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.dnf = dnf;
	}

	
	// ----- Access ----- \\
	
	/**
	 * @return If the participant finished the event
	 */
	public boolean getDnf() {
		return this.dnf;
	}
	
	/**
	 * @return The finish time
	 */
	public long getFinishTime() {
		return this.finishTime;
	}
	
	/**
	 * @return The start time
	 */
	public long getStartTime() {
		return this.startTime;
	}
	
	/**
	 * Gets the time it took to complete the race.
	 * @return The total time from start to finish of the race or -1 if DNF or not finished.
	 */
	public long getElapsedTime() {
		return elapsedTime;
	}
	
	/**
	 * @return The event name this record is for
	 */
	public String getEventName() {
		return this.eventName;
	}
	
	/**
	 * @return The event Id
	 */
	public int getEventId() {
		return this.eventId;
	}

	
	// ----- Mutate ----- \\
	
	/**
	 * Sets the event's name
	 * @param eventName
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	/**
	 * Sets the start time of the participant's record
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Sets the finish time of the participant's record
	 * @param finishTime
	 */
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
		elapsedTime = this.finishTime - this.startTime;
	}
	
	/**
	 * Sets whether the participant finished the event or not
	 * @param dnf
	 */
	public void setDnf(boolean dnf) {
		this.dnf = dnf;
	}
	
	/**
	 * @param eventId
	 */
	public void setEventId(int eventId){
		this.eventId = eventId;
	}
	
	/**
	 * Resets all the variables to 'gracefully exit'.
	 */
	public void exit() {
		System.out.println("exiting rec");
		eventName = null;
		eventId = -1;
		startTime = -1;
		finishTime = -1;
		elapsedTime = -1;
		dnf = false;
		System.out.println("exiting rec");
	}
	
}