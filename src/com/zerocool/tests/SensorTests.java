package com.zerocool.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.entities.Sensor;

public class SensorTests {

	Sensor sensor;
	
	@Before
	public void setUp() throws Exception {
		sensor = new Sensor();
	}

	@After
	public void tearDown() throws Exception {
		sensor = null;
	}

	@Test
	public void testSensor() {
		assertNotNull(sensor);
	}

	@Test
	public void testSensorBoolean() {
		sensor = new Sensor(true);
		assertNotNull(sensor);
		assertTrue(sensor.getState());
	}

	@Test
	public void testGetState() {
		assertFalse(sensor.getState());
	}

	@Test
	public void testGetType() {
		String type = sensor.getType();
		assertNotNull(type);
	}

	@Test
	public void testSetSensorType() {
		sensor.setSensorType("EYE");
		assertEquals("EYE", sensor.getType());
		sensor.setSensorType("GATE");
		assertEquals("GATE", sensor.getType());
		sensor.setSensorType("PAD");
		assertEquals("PAD", sensor.getType());
	}

	@Test
	public void testSetState() {
		sensor.setState(true);
		assertTrue(sensor.getState());
		sensor.setState(false);
		assertFalse(sensor.getState());
	}

	@Test
	public void testExit() {
		sensor.exit();
		assertFalse(sensor.getState());
	}

}
