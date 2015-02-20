package com.zerocool.systemcontroller.event;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractEvent { 
	
	private ArrayList<AbstractEvent> Events;
   
    @Before
    public void setUp() throws Exception {
    	Events = new ArrayList<AbstractEvent>();
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
    	Events.add( new Individual("100M Sprint", AbstractEvent.EventType.IND, new Date(2015, 2, 17, 9, 23, 50)));
    	Events.add( new Group("CrossCountry Skiiing", AbstractEvent.EventType.GRP, new Date(2015, 2, 17, 9, 23, 50)));
    	Events.add( new ParIndividual("Marathon", AbstractEvent.EventType.PARIND, new Date(2015, 2, 17, 9, 23, 50)));
    	Events.add( new ParGroup("Swimming", AbstractEvent.EventType.PARGRP, new Date(2015, 2, 17, 9, 23, 50)));
    }
    
    @Test
	@SuppressWarnings("deprecation")
    public void testFourParamConstructor(){
    	Events.add( new Individual("100M Sprint", AbstractEvent.EventType.IND, new Date(2015, 2, 17, 9, 23, 50), 1111));
    	Events.add( new Group("CrossCountry Skiiing", AbstractEvent.EventType.GRP, new Date(2015, 2, 17, 9, 23, 50), 2222));
    	Events.add( new ParIndividual("Marathon", AbstractEvent.EventType.PARIND, new Date(2015, 2, 17, 9, 23, 50), 3333));
    	Events.add( new ParGroup("Swimming", AbstractEvent.EventType.PARGRP, new Date(2015, 2, 17, 9, 23, 50), 4444));
    }
    
    @Test
    public void testFullEvent(){
    	Events.add( new Individual("100M Sprint", AbstractEvent.EventType.IND, new Date(2015, 2, 17, 9, 23, 50), 1111));
    	Events.add( new Group("CrossCountry Skiiing", AbstractEvent.EventType.GRP, new Date(2015, 2, 17, 9, 23, 50), 2222));
    	Events.add( new ParIndividual("Marathon", AbstractEvent.EventType.PARIND, new Date(2015, 2, 17, 9, 23, 50), 3333));
    	Events.add( new ParGroup("Swimming", AbstractEvent.EventType.PARGRP, new Date(2015, 2, 17, 9, 23, 50), 4444));
    }
    
}