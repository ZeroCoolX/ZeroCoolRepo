package com.zerocool.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.Sensor;

public class SensorTests {

	Queue<String> commandList;
	Sensor sensor;
	SystemController systemController;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		commandList = new LinkedList<String>();
	
		systemController = new SystemController();
		sensor = new Sensor(systemController, "PAD", 1);
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
		// commandList.add("10:01:03.0	START");
		
		executeCommands();
	}

	@Test
	public void testSensorBadType() {
		exception.expect(IllegalArgumentException.class);
		sensor = new Sensor(systemController, "fkdajir", 1);
	}
	
	@Test
	public void testSensorGate() {
		sensor = new Sensor(systemController, "GATE", 1);
		assertEquals("GATE", sensor.getType());
	}
	
	@Test
	public void testSensorPad() {
		sensor = new Sensor(systemController, "PAD", 1);
		assertEquals("PAD", sensor.getType());
	}
	
	@Test
	public void testSensorEye() {
		sensor = new Sensor(systemController, "EYE", 1);
		assertEquals("EYE", sensor.getType());
	}
	
	@Test
	public void testGetType() {
		commandList.add("10:00:00.0	TIME 10:00:00.0");
		commandList.add("10:00:02.0	ON");
		commandList.add("10:00:04.0 CONN PAD 1");
		
		executeCommands();
		
		assertEquals("PAD", systemController.getChannels()[0].getSensorType());
		
		assertEquals("PAD", sensor.getType());	
	}

	@Test
	public void testSetSensorType() {
		sensor.setSensorType("GATE");
		assertEquals("GATE", sensor.getType());
	}

	@Test
	public void testSetSensorTypeBadType() {
		exception.expect(IllegalArgumentException.class);
		sensor.setSensorType("Type");
	}
	
	@Test
	public void testResetTrigger() {
		sensor.resetTrigger();
		assertFalse(sensor.getTrigger());
	}

	@Test
	public void testTriggerSensor() {
		
	}
	
	@Test
	public void testExit() {
		sensor.exit();
		assertFalse(sensor.getTrigger());
		assertNull(sensor.getType());
	}

}
