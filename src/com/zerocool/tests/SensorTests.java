package com.zerocool.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.controllers.SystemController;

public class SensorTests {

	Queue<String> commandList;
	
	SystemController systemController;
	
	@Before
	public void setUp() throws Exception {
		commandList = new LinkedList<String>();
		systemController = new SystemController();
	}

	@After
	public void tearDown() throws Exception {
		commandList = null;
		systemController = null;
	}
	
	private void executeCommands() {
		while (!commandList.isEmpty()) {
			try {			
				systemController.executeCommand(commandList.poll());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testSensor() {
		commandList.add("10:00:00.0	TIME 10:01:00.0");
		commandList.add("10:01:02.0	ON");
		commandList.add("10:01:04.0 CONN GATE 1");
		
		executeCommands();
		
		assertNotNull(systemController.getChannels()[0].getSensor());
		
		commandList.add("10:00:00.0	TIME 10:01:00.0");
		commandList.add("10:01:02.0	TOGGLE 1");
		commandList.add("10:01:03.0	START");
		
		executeCommands();
	}

	@Test
	public void testGetType() {
//		String type = sensor.getType();
//		assertNotNull(type);
	}

	@Test
	public void testSetSensorType() {
//		sensor.setSensorType("EYE");
//		assertEquals("EYE", sensor.getType());
//		sensor.setSensorType("GATE");
//		assertEquals("GATE", sensor.getType());
//		sensor.setSensorType("PAD");
//		assertEquals("PAD", sensor.getType());
	}

	@Test
	public void testSetState() {
//		sensor.setState(true);
//		assertTrue(sensor.getState());
//		sensor.setState(false);
//		assertFalse(sensor.getState());
	}

	@Test
	public void testExit() {
//		sensor.exit();
//		assertFalse(sensor.getState());
	}

}
