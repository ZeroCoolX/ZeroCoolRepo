package com.zerocool.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.Channel;
import com.zerocool.entities.Sensor;

public class ChannelTests {

	Channel channel;
	SystemController admin;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		admin = new SystemController();
		channel = new Channel(admin, null, 1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testChannel() {
		assertNotNull(channel);
		assertNull(channel.getSensorType());
		assertFalse(channel.getState());
		assertEquals(1, channel.getId());
	}

	@Test
	public void testChannelInvalidSensorType() {
		exception.expect(IllegalArgumentException.class);
		channel = new Channel(admin, "Some other Sensor", 1);
	}
	
	@Test
	public void testChannelValidSensorType() {
		channel = new Channel(admin, "PAD", 1);
		assertEquals("PAD", channel.getSensorType());
		
		channel = new Channel(admin, "GATE", 1);
		assertEquals("GATE", channel.getSensorType());
		
		channel = new Channel(admin, "EYE", 1);
		assertEquals("EYE", channel.getSensorType());
		assertFalse(channel.getState());
		assertEquals(1, channel.getId());
	}

	@Test
	public void testChannelIsActive() {
		channel = new Channel(admin, null, 2, true);
		assertTrue(channel.getState());
	}
	
	@Test
	public void testChannelNotActive() {
		channel = new Channel(admin, null, 1, false);
		assertFalse(channel.getState());
	}

	@Test
	public void testAddSensorSensor() {
		Sensor s = new Sensor(admin, "EYE", 2);
		channel.addSensor(s);
		assertEquals("EYE", channel.getSensorType());
		
		s = new Sensor(admin, "GATE", 2);
		channel.addSensor(s);
		assertEquals("GATE", channel.getSensorType());
	}

	@Test
	public void testAddSensorString() {
		channel.addSensor("PAD");
		assertEquals("PAD", channel.getSensorType());
	}
	
	@Test
	public void testAddSensorStringNotValid() {
		exception.expect(IllegalArgumentException.class);
		channel.addSensor("Wierd Sensor");
	}

	@Test
	public void testDisconnectSensor() {
		channel.addSensor(new Sensor(admin, "PAD", 5));
		assertEquals("PAD", channel.getSensorType());
		
		channel.disconnectSensor();
		assertNull(channel.getSensorType());
		assertNull(channel.getSensor());
	}

	@Test
	public void testTriggerSensor() {
		channel.addSensor(new Sensor(admin, "EYE", 5));
		channel.setState(true);
		assertTrue(channel.triggerSensor());
	}
	
	@Test
	public void testTriggerSensorNoSensor() {
		// test channel has no sensor in these tests from setup
		assertFalse(channel.triggerSensor());
	}
	
	@Test
	public void testTriggerSensorChannelNotActive() {
		// test channel is created not active
		assertFalse(channel.triggerSensor());
	}
	
	@Test
	public void testGetStateActive() {
		channel = new Channel(admin, null, 3, true);
		assertTrue(channel.getState());
	}
	
	@Test
	public void testGetStateNotActive() {
		assertFalse(channel.getState());
	}

	@Test
	public void testGetSensorStateNoSensor() {
		assertFalse(channel.getSensorState());
	}
	
	@Test
	public void testGetSensorStateWithSensor() {
		channel.addSensor(new Sensor(admin, "GATE", 9));
		assertTrue(channel.getSensorState());
	}

	@Test
	public void testGetSensorTriggerNoSensor() {
		assertFalse(channel.getSensorTrigger());
	}

	@Test
	public void testGetSensorTriggerNotTriggered() {
		channel.addSensor(new Sensor(admin, "GATE", 9));
		assertFalse(channel.getSensorTrigger());
	}
	
	@Test
	public void testSensorTriggerWithSensorAndTriggered() {
		channel.addSensor(new Sensor(admin, "GATE", 9));
		channel.getSensor().trigger();
		assertTrue(channel.getSensorTrigger());
	}
	
	@Test
	public void testGetId() {
		assertEquals(1, channel.getId());
		channel = new Channel(admin, null, 100);
		assertEquals(100, channel.getId());
	}

	@Test
	public void testSetState() {
		// this is also tested in other test cases 
		channel.setState(true);
		assertTrue(channel.getState());
	}

	@Test
	public void testResetSensorTrigger() {
		channel.addSensor(new Sensor(admin, "GATE", 4));
		channel.getSensor().trigger();
		channel.resetSensorTrigger();
		assertFalse(channel.getSensorTrigger());
	}

	@Test
	public void testSetSensorTypeNullSensor() {
		exception.expect(IllegalArgumentException.class);
		channel.setSensorType(null);
	}

	@Test
	public void testSetSensorType() {
		channel.setSensorType("PAD");
	}
	
	@Test
	public void testSetSensorTypeBadSpelling() {
		exception.expect(IllegalArgumentException.class);
		channel.setSensorType("Gate");
	}


	
	@Test
	public void testSetID() {
		channel.setID(300);
		assertEquals(300, channel.getId());
	}

	@Test
	public void testExit() {
		channel.exit();
		assertEquals(-1, channel.getId());
		assertNull(channel.getSensor());
		assertFalse(channel.getState());
	}

}
