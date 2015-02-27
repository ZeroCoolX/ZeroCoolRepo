package com.zerocool.tests;

import java.io.File;
import java.text.ParseException;

import com.zerocool.controllers.SystemController;


public class Driver {
	
	public static void main (String [] args) throws ParseException{
		
		System.out.println("Testing...testing....\n\n");
		
		SystemController sysCont = new SystemController();
		File file = new File("test_files/syscontrolFastFileTest.txt");
		//File file = new File("/Users/TheHerbaliser/Documents/workspace/syscontrolFileTest2.txt");
		System.out.println("\n\nTesting readFile\n\n");
		sysCont.readFile(file);
		System.out.println("\n\nTesting readInput\n\n");
		//sysCont.readInput();
		
		/*AbstractEvent[] events = new AbstractEvent[8];
		//Calendar cal = Calendar.getInstance();
		System.out.println("Testing each constructor: [String], [String, EventType], [String, EventType, Date], [String, EventType, Date, long]");

    	SimpleDateFormat f = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
    	Date d = new Date();
		events[0] = new Individual("100M Sprint");
		events[1] = new Group("CrossCountry Skiiing", AbstractEvent.EventType.GRP);
		d = f.parse( (""+(d.getYear()+1900)+"/"+(d.getMonth()+1)+"/"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds()+".000") );
		System.out.println("TIME2: " + d);
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
		}*/
	}
	

}
