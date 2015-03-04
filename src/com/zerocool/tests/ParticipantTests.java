package com.zerocool.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.Individual;
import com.zerocool.entities.Participant;

public class ParticipantTests {
	Participant participant1;
	Participant participant2;

	@Before
	public void setUp() throws Exception {
		participant1 = new Participant("Name 1", 1);
		participant2 = new Participant("Name 2", 2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParticipant() {
		// assert
		assertNotNull(participant1);
		assertNotNull(participant2);
	}

	@Test
	public void testCreateNewRecord() {
		// Act
		participant1.createNewRecord();
		participant2.createNewRecord();
		participant2.createNewRecord();
		
		// Assert
		assertNotNull(participant1.getLastRecord());
		assertEquals(1, participant1.getRecordCount());
		
		assertNotNull(participant2.getLastRecord());
		assertEquals(2, participant2.getRecordCount());
	}

	@Test
	public void testCreateNewRecordStringInt() {
		// Arrange
		AbstractEvent e1 = new Individual("event1", 0);
		
		// Act
		participant1.createNewRecord(e1.getEventName(), e1.getEventId());
		participant2.createNewRecord(e1.getEventName(), e1.getEventId());
		
		// Assert
		assertEquals(e1.getEventName(), participant1.getLastRecord().getEventName());
		assertEquals(e1.getEventName(), participant2.getLastRecord().getEventName());
		assertEquals(e1.getEventId(), participant1.getLastRecord().getEventId());
		assertEquals(e1.getEventId(), participant2.getLastRecord().getEventId());
		assertEquals(1, participant1.getRecordCount());
		assertEquals(1, participant2.getRecordCount());
	}

	@Test
	public void testGetLastRecord() {
		// Act
		participant1.createNewRecord();
		participant2.createNewRecord();
		
		// Assert
		assertNotNull(participant1.getLastRecord());
		assertNotNull(participant2.getLastRecord());
	}

	@Test
	public void testGetRecord() {
		// Act
		participant1.createNewRecord();
		participant2.createNewRecord();
				
		// Assert
		assertNotNull(participant1.getRecord(0));
		assertNotNull(participant2.getRecord(0));
	}

	@Test
	public void testGetFormattedData() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetId() {
		assertEquals(1, participant1.getId());
		assertEquals(2, participant2.getId());
	}

	@Test
	public void testGetRecordCount() {
		// Act
		participant1.createNewRecord();
		participant2.createNewRecord();
		participant2.createNewRecord();
		participant2.createNewRecord();
		
		// Assert
		
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIsCompeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetIsCompeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testExit() {
		fail("Not yet implemented");
	}

}
