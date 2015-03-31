package com.zerocool.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.zerocool.entities.Channel;
import com.zerocool.entities.Sensor;

public class ChannelTests {

	Channel channel;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		channel = new Channel(null, 0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testChannel() {
		assertNotNull(channel);
		assertNull(channel.getSensorType());
		assertFalse(channel.getState());
		assertEquals(0, channel.getId());
	}

	@Test
	public void testChannelString() {
		channel = new Channel("GATE");
		assertFalse(channel.getState());
		assertEquals(0, channel.getId());
		assertEquals("GATE", channel.getSensorType());
	}
	
	@Test
	public void testBadChannelString() {
		exception.expect(IllegalArgumentException.class);
		channel = new Channel("you suck");
	}

	@Test
	public void testChannelStringBoolean() {
		channel = new Channel("PAD", true);
		assertTrue(channel.getState());
		assertEquals(0, channel.getId());
		assertEquals("PAD", channel.getSensorType());
	}
	
	@Test
	public void testBadChannelStringBoolean() {
		exception.expect(IllegalArgumentException.class);
		channel = new Channel("you suck", true);
	}

	@Test
	public void testAddSensorSensor() {
		Sensor s = new Sensor();
		s.setSensorType("EYE");
		channel.addSensor(s);
		assertEquals("EYE", channel.getSensorType());
	}

	@Test
	public void testAddSensorString() {
		fail("Not yet implemented");
	}

	@Test
	public void testDisconnectSensor() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveSensor() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetState() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSensorState() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSensorType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetId() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetState() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSensorState() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSensorType() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetID() {
		fail("Not yet implemented");
	}

	@Test
	public void testExit() {
		fail("Not yet implemented");
	}

}
