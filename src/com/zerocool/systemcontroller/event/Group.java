package com.zerocool.systemcontroller.event;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.zerocool.systemcontroller.participant.Participant;

public class Group extends AbstractEvent {
	
	private EventType type;
	private String name;
	private Participant [] currentParticipants;
	//dateFormat.format(date) prints (example)   2014/08/06 15:59:48
	private DateFormat eventTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	//eventTime stored the entire date but the specific miliseconds, seconds, minutes, hours..etc can be accessed from such
	private Date eventTime;
	private long eventId;
	
	public Group(){
		super();
	}

	public Group(String name){
		super(name, EventType.GRP);
	}

	public Group(String name, EventType type){
		super(name, type, new Date());
	}
	
	public Group(String name, EventType type, Date eventTime){
		super(name, type, eventTime, -1);
	}
	
	public Group(String name, EventType type, Date eventTime, long eventId){
		super(name, type, eventTime, eventId);
	}
	
	void initializeEvent(Participant[] participants){
		currentParticipants = participants;
	}
	
	void startParticipant(){
		System.out.println("Starting Group Participants");
	}
	
	void startParticipants(){
		
	}
	
	void finishParticipant(){
		System.out.println("Finishing Group Participants");
	}
	
}