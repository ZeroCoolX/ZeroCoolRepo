package com.zerocool.systemcontroller.event;
import java.util.Calendar;
import java.util.Date;


public class Driver {
	
	@SuppressWarnings("deprecation")
	public static void main (String [] args){
		
		System.out.println("Testing...testing....");
		AbstractEvent[] events = new AbstractEvent[4];
		//Calendar cal = Calendar.getInstance();
		events[0] = new Individual("100M Sprint");
		events[1] = new Group("CrossCountry Skiiing", AbstractEvent.EventType.GRP);
		events[2] = new ParIndividual("Marathon", AbstractEvent.EventType.PARIND, new Date(2015, 2, 17, 9, 23, 50));
		events[3] = new ParGroup("Swimming", AbstractEvent.EventType.PARGRP, new Date(2015, 2, 17, 9, 25, 00), 9000);	

		for(AbstractEvent eve: events){
			String message = "";
			message += "\nName: "+eve.getName();
			message += "\nType: "+eve.getType();
			message += "\nTime: "+eve.getEventTime().toString();
			message += "\nId: "+eve.getEventId();
			System.out.println(message);
			eve.startParticipant();
			eve.finishParticipant();
		}
	}
	
	// testing

}
