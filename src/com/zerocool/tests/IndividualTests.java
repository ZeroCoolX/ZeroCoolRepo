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
import com.zerocool.entities.Individual;
import com.zerocool.entities.Participant;
import com.zerocool.services.SystemTime;

public class IndividualTests {

	private Channel startChannel;
	private Channel finishChannel;
	private SystemController admin;
	private Individual event;
	private SystemTime time;
	private LinkedList<Participant> participants;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		time = new SystemTime();
		event = new Individual("Some Event", time.getTime());
		admin = new SystemController();
		startChannel = new Channel(admin, null, 1);
		finishChannel = new Channel(admin, null, 2);
		participants = new LinkedList<Participant>();
		for (int i = 1; i < 5; ++i) {
			Participant p = new Participant("Name" + i, i);
			p.createNewRecord(event.getEventName(), event.getEventId());
			participants.add(p);
			event.addParticipant(p);
		}
	}

	@After
	public void tearDown() throws Exception {
		participants.clear();
		event.newRun();
	}
	
	@Test
	public void testNewRunEmptyEvent() {
		
	}
	
	@Test
	public void testNewRun() {
		
	}

	@Test
	public void testIndividual() {
		long eventTime = time.getTime();
		event = new Individual("Some Event", eventTime);
		assertEquals(eventTime, event.getEventTime());
		assertEquals("Some Event", event.getEventName());
		assertEquals(EventType.IND, event.getType());
	}
	
	@Test
	public void testTriggeredStart() {
		long curTime = time.getTime();
		
		for (int i = 0; i < participants.size(); ++i) {
			event.triggered(curTime, startChannel.getId());
		}
		
		for (Participant p : participants) {
			assertTrue(p.getIsCompeting());
			assertEquals(curTime, p.getRecordByEventId(event.getEventId()).getStartTime());
		}
		
		assertEquals(4, event.getRunningQueue().size());
	}
	
	@Test
	public void testTriggeredFinsih() {
		long curTime = time.getTime();
		long timeToFin = 100000;
		long total = curTime + timeToFin;
		long elapsed = total - curTime;
		
		for (int i = 0; i < participants.size(); ++i) {
			event.triggered(curTime, startChannel.getId());
			event.triggered(total, finishChannel.getId());
		}
		
		for (Participant p : participants) {
			assertFalse(p.getIsCompeting());
			assertEquals(total, p.getRecordByEventId(event.getEventId()).getFinishTime());
			assertEquals(elapsed, p.getRecordByEventId(event.getEventId()).getElapsedTime());
		}
		
		assertEquals(4, event.getFinishedQueue().size());
	}
	
	@Test
	public void testTriggeredEmptyStartingQueue() {
		// reset to new run
		event.newRun();
		
		assertEquals(0, event.getStartingQueue().size());
		exception.expect(IllegalStateException.class);
		event.triggered(time.getTime(), startChannel.getId());
	}
	
	@Test
	public void testTriggeredEmptyRunningQueue() {
		assertEquals(0, event.getRunningQueue().size());
		exception.expect(IllegalStateException.class);
		event.triggered(time.getTime(), finishChannel.getId());
	}

	@Test
	public void testSetDnf() {	
		for (int i = 0; i < participants.size(); ++i) {
			long curTime = time.getTime();
			event.triggered(curTime, startChannel.getId());
			event.setDnf(curTime);
		}
		
		for (Participant p : participants) {
			assertTrue(p.getLastRecord().getDnf());
		}
	}
	
	@Test
	public void testSetDnfNoParticipants() {
		exception.expect(IllegalStateException.class);
		event.setDnf(time.getTime());
	}
	
	@Test
	public void testGetRunningQueue() {
		long curTime = time.getTime();
		Participant p = new Participant("Extra one", 11);
		p.createNewRecord();
		participants.add(p);
		event.addParticipant(p);
		
		for (int i = 0; i < participants.size(); ++i) {
			event.triggered(curTime, startChannel.getId());
		}
		
		assertEquals(5, event.getRunningQueue().size());
	}

	@Test
	public void testGetFinishedQueue() {
		for (int i = 0; i < participants.size(); ++i) {
			event.triggered(time.getTime(), startChannel.getId());
			event.triggered(time.getTime(), finishChannel.getId());
		}
		
		assertEquals(4, event.getFinishedQueue().size());
	}
}
