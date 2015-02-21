package com.zerocool.systemcontroller.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.zerocool.systemcontroller.participant.Participant;


public class Driver {
	
	@SuppressWarnings("deprecation")
	public static void main (String [] args) throws ParseException{
		
		System.out.println("Testing...testing....");
		AbstractEvent[] events = new AbstractEvent[8];
		//Calendar cal = Calendar.getInstance();
		System.out.println("Testing each constructor: [String], [String, EventType], [String, EventType, Date], [String, EventType, Date, long]");

    	SimpleDateFormat f = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss.lll");
    	Date d;
		events[0] = new Individual("100M Sprint");
		events[1] = new Group("CrossCountry Skiiing", AbstractEvent.EventType.GRP);
		d = f.parse("2015/02/17 09:23:50.000");
		events[2] = new ParIndividual("Marathon", AbstractEvent.EventType.PARIND, d.getTime());
		d = f.parse("2015/02/17 09:25:00.000");
		events[3] = new ParGroup("Swimming", AbstractEvent.EventType.PARGRP, d.getTime(), 9000);	
		System.out.println("Testing empty constructor for each class. Should be type specific");
		events[4] = new Individual();
		events[5] = new Group();
		events[6] = new ParIndividual();
		events[7] = new ParGroup();
		
		 ArrayList<Participant> partic = new  ArrayList<Participant>();
		partic.add(new Participant(1,"Bob"));
		partic.get(0).createNewRecord();
		
		for(AbstractEvent e: events){
			e.initializeEvent(partic);
		}

		for(AbstractEvent eve: events){
			String message = "";
			message += "\nName: "+eve.getName();
			message += "\nType: "+eve.getType();
			message += "\nTime: "+eve.getEventTime();
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
			eve.finishParticipants();
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
