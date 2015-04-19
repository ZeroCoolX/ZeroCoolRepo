package com.zerocool.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.Channel;
import com.zerocool.entities.ParGroup;
import com.zerocool.entities.ParIndividual;
import com.zerocool.entities.Participant;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.services.SystemTime;

public class ParIndividualTests {

	private Channel startChannel;
	private Channel finishChannel;
	private SystemController admin;
	private ParIndividual event;
	private SystemTime time;
	private LinkedList<Participant> participants;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		time = new SystemTime();
		event = new ParIndividual("Parallel Group Event", time.getTime());
		admin = new SystemController();
		startChannel = new Channel(admin, null, 1);
		finishChannel = new Channel(admin, null, 2);
		participants = new LinkedList<Participant>();
	}

	@After
	public void tearDown() throws Exception {
		participants.clear();
		event = null;
	}
	
	@Test
	public void testTriggered_EmptyStartQueue() {
		// no participants to start
		exception.expect(IllegalStateException.class);
		event.triggered(300000, startChannel.getId());
		assertEquals(0, event.getRunningQueue().size());
		assertEquals(0, event.getStartingQueue().size());
		assertEquals(0, event.getCurrentParticipants().size());
	}

	@Test
	public void testTriggeredFinsih_EmptyParticipants() {
		exception.expect(IllegalStateException.class);
		event.triggered(4000, finishChannel.getId());
		assertEquals(0, event.getFinishedQueue().size());
	}
	
	@Test
	public void testTriggeredStart() {
		participants = createTestParticipants();
		long startTime = time.getTime();
		for (Participant p : participants) {
			p.createNewRecord(event.getEventName(), event.getEventId());
			event.addParticipant(p);
			event.triggered(startTime, startChannel.getId());
		}
		
		for (Participant p : participants) {
			assertEquals(startTime, p.getRecordByEventId(event.getEventId()).getStartTime());
			assertTrue(p.getIsCompeting());
		}
		
		assertEquals(2, event.getRunningQueue().size());
		assertEquals(2, event.getCurrentParticipants().size());
	}
	
	@Test
	public void testTriggeredStart_OddChannel() {
		participants = createTestParticipants();
		Channel badStartChannel = new Channel(admin, null, 2);
		long startTime = time.getTime();
		for (Participant p : participants) {
			p.createNewRecord(event.getEventName(), event.getEventId());
			event.addParticipant(p);
			
			// Exception should be thrown because the start channel is 
			// an even number, which is for finish channels
			exception.expect(IllegalStateException.class);
			event.triggered(startTime, badStartChannel.getId());
		}
		
		for (Participant p : participants) {
			assertEquals(-1, p.getRecordByEventId(event.getEventId()).getStartTime());
			assertFalse(p.getIsCompeting());
		}
	}
	
	@Test
	public void testTriggeredFinish_EvenChannel() {
		participants = createTestParticipants();
		long startTime = time.getTime();
		long finishTime = time.getTime() + 700000;
		long elapsedTime = finishTime - startTime;
		
		for (Participant p : participants) {
			p.createNewRecord(event.getEventName(), event.getEventId());
			event.addParticipant(p);
			event.triggered(startTime, startChannel.getId());
		}
		
		for (Participant p : participants) {
			event.triggered(finishTime, finishChannel.getId());
			assertEquals(finishTime, p.getRecordByEventId(event.getEventId()).getFinishTime());
			assertEquals(elapsedTime, p.getRecordByEventId(event.getEventId()).getElapsedTime());
			assertFalse(p.getIsCompeting());
		}	
	}
	
	@Test
	public void testTriggeredFinish_BadOddChannel() {
		// use a finish channel with a start channel identifier
		Channel badChannel = new Channel(admin, null, 5);
		participants = createTestParticipants();
		long startTime = time.getTime();
		long finishTime = time.getTime() + 100000;
		
		for (Participant p : participants) {
			p.createNewRecord(event.getEventName(), event.getEventId());
			event.addParticipant(p);

			event.triggered(startTime, startChannel.getId());
		}
		
		
		for (Participant p : participants) {
			exception.expect(IllegalStateException.class);
			
			event.triggered(finishTime, badChannel.getId());
			// Participants should all still be competing because they
			// did not trigger the proper channel
			assertEquals(-1, p.getRecordByEventId(event.getEventId()).getFinishTime());
			assertEquals(-1, p.getRecordByEventId(event.getEventId()).getElapsedTime());
			assertTrue(p.getIsCompeting());
		}
	}

	@Test
	public void testSetDnf() {
		participants = createTestParticipants();
		long startTime = time.getTime();
		long finishTime = time.getTime() + 200000;
		
		for (Participant p : participants) {
			p.createNewRecord(event.getEventName(), event.getEventId());
			event.addParticipant(p);

			event.triggered(startTime, startChannel.getId());
		}
		
		for (Participant p : participants) {
			event.setDnf(finishTime);
			assertTrue(p.getRecordByEventId(event.getEventId()).getDnf());
		}
	}

	@Test
	public void testParIndividual() {
		long eventTime = time.getTime();
		event = new ParIndividual("Some ParIND Event", eventTime);
		assertEquals(eventTime, event.getEventTime());
		assertEquals("Some ParIND Event", event.getEventName());
		assertEquals(EventType.PARIND, event.getType());
	}

	private LinkedList<Participant> createTestParticipants() {
		 LinkedList<Participant> result = new LinkedList<Participant>();
		 
		 result.add(new Participant("Name 1", 1));
		 result.add(new Participant("Name 2", 2));
		 
		 return result;
	}
}
