package com.zerocool.entities;

import java.util.*;

import com.zerocool.services.SystemTime;
/**
 * @author ZeroCool
 * The Participant Class
 * 
 * This class is the representation of a 
 * {@link #Participant} in any timed Event
 */
public class Participant {
	
	private String name;
	private ArrayList<Record> records;
	
	private int id;
	
	private boolean isCompeting;
	
	/**
	 * Creates a new instance of the Participant class
	 * @param id
	 * @param name
	 */
	public Participant(String name, int id) {
		this.name = name;
		this.id = id;
		records = new ArrayList<Record>();
	}
	
	
	// ----- Functional ----- \\
	
	/**
	 * Creates a new {@link #Record} for this {@link #Participant}.
	 */
	public void createNewRecord() {
		records.add(new Record());
	}
	
	/**
	 * Overload createNewRecord to simplify implementation later on.  (In event class!)
	 * @param eventName - The name of the event.
	 * @param eventId - The ID of the event.
	 */
	public void createNewRecord(String eventName, int eventId) {
		records.add(new Record(eventName, eventId));
	}
	
	// ----- Access ----- \\
	
	/**
	 * Returns the last {@link #Record} created for this {@link #Participant}.
	 * If no {@link #Record} exists then it returns null
	 * @return The last created {@link #Record}
	 * @throws IllegalArgumentException - If recordNum is below 0 or greater than
	 * 	the number of records.
	 */
	public Record getLastRecord() {
		return getRecord(records.size() - 1);
	}
	
	/**
	 * Returns the {@link #Record} at a specific index created for this {@link #Participant}.
	 * If no {@link #Record} exists then it returns null
	 * @return The last created {@link #Record}
	 * @throws IllegalArgumentException - If recordNum is below 0 or greater than
	 * 	the number of records.
	 */
	public Record getRecord(int recordNum) {
		if (recordNum < 0 || recordNum >= records.size()) {
			throw new IllegalArgumentException("There is no record at " + recordNum + ".");
		}
		
		return records.isEmpty() ? null : records.get(recordNum);
	}
	
	/**
	 * Returns the formatted data for the participant's most recent
	 * Event
	 * Format - <Event ID> <Participant Number> <Elapsed Time>
	 * @param eventID
	 * @return a empty string if no data for the event ID.  Otherwise
	 * a string of event data in the format above
	 */
	public String getFormattedData() {
		Record rec = this.getLastRecord();
		return rec.getEventId() + "\t" + id + "\t" + (rec.getDnf() ? "DNF" : SystemTime.formatTime(rec.getElapsedTime()));
	}
	
	/**
	 * Returns the formatted data for the participant's most recent
	 * Event
	 * Format - <Event ID> <Participant Number> <Elapsed Time>
	 * @param eventID
	 * @return a empty string if no data for the event ID.  Otherwise
	 * a string of event data in the format above
	 */
	public String getElapsedFormattedData(SystemTime systime) {
		Record rec = this.getLastRecord();
		return "\t" + (rec.getDnf() ? "DNF" : SystemTime.formatTime(rec.getElapsedTime(systime)));
	}

	/**
	 * Get's the ID of the participant.
	 * @return The id of the participant.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the number of Records that this {@link #Participant} has.
	 * @return The number of records.
	 */
	public int getRecordCount() {
		return records.size();
	}
	
	/**
	 * Returns the {@link #Participant}'s name.
	 * @return The name of the participant.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get if this {@link #Participant} is competing.
	 * @return true if in an event else false.
	 */
	public boolean getIsCompeting() {
		return isCompeting;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof Participant && id == ((Participant) other).id;
	}
	
	
	// ----- Mutate ----- \\\	
	
	/**
	 * Sets if this {@link #Participant} is competing in an {@link #Event}.
	 * @param isCompeting - True if participant is in an event else false.
	 */
	public void setIsCompeting(boolean isCompeting) {
		this.isCompeting = isCompeting;
	}
	
	/**
	 * Used so the system can exit "gracefully"
	 */
	public void exit() {
		System.out.println("exiting par");
		
		name = null;
		id = -1;
		isCompeting = false;
		
		for (int i = 0; i < records.size(); ++i) {
			records.get(i).exit();
		}
		
		System.out.println("exiting par");
	}
}