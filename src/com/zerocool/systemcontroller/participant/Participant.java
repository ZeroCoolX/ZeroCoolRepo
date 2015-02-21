package com.zerocool.systemcontroller.participant;

import java.util.*;
/**
 * @author ZeroCool
 * The Participant Class
 * 
 * This class is the representation of a 
 * {@link #Participant} in any timed Event
 */
public class Participant {
	private long id;
	private String name;
	private Stack<Record> records;
	private boolean isCompeting;
	
	/**
	 * Creates a new instance of the Participant class
	 * @param id
	 * @param name
	 */
	public Participant(long id, String name) {
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
	
	// ----- Access ----- \\
	/**
	 * Returns the last {@link #Record} created for this {@link #Participant}.
	 * If no {@link #Record} exists then it returns null
	 * @return The last created {@link #Record}
	 */
	public Record getLastRecord() {
		return this.records.isEmpty() ? null : this.records.peek();
	}
	
	public String getFormattedData(long eventID) {
		return null;
	}

	/**
	 * @return
	 */
	public long getID() {
		return this.id;
	}
	
	/**
	 * Returns the number of Records that this {@link #Participant} has.
	 * @return int
	 */
	public int getRecordCount() {
		return this.records.size();
	}
	
	/**
	 * Returns the {@link #Participant}'s name.
	 * @return string
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get if this {@link #Participant} is competing
	 * @return
	 */
	public boolean getIsCompeting() {
		return this.isCompeting;
	}
	
	
	// ----- Mutate ----- \\\	
	/**
	 * Sets if this {@link #Participant} is competing in an {@link #Event}
	 * @param isCompeting
	 */
	public void setIsCompeting(boolean isCompeting) {
		this.isCompeting = isCompeting;
	}
	
}
