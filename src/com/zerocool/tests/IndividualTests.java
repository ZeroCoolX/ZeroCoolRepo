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
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIndividual() {
		long eventTime = time.getTime();
		event = new Individual("Some Event", eventTime);
		assertEquals(eventTime, event.getEventTime());
		assertEquals("Some Event", event.getEventName());
	}
	
	@Test
	public void testTriggeredStart() {
		
	}
	
	@Test
	public void testTriggeredFinsih() {
		
	}
	
	@Test
	public void testTriggeredEmptyStartingQueue() {
		assertEquals(0, event.getStartingQueue());
		exception.expect(IllegalStateException.class);
		event.triggered(time.getTime(), startChannel.getId());
	}
	
	@Test
	public void testTriggeredEmptyRunningQueue() {
		assertEquals(0, event.getRunningQueue());
		exception.expect(IllegalStateException.class);
		event.triggered(time.getTime(), finishChannel.getId());
	}

	@Test
	public void testSetDnf() {
		for (int i = 1; i < 5; ++i) {
			Participant p = new Participant("Name" + i, i);
			p.createNewRecord(event.getEventName(), event.getEventId());
			participants.add(p);
			event.addParticipant(p);
		}
		
		for (Participant p : participants) {
			long curTime = time.getTime();
			event.triggered(time.getTime(), startChannel.getId());
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
		fail("Not yet implemented");
	}

	@Test
	public void testGetFinishedQueue() {
		fail("Not yet implemented");
	}
}
