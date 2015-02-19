package com.zerocool.systemcontroller.event;

import java.util.Calendar;
import java.util.Date;

import com.zerocool.systemcontroller.participant.Participant;


public class Driver {
	
	@SuppressWarnings("deprecation")
	public static void main (String [] args){
		
		System.out.println("Testing...testing....");
		AbstractEvent[] events = new AbstractEvent[8];
		//Calendar cal = Calendar.getInstance();
		System.out.println("Testing each constructor: [String], [String, EventType], [String, EventType, Date], [String, EventType, Date, long]");
		events[0] = new Individual("100M Sprint");
		events[1] = new Group("CrossCountry Skiiing", AbstractEvent.EventType.GRP);
		events[2] = new ParIndividual("Marathon", AbstractEvent.EventType.PARIND, new Date(2015, 2, 17, 9, 23, 50));
		events[3] = new ParGroup("Swimming", AbstractEvent.EventType.PARGRP, new Date(2015, 2, 17, 9, 25, 00), 9000);	
		System.out.println("Testing empty constructor for each class. Should be type specific");
		events[4] = new Individual();
		events[5] = new Group();
		events[6] = new ParIndividual();
		events[7] = new ParGroup();
		
		Participant[] partic = new Participant[1];
		partic[0] = new Participant();
		partic[0].createNewRecord();
		
		for(AbstractEvent e: events){
			e.initializeEvent(partic);
		}

		for(AbstractEvent eve: events){
			String message = "";
			message += "\nName: "+eve.getName();
			message += "\nType: "+eve.getType();
			message += "\nTime: "+eve.getEventTime().toString();
			message += "\nId: "+eve.getEventId();
			System.out.println(message);
			message = "";
			eve.startParticipants();
			for(Participant par: eve.getParticipants()){
				message += "\nEvent Name: "+par.getLastRecord().getEventName();
				message += "\nEvent ID: "+par.getLastRecord().getEventID();
				message += "\nStart Time: "+par.getLastRecord().getStartTime();
				message += "\nisCompeting: "+par.getIsCompeting();
				System.out.println(message);
			}
			message = "";
			eve.finishParticipant();
			for(Participant par: eve.getParticipants()){
				message += "\nEvent Name: "+par.getLastRecord().getEventName();
				message += "\nEvent ID: "+par.getLastRecord().getEventID();
				message += "\nFinish Time: "+par.getLastRecord().getFinishTime();
				message += "\nisCompeting: "+par.getIsCompeting();
				System.out.println(message);
			}
		}
	}
	

}
