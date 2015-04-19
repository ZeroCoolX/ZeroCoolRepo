package com.zerocool.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Channel;
import com.zerocool.entities.Group;
import com.zerocool.entities.Participant;
import com.zerocool.services.SystemTime;

public class GroupTests {

	private Channel startChannel;
	private Channel finishChannel;
	private SystemController admin;
	private Group event;
	private SystemTime time;
	private LinkedList<Participant> participants;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		time = new SystemTime();
		event = new Group("Group Event", time.getTime());
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
	public void testTriggeredStart_EmptyStartQueue() {
		event.triggered(100000, startChannel.getId());
		assertEquals(0, event.getRunningQueue().size());
		assertEquals(0, event.getStartingQueue().size());
		assertEquals(0, event.getCurrentParticipants().size());
	}
	
	@Test
	public void testTriggeredFinsih_EmptyParticipants() {
		event.triggered(200000, finishChannel.getId());
		assertEquals(0, event.getFinishedQueue().size());
	}

	@Test
	public void testTriggeredStart() {
		participants = createTestParticipants();
		long startTime = time.getTime();
		for (Participant p : participants) {
			p.createNewRecord(event.getEventName(), event.getEventId());
			event.addParticipant(p);
		}
		
		event.triggered(startTime, startChannel.getId());
		
		for (Participant p : participants) {
			assertEquals(startTime, p.getRecordByEventId(event.getEventId()).getStartTime());
			assertTrue(p.getIsCompeting());
		}
		
		assertEquals(8, event.getRunningQueue().size());
		assertEquals(8, event.getCurrentParticipants().size());
	}
	
	@Test
	public void testTriggeredFinish() {
		participants = createTestParticipants();
		long startTime = time.getTime();
		long finishTime = time.getTime() + 200000;
		long elapsedTime = finishTime - startTime;
		
		for (Participant p : participants) {
			p.createNewRecord(event.getEventName(), event.getEventId());
			event.addParticipant(p);
		}
		
		event.triggered(startTime, startChannel.getId());
		
		for (Participant p : participants) {
			event.triggered(finishTime, finishChannel.getId());
			assertEquals(finishTime, p.getRecordByEventId(event.getEventId()).getFinishTime());
			assertEquals(elapsedTime, p.getRecordByEventId(event.getEventId()).getElapsedTime());
			assertFalse(p.getIsCompeting());
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
		}
		
		event.triggered(startTime, startChannel.getId());
		
		for (Participant p : participants) {
			event.setDnf(finishTime);
			assertTrue(p.getRecordByEventId(event.getEventId()).getDnf());
		}
	}

	@Test
	public void testGroup() {
		long eventTime = time.getTime();
		event = new Group("Some Group Event", eventTime);
		assertEquals(eventTime, event.getEventTime());
		assertEquals("Some Group Event", event.getEventName());
		assertEquals(EventType.GRP, event.getType());
	}
	
	private LinkedList<Participant> createTestParticipants() {
		 LinkedList<Participant> result = new LinkedList<Participant>();
		 
		 result.add(new Participant("Name 1", 1));
		 result.add(new Participant("Name 2", 2));
		 result.add(new Participant("Name 3", 3));
		 result.add(new Participant("Name 4", 4));
		 result.add(new Participant("Name 5", 5));
		 result.add(new Participant("Name 6", 6));
		 result.add(new Participant("Name 7", 7));
		 result.add(new Participant("Name 8", 8));
		 
		 return result;
	}
}
