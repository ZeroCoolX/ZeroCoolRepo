package com.zerocool.systemcontroller.event;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.systemcontroller.participant.Participant;

public class TestAbstractEvent { 
	
	private ArrayList<AbstractEvent> Events;
	private ArrayList<Participant> partic;
	
    @Before
    public void setUp() throws Exception {
    	Events = new ArrayList<AbstractEvent>();
    	partic = new  ArrayList<Participant>();
    }
    
    public void addEvents(){
    	SimpleDateFormat f = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
    	Date d;
		try {
			d = f.parse("2015/02/17 09:23:50.000");
			System.out.println(d.getTime());
	    	Events.add( new Individual("100M Sprint", AbstractEvent.EventType.IND, d.getTime(), 1111));
	    	Events.add( new Group("CrossCountry Skiiing", AbstractEvent.EventType.GRP, d.getTime(), 2222));
	    	Events.add( new ParIndividual("Marathon", AbstractEvent.EventType.PARIND, d.getTime(), 3333));
	    	Events.add( new ParGroup("Swimming", AbstractEvent.EventType.PARGRP, d.getTime(), 4444));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
    
    public void addParticipants(int numPartic){
    	for(int i = 0; i < numPartic; ++i){
    		partic.add(new Participant(1234, "test"));
    		partic.get(i).createNewRecord();
    	}
    }
    
    public void link(int num){
    	for(int i = 0; i < num; ++i){
    		Events.get(i).setParticipants(partic);
    	}
    }
 
    @After
    public void tearDown() throws Exception {
    	System.out.println("Aaaaaaand we're done here.");
    }
    
    @Test
    public void testEmptyParamConstructor(){
    	Events.add(new Individual());
    	Events.add(new Group());
    	Events.add(new ParIndividual());
    	Events.add(new ParGroup());
    }
    
    @Test
    public void testOneParamConstructor(){
    	Events.add(new Individual("100M Sprint"));
    	System.out.println(Events.get(0).getName());
    	Events.add(new Group("CrossCountry Skiing"));
    	Events.add(new ParIndividual("Marathon"));
    	Events.add(new ParGroup("Swimming"));
    }
    
    @Test
    public void testTwoParamConstructor(){
    	Events.add( new Individual("100M Sprint", AbstractEvent.EventType.IND));
    	Events.add( new Group("CrossCountry Skiiing", AbstractEvent.EventType.GRP));
    	Events.add( new ParIndividual("Marathon", AbstractEvent.EventType.PARIND));
    	Events.add( new ParGroup("Swimming", AbstractEvent.EventType.PARGRP));
    }
    
    @Test
	@SuppressWarnings("deprecation")
    public void testThreeParamConstructor(){
    	SimpleDateFormat f = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
    	Date d;
		try {
			d = f.parse("2015/02/17 09:23:50.000");
	    	Events.add( new Individual("100M Sprint", AbstractEvent.EventType.IND, d.getTime()));
	    	Events.add( new Group("CrossCountry Skiiing", AbstractEvent.EventType.GRP, d.getTime()));
	    	Events.add( new ParIndividual("Marathon", AbstractEvent.EventType.PARIND, d.getTime()));
	    	Events.add( new ParGroup("Swimming", AbstractEvent.EventType.PARGRP, d.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
    
    @Test
	@SuppressWarnings("deprecation")
    public void testFourParamConstructor(){
    	SimpleDateFormat f = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
    	Date d;
		try {
			d = f.parse("2015/02/17 09:23:50.000");
	    	Events.add( new Individual("100M Sprint", AbstractEvent.EventType.IND, d.getTime(), 1111));
	    	Events.add( new Group("CrossCountry Skiiing", AbstractEvent.EventType.GRP, d.getTime(), 2222));
	    	Events.add( new ParIndividual("Marathon", AbstractEvent.EventType.PARIND, d.getTime(), 3333));
	    	Events.add( new ParGroup("Swimming", AbstractEvent.EventType.PARGRP, d.getTime(), 4444));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testGetParticipants(){
    	addEvents();
    	//add participants for testing
    	addParticipants(1);

    	for(AbstractEvent eve: Events){
    		eve.initializeEvent(partic);
    		assertNotNull(eve.getParticipants());
    		assertEquals(1, eve.getParticipants().size());
    	}
    }
    
    @Test
    public void testInitialEvent(){
    	addEvents();
    	//add participants for testing
    	addParticipants(1);	
    	for(AbstractEvent eve: Events){
    		eve.initializeEvent(partic);
    		assertNotNull(eve.getParticipants());
    		assertEquals(1, eve.getParticipants().size());
    		assertNotNull(eve.getParticipants().get(0).getLastRecord().getEventName());
    		assertNotNull(eve.getParticipants().get(0).getLastRecord().getEventID());
    	}
    }
    
    @Test
    public void startParticipant(){
    	addEvents();
    	//add participants for testing
    	addParticipants(1);	
    	for(AbstractEvent eve: Events){
    		eve.initializeEvent(partic);
    		eve.startParticipants();
    		assertTrue(eve.getParticipants().get(0).getIsCompeting());
    		if(eve.getType().equals(AbstractEvent.EventType.IND)){
    			assertEquals(3399,eve.getParticipants().get(0).getLastRecord().getStartTime());
    		}else if(eve.getType().equals(AbstractEvent.EventType.GRP)){
    			assertEquals(2132,eve.getParticipants().get(0).getLastRecord().getStartTime());
    		}else if(eve.getType().equals(AbstractEvent.EventType.PARIND)){
    			assertEquals(3425,eve.getParticipants().get(0).getLastRecord().getStartTime());
    		}else if(eve.getType().equals(AbstractEvent.EventType.PARGRP)){
    			assertEquals(22222,eve.getParticipants().get(0).getLastRecord().getStartTime());
    		}
    	}
    }
    
    @Test
    public void finishParticipant(){
    	addEvents();
    	//add participants for testing
    	addParticipants(1);	
    	for(AbstractEvent eve: Events){
    		eve.initializeEvent(partic);
    		eve.finishParticipants();
    		assertFalse(eve.getParticipants().get(0).getIsCompeting());
    		if(eve.getType().equals(AbstractEvent.EventType.IND)){
    			assertEquals(3,eve.getParticipants().get(0).getLastRecord().getFinishTime());
    		}else if(eve.getType().equals(AbstractEvent.EventType.GRP)){
    			assertEquals(9999,eve.getParticipants().get(0).getLastRecord().getFinishTime());
    		}else if(eve.getType().equals(AbstractEvent.EventType.PARIND)){
        		assertEquals(1123,eve.getParticipants().get(0).getLastRecord().getFinishTime());
    		}else if(eve.getType().equals(AbstractEvent.EventType.PARGRP)){
        		assertEquals(9922,eve.getParticipants().get(0).getLastRecord().getFinishTime());
    		}
    	}
    }
    
}