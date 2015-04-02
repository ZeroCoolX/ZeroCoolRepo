package com.zerocool.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.Individual;
import com.zerocool.entities.Participant;

public class ParticipantTests {
	Participant participant1;
	Participant participant2;

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
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
		// Arrange
		AbstractEvent e1 = new Individual("event1", 0);
				
		// Act
		participant1.createNewRecord(e1.getEventName(), e1.getEventId());
		assertNotNull(participant1.getFormattedData());
	}
	
	@Test
	public void testGetFormattedData_NoRecord() {
		exception.expect(IllegalArgumentException.class);
		participant1.getFormattedData();
	}

	@Test
	public void testGetRecordIntNoRecord() {
		exception.expect(IllegalArgumentException.class);
		participant1.getRecord(-1);
	}
	
	@Test
	public void testGetLastRecordNoRecord() {
		exception.expect(IllegalArgumentException.class);
		participant2.getLastRecord();
		participant1.getLastRecord();
	}
	
	@Test
	public void testGetRecordByEventIdNoEvent() {
		assertNull(participant1.getRecordByEventId(10));
		assertNull(participant2.getRecordByEventId(-1000));
	}
	
	@Test
	public void testGetRecordByEventId() {
		
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
		
		assertEquals(1, participant1.getRecordCount());
		assertEquals(3, participant2.getRecordCount());
	}

	@Test
	public void testGetName() {
		// Assert
		
		assertEquals("Name 1", participant1.getName());
		assertEquals("Name 2", participant2.getName());
	}

	@Test
	public void testGetIsCompeting() {
		assertFalse(participant1.getIsCompeting());
		assertFalse(participant2.getIsCompeting());
	}

	@Test
	public void testEqualsObject() {
		Participant participant3 = new Participant("Name 3", 1);
		
		assertTrue(participant1.equals(participant3));
	}

	@Test
	public void testSetIsCompetingTrue() {
		// Act 
		participant1.setIsCompeting(true);
		participant2.setIsCompeting(true);
		
		// Assert
		assertTrue(participant1.getIsCompeting());
		assertTrue(participant2.getIsCompeting());
	}

	@Test
	public void testSetIsCompetingFalse() {
		// Act 
		participant1.setIsCompeting(false);
		participant2.setIsCompeting(false);
		
		// Assert
		assertFalse(participant1.getIsCompeting());
		assertFalse(participant2.getIsCompeting());
	}
	
	@Test
	public void testExit() {
		// Act
		participant1.exit();
		participant2.exit();
		
		// Assert
		assertEquals(-1, participant1.getId());
		assertEquals(0, participant1.getRecordCount());
		assertNull(participant1.getName());
		assertFalse(participant1.getIsCompeting());
		assertEquals(-1, participant2.getId());
		assertEquals(0, participant2.getRecordCount());
		assertNull(participant2.getName());
		assertFalse(participant2.getIsCompeting());
	}

}
