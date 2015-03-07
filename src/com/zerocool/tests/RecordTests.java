package com.zerocool.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.entities.Record;

public class RecordTests {

	Record record;
	
	@Before
	public void setUp() throws Exception {
		record = new Record();
	}

	@After
	public void tearDown() throws Exception {
		record = null;
	}

	@Test
	public void testRecord() {
		assertNotNull(record);
		assertNull(record.getEventName());
		assertEquals(-1, record.getEventId());
		assertEquals(-1, record.getStartTime());
		assertEquals(-1, record.getFinishTime());
		assertEquals(-1, record.getElapsedTime());
		assertFalse(record.getDnf());
	}

	@Test
	public void testRecordStringInt() {
		record = new Record("Hi Mom!", 7);
		
		assertNotNull(record);
		assertEquals("Hi Mom!", record.getEventName());
		assertEquals(7, record.getEventId());
		assertEquals(-1, record.getStartTime());
		assertEquals(-1, record.getFinishTime());
		assertEquals(-1, record.getElapsedTime());
		assertFalse(record.getDnf());
	}

	@Test
	public void testRecordStringIntLongLongBoolean() {
		record = new Record("Hi Dad!", 2, 1000, 2000, false);
		
		assertNotNull(record);
		assertEquals("Hi Dad!", record.getEventName());
		assertEquals(2, record.getEventId());
		assertEquals(1000, record.getStartTime());
		assertEquals(2000, record.getFinishTime());
		assertEquals(1000, record.getElapsedTime());
		assertFalse(record.getDnf());
		
		record = new Record("Hi Dad!", 2, 1000, 2000, true);
		
		assertNotNull(record);
		assertEquals("Hi Dad!", record.getEventName());
		assertEquals(2, record.getEventId());
		assertEquals(1000, record.getStartTime());
		assertEquals(-1, record.getFinishTime());
		assertEquals(-1, record.getElapsedTime());
		assertTrue(record.getDnf());
		
		try {
			record = new Record("Fail", 9, -1, 2000, false);
			fail("Start time can't be negative!  Should throw an exception.");
		} catch (IllegalArgumentException e) {
			System.out.println("Caught the correct exception!");
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException.");
		}
		
		try {
			record = new Record("Fail", 9, 1000, -1, false);
			fail("Finish time can't be negative!  Should throw an exception.");
		} catch (IllegalArgumentException e) {
			System.out.println("Caught the correct exception!");
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException.");
		}
		
		try {
			record = new Record("Fail", 9, 1000, 500, false);
			fail("Finish time can't be less than start time!  Should throw an exception.");
		} catch (IllegalArgumentException e) {
			System.out.println("Caught the correct exception!");
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException.");
		}
	}

	@Test
	public void testDnf() {
		assertFalse(record.getDnf());
		
		record.setDnf(true);
		assertTrue(record.getDnf());
		
		record.setDnf(false);
		assertFalse(record.getDnf());
		
		record.setStartTime(1000);
		record.setFinishTime(2000);
		record.setDnf(true);
		assertEquals(1000, record.getStartTime());
		assertEquals(-1, record.getFinishTime());
		assertEquals(-1, record.getElapsedTime());
		
		record.setDnf(false);
		assertEquals(1000, record.getStartTime());
		assertEquals(2000, record.getFinishTime());
		assertEquals(1000, record.getElapsedTime());
	}

	@Test
	public void testFinishTime() {
		assertEquals(-1, record.getFinishTime());
		
		record.setFinishTime(1000);
		assertEquals(1000, record.getFinishTime());
		
		record.setFinishTime(999999999);
		assertEquals(999999999, record.getFinishTime());
		
		record.setDnf(true);
		assertEquals(-1, record.getFinishTime());
		
		record.setDnf(false);
		assertEquals(999999999, record.getFinishTime());
		
		record.setStartTime(1000);
		
		try {
			record.setFinishTime(500);
			fail("Finish time can't be less than start time!  Should throw an exception.");
		} catch (IllegalArgumentException e) {
			System.out.println("Caught the correct exception!");
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException.");
		}
		
		try {
			record.setFinishTime(-1);
			fail("Finish time can't be negative!  Should throw an exception.");
		} catch (IllegalArgumentException e) {
			System.out.println("Caught the correct exception!");
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException.");
		}
	}

	@Test
	public void testStartTime() {
		assertEquals(-1, record.getStartTime());
		
		record.setStartTime(1000);
		assertEquals(1000, record.getStartTime());
		
		record.setStartTime(999999999);
		assertEquals(999999999, record.getStartTime());
		
		record.setDnf(true);
		assertEquals(999999999,	record.getStartTime());
		
		record.setDnf(false);
		assertEquals(999999999, record.getStartTime());
		
		record.setStartTime(500);
		record.setFinishTime(1000);
		
		try {
			record.setStartTime(2000);
			fail("Start time can't be greater than finish time!  Should throw an exception.");
		} catch (IllegalArgumentException e) {
			System.out.println("Caught the correct exception!");
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException.");
		}
		
		try {
			record.setStartTime(-1);
			fail("Start time can't be negative!  Should throw an exception.");
		} catch (IllegalArgumentException e) {
			System.out.println("Caught the correct exception!");
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException.");
		}
	}

	@Test
	public void testElapsedTime() {
		assertEquals(-1, record.getElapsedTime());
		
		record.setStartTime(1000);
		assertEquals(-1, record.getElapsedTime());
		
		record.setFinishTime(2000);
		assertEquals(1000, record.getElapsedTime());
		
		record.setFinishTime(10001);
		// OVER 9000!
		assertEquals(9001, record.getElapsedTime());
		
		record.setStartTime(10000);
		assertEquals(1, record.getElapsedTime());
		
		record.setDnf(true);
		assertEquals(-1, record.getElapsedTime());
		
		record.setDnf(false);
		assertEquals(1, record.getElapsedTime());
		
		record = new Record();
		record.setFinishTime(1000);
		assertEquals(-1, record.getElapsedTime());
		
		record.setStartTime(500);
		assertEquals(500, record.getElapsedTime());
	}

	@Test
	public void testEventName() {
		assertNull(record.getEventName());
		
		record.setEventName("You suck");
		assertEquals("You suck", record.getEventName());
		
		record.setEventName("Gotta catch'em all!");
		assertEquals("Gotta catch'em all!", record.getEventName());
	}

	@Test
	public void testEventId() {
		assertEquals(-1, record.getEventId());
		
		record.setEventId(78797);
		assertEquals(78797, record.getEventId());
		
		record.setEventId(-8765678);
		assertEquals(-8765678, record.getEventId());
	}

	@Test
	public void testExit() {
		record.setEventName("Something Cool");
		record.setEventId(9000000);
		record.setStartTime(1000);
		record.setFinishTime(2000);
		record.setDnf(true);
		
		record.exit();
		
		assertNotNull(record);
		assertNull(record.getEventName());
		assertEquals(-1, record.getEventId());
		assertEquals(-1, record.getStartTime());
		assertEquals(-1, record.getFinishTime());
		assertEquals(-1, record.getElapsedTime());
		assertFalse(record.getDnf());
	}

}
