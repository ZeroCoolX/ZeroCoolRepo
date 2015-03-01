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
	private Stack<Record> records;
	
	private int id;
	
	private boolean isCompeting;
	private boolean isNext;
	
	/**
	 * Creates a new instance of the Participant class
	 * @param id
	 * @param name
	 */
	public Participant(int id, String name) {
		this.id = id;
		this.name = name;
		this.records = new Stack<Record>();
	}
	
	
	// ----- Functional ----- \\
	
	/**
	 * Creates a new {@link #Record} for this {@link #Participant}.
	 */
	public void createNewRecord() {
		records.push(new Record());
	}
	
	/**
	 * Overload createNewRecord to simplify implementation later on.  (In event class!)
	 * @param eventName - The name of the event.
	 * @param eventId - The ID of the event.
	 */
	public void createNewRecord(String eventName, int eventId) {
		records.push(new Record(eventName, eventId));
	}
	
	// ----- Access ----- \\
	
	/**
	 * Returns the last {@link #Record} created for this {@link #Participant}.
	 * If no {@link #Record} exists then it returns null
	 * @return The last created {@link #Record}
	 */
	public Record getLastRecord() {
		return this.records.isEmpty() ? null : this.records.peek();
	}
	
	/**
	 * Returns the formatted data for the participant's most recent
	 * Event
	 * Format - <Event ID> <Participant Number> <Elapsed Time>
	 * @param eventID
	 * @return a empty string if no data for the event ID.  Otherwise
	 * a string of event data in the format above
	 */
	public String getFormattedData(int eventID) {
		Record eventRecord = null;
		String result = "";
		
		for(Record r : this.records) {
			if(r.getEventID() == eventID) eventRecord = r;
		}
		
		if(eventRecord != null){
			result = "" + eventID + "     " + this.id + "    " + SystemTime.formatTime(eventRecord.getElapsedTime());
		}
		
		return result;
	}

	/**
	 * Get's the ID of the particiapnt.
	 * @return The id of the participant.
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Returns the number of Records that this {@link #Participant} has.
	 * @return The number of records.
	 */
	public int getRecordCount() {
		return this.records.size();
	}
	
	/**
	 * Returns the {@link #Participant}'s name.
	 * @return The name of the participant.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get if this {@link #Participant} is competing.
	 * @return true if in an event else false.
	 */
	public boolean getIsCompeting() {
		return this.isCompeting;
	}
	
	public boolean getIsNext(){
		return this.isNext;
	}
	
	
	// ----- Mutate ----- \\\	
	
	/**
	 * Sets if this {@link #Participant} is competing in an {@link #Event}.
	 * @param isCompeting - True if participant is in an event else false.
	 */
	public void setIsCompeting(boolean isCompeting) {
		this.isCompeting = isCompeting;
	}
	
	public void setIsNext(boolean next){
		isNext = next;
	}
	
	
	public void exit(){
		System.out.println("exiting par");
		name = null;
		id = -1;
		isCompeting = false;
		isNext = false;
		while(!records.isEmpty()){
			records.pop().exit();
		}
		System.out.println("exiting par");
	}
}