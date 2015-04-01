package com.zerocool.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.Individual;
import com.zerocool.entities.Participant;
import com.zerocool.services.SystemTime;

public class AbstractEventTests {

	AbstractEvent event;
	SystemTime time;
	
	@Rule
	ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		// Have to test on Individual since 
		// cannot instantiate AbstractEvent
		time = new SystemTime();
		event = new Individual("Event Name", time.getTime());
	}

	@After
	public void tearDown() throws Exception {
		time = null;
		event = null;
	}

	@Test
	public void testResetEventId() {
		event.resetEventId();
		assertEquals(0, event.getEventId());
	}

	@Test
	public void testSetName() {
		String s = "New Name";
		event.setName(s);
		assertEquals(s, event.getEventName());
	}
	
	@Test
	public void testSetNameEmptyString() {
		String s = "";
		event.setName(s);
		assertEquals(s, event.getEventName());
	}

	@Test
	public void testSetEventTime() {
		long timeNow = time.getTime();
		event.setEventTime(timeNow);
		assertEquals(timeNow, event.getEventTime());
	}

	@Test
	public void testAddParticipantNull() {
		exception.expect(IllegalArgumentException.class);
		event.addParticipant(null);
	}

	@Test
	public void testAddParticipantAlreadyInQueue() {
		Participant participant = new Participant("Name", 8);
		event.addParticipant(participant);
		
		exception.expect(IllegalArgumentException.class);
		event.addParticipant(participant);
	}
	
	@Test
	public void testAddParticipant() {
		Participant participant = new Participant("Name", 115);
		event.addParticipant(participant);
		assertEquals(participant, event.getCurrentParticipants().get(0));
		assertEquals(participant, event.getStartingQueue().peek());
	}
	
	@Test
	public void testResetEvent() {
		// Ensure reset affects participants in the start queue
		Participant number1 = new Participant("One",1);
		Participant number2 = new Participant("two",2);
		Participant number3 = new Participant("three",3);
		
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(number1);
		participants.add(number2);
		participants.add(number3);
		
		for (Participant p: participants) {
			event.addParticipant(p);
		}
		
		event.resetEvent();
		
		// All participants should be in the new start queue
		assertEquals(3, event.getStartingQueue().size());
		
	}

	@Test
	public void testGetEventName() {
		String name = "Some name";
		event.setName(name);
		assertEquals(name, event.getEventName());
	}

	@Test
	public void testGetType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEventTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFormattedEventTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEventId() {
		assertEquals(1, event.getEventId());
	}

	@Test
	public void testGetCurrentParticipants() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStartingQueue() {
		Participant number1 = new Participant("One",1);
		Participant number2 = new Participant("two",2);
		Participant number3 = new Participant("three",3);
		Participant number4 = new Participant("Four", 4);
		
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(number1);
		participants.add(number2);
		participants.add(number3);
		participants.add(number4);
		
		for (Participant p: participants) {
			event.addParticipant(p);
		}
		
		assertEquals(4, event.getStartingQueue().size());
	}

	@Test
	public void testGetRunningQueue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFinishedQueue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFormattedData() {
		fail("Not yet implemented");
	}

	@Test
	public void testStartParticipant() {
		fail("Not yet implemented");
	}

	@Test
	public void testFinishParticipant() {
		fail("Not yet implemented");
	}

	@Test
	public void testExit() {
		event.exit();
		
	}

}
