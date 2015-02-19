package com.zerocool.systemcontroller.participant;

public class Participant {

	public Participant() {
		
	}

	public Participant(long id) {
		
	}
	
	public Participant(long id, String name) {
		this(id);
		
	}
	
	
	// ----- functional methods ----- \\
	
	public void createNewRecord() {
		
	}
	
	public Record getLastRecord() {
		
		return null;
	}
	
	public String getFormattedData(long eventID) {
		
		return null;
	}
	
	
	// ----- accessors ----- \\
	
	public long getID() {
		
		return -1;
	}
	
	public int getRecordCount() {
		
		return -1;
	}
	
	public String getName() {
		
		return null;
	}
	
	public boolean getIsCompeting() {
		
		return false;
	}
	
	
	// ----- mutators ----- \\
	
	public void setName(String name) {
		
	}
	
	public void setID(long id) {
		
	}
	
	public void setIsCompeting(boolean isCompeting) {
		
	}
	
}
