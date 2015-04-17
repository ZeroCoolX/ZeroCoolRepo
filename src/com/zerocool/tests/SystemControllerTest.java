package com.zerocool.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Queue;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.controllers.SystemController;
import com.zerocool.controllers.TaskList;
import com.zerocool.controllers.TaskList.Task;
import com.zerocool.entities.Participant;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Sensor.SensorType;

public class SystemControllerTest {
	
	SystemController sysCont = null;
	File file = null;
	FileReader fileReader = null;
	Scanner inFile = null;

	@Before
	public void setUp() throws Exception {
		sysCont = new SystemController();
	}

	@After
	public void tearDown() throws Exception {
		sysCont = null;
	}
	
	private void waitForCommand(String command) {
		
		if (sysCont.getLastTask() != null) {
			while (!sysCont.getLastTask().toString().equals(command)) {
				try {
					Thread.sleep(100);
				} catch (Exception e) { };
			}
		}
		
		try {
			Thread.sleep(500);
		} catch (Exception e) { };
	}
	
	@Test
	public void testSystemController() {
		assertNotNull(sysCont.getChannels());
		assertNotNull(sysCont.getEventLog());
		assertNotNull(sysCont.getTimer());
		assertNotNull(sysCont.getSystemTime());
	}
	
	@Test
	public void testReadFile() {
		file = new File("test_files/syscontroljunittestfile.txt");
		TaskList tasks = new TaskList();
		tasks.addTask(file);
		Queue<Task> commandList = tasks.getTaskList();
		assertEquals("TIME", commandList.peek().getTaskCommand());
		assertEquals("12:01:00.0", commandList.poll().getTaskArgumentOne());
		assertEquals("ON", commandList.poll().getTaskCommand());
		assertEquals("OFF", commandList.poll().getTaskCommand());
		assertEquals("CONN", commandList.peek().getTaskCommand());
		assertEquals("GATE", commandList.peek().getTaskArgumentOne());
		assertEquals("1", commandList.poll().getTaskArgumentTwo());
		assertEquals("TOGGLE", commandList.peek().getTaskCommand());
		assertEquals("1", commandList.poll().getTaskArgumentOne());
		assertEquals("NUM", commandList.peek().getTaskCommand());
		assertEquals("315", commandList.poll().getTaskArgumentOne());
		assertEquals("START", commandList.poll().getTaskCommand());
		assertEquals("FIN", commandList.poll().getTaskCommand());
		assertEquals("DNF", commandList.poll().getTaskCommand());
		assertEquals("PRINT", commandList.poll().getTaskCommand());
		assertEquals("EXIT", commandList.poll().getTaskCommand());
		assertTrue(commandList.isEmpty());
		
		sysCont = new SystemController();
		file = new File("test_files/sprint2_fast_testfile.txt");
		tasks.addTask(file);
		commandList = tasks.getTaskList();
		assertEquals("TIME", commandList.peek().getTaskCommand());
		assertEquals("12:00:01.0", commandList.poll().getTaskArgumentOne());
		assertEquals("ON", commandList.poll().getTaskCommand());
		assertEquals("OFF", commandList.poll().getTaskCommand());
		assertEquals("ON", commandList.poll().getTaskCommand());
		assertEquals("ON", commandList.poll().getTaskCommand());
		assertEquals("CONN", commandList.peek().getTaskCommand());
		assertEquals("GATE", commandList.peek().getTaskArgumentOne());
		assertEquals("1", commandList.poll().getTaskArgumentTwo());
		assertEquals("CONN", commandList.peek().getTaskCommand());
		assertEquals("EYE", commandList.peek().getTaskArgumentOne());
		assertEquals("2", commandList.poll().getTaskArgumentTwo());
		assertEquals("CONN", commandList.peek().getTaskCommand());
		assertEquals("GATE", commandList.peek().getTaskArgumentOne());
		assertEquals("3", commandList.poll().getTaskArgumentTwo());
		assertEquals("CONN", commandList.peek().getTaskCommand());
		assertEquals("EYE", commandList.peek().getTaskArgumentOne());
		assertEquals("4", commandList.poll().getTaskArgumentTwo());
		assertEquals("EVENT", commandList.peek().getTaskCommand());
		assertEquals("PARIND", commandList.poll().getTaskArgumentOne());
		assertEquals("TOGGLE", commandList.peek().getTaskCommand());
		assertEquals("1", commandList.poll().getTaskArgumentOne());
		assertEquals("TOGGLE", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("TOGGLE", commandList.peek().getTaskCommand());
		assertEquals("3", commandList.poll().getTaskArgumentOne());
		assertEquals("TOGGLE", commandList.peek().getTaskCommand());
		assertEquals("4", commandList.poll().getTaskArgumentOne());
		assertEquals("NUM", commandList.peek().getTaskCommand());
		assertEquals("234", commandList.poll().getTaskArgumentOne());
		assertEquals("NUM", commandList.peek().getTaskCommand());
		assertEquals("315", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("1", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("3", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("4", commandList.poll().getTaskArgumentOne());
		assertEquals("EXPORT", commandList.poll().getTaskCommand());
		assertEquals("PRINT", commandList.poll().getTaskCommand());
		assertEquals("ENDRUN", commandList.poll().getTaskCommand());
		assertEquals("NEWRUN", commandList.poll().getTaskCommand());
		assertEquals("EVENT", commandList.peek().getTaskCommand());
		assertEquals("PARGRP", commandList.poll().getTaskArgumentOne());
		assertEquals("NUM", commandList.peek().getTaskCommand());
		assertEquals("167", commandList.poll().getTaskArgumentOne());
		assertEquals("NUM", commandList.peek().getTaskCommand());
		assertEquals("166", commandList.poll().getTaskArgumentOne());
		assertEquals("NUM", commandList.peek().getTaskCommand());
		assertEquals("200", commandList.poll().getTaskArgumentOne());
		assertEquals("NUM", commandList.peek().getTaskCommand());
		assertEquals("201", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("1", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("1", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("3", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("4", commandList.poll().getTaskArgumentOne());
		assertEquals("PRINT", commandList.poll().getTaskCommand());
		assertEquals("EXPORT", commandList.poll().getTaskCommand());
		assertEquals("OFF", commandList.poll().getTaskCommand());
		assertEquals("ON", commandList.poll().getTaskCommand());
		assertEquals("NEWRUN", commandList.poll().getTaskCommand());
		assertEquals("EVENT", commandList.peek().getTaskCommand());
		assertEquals("GRP", commandList.poll().getTaskArgumentOne());
		assertEquals("TOGGLE", commandList.peek().getTaskCommand());
		assertEquals("1", commandList.poll().getTaskArgumentOne());
		assertEquals("TOGGLE", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("1", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("TRIG", commandList.peek().getTaskCommand());
		assertEquals("2", commandList.poll().getTaskArgumentOne());
		assertEquals("ENDRUN", commandList.poll().getTaskCommand());
		assertEquals("PRINT", commandList.poll().getTaskCommand());
		assertEquals("EXPORT", commandList.poll().getTaskCommand());
		assertEquals("OFF", commandList.poll().getTaskCommand());
		assertEquals("EXIT", commandList.poll().getTaskCommand());
		assertTrue(commandList.isEmpty());
	}
	
	@Test
	public void testReadInput() {
		System.out.println("\t-------Testing readInput()-------");
		System.out.println("This is a special type of test...\n" +
				"Since this is testing user input...it may not always be consistent...\n" +
				"Well in order for the test to test the data is accurate it has to know what to expect...AKA be consistent...\n" +
				"So please enter the commands like they are written pressing enter after each command:\n" +
				"<<OR if you're not really feeling like running this test just type EXIT and this test will be skipped>>\n\n" +
				"ON\n" +
				"OFF\n" +
				"ON\n" +
				"CONN GATE 1\n" +
				"TOGGLE 1\n" +
				"NUM 315\n" +
				"START\n" +
				"FIN\n" +
				"DNF\n" +
				"PRINT\n" +
				"EXIT");
		sysCont.readInput();
		System.out.println("\nIf no errors were thrown..then this test passed. YAY");
		System.out.println("\t-------Successfully tested readInput()-------\n");
	}
	
	@Test
	public void addTask() {
		sysCont = new SystemController();
		System.out.println("\t-------Testing addTask() with sprint 1 file-------");
		try {
		
			sysCont.addTask("12:01:00.0	TIME 12:01:01.0");
			
			sysCont.addTask("12:01:02.0	ON");
			waitForCommand("12:01:02.0 ON");
			assertNotNull(sysCont.getEventLog());
			assertNotNull(sysCont.getTimer());
			assertEquals(EventType.IND, sysCont.getTimer().getCurrentEvent().getType());
			assertEquals(EventType.IND + "", sysCont.getTimer().getCurrentEvent().getEventName());
			assertNotNull(sysCont.getTimer().getTotalParticipants());
			assertNotNull(sysCont.getTimer().getCurrentEvent().getCurrentParticipants());
			assertNotNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());

			sysCont.addTask("12:01:04.0	OFF");
			waitForCommand("12:01:04.0 OFF");
			assertNull(sysCont.getEventLog());
			assertNull(sysCont.getTimer());
			assertNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());
			
			sysCont.addTask("12:01:08.0	ON");
			waitForCommand("12:01:08.0 ON");
			
			sysCont.addTask("12:01:10.0	CONN GATE 1");
			waitForCommand("12:01:10.0 CONN GATE 1");
			assertEquals(8, sysCont.getChannels().length);
			assertEquals(0, sysCont.getChannels()[0].getId());
			assertNotNull(sysCont.getChannels()[0].getSensor());
			assertEquals(SensorType.GATE.toString(), sysCont.getChannels()[0].getSensorType());
			assertTrue(sysCont.getChannels()[0].getSensorState());
			
			sysCont.addTask("12:01:14.0	TOGGLE 1");
			waitForCommand("12:01:14.0 TOGGLE 1");
			assertEquals(0, sysCont.getChannels()[0].getId());
			assertTrue(sysCont.getChannels()[0].getState());
			assertTrue(sysCont.getChannels()[0].getSensorState());
			assertEquals(SensorType.GATE.toString(), sysCont.getChannels()[0].getSensorType());
			
			sysCont.addTask("12:01:18.0 NUM 234");
			waitForCommand("12:01:18.0 NUM 234");
			assertEquals(1, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(1, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(1, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertEquals(234, sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getId());
			
			sysCont.addTask("12:01:20.0	NUM 315");
			waitForCommand("12:01:20.0 NUM 315");
			assertEquals(2, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			for (Participant curPar : sysCont.getTimer().getCurrentEvent().getCurrentParticipants()) {
				assertNotNull(curPar.getLastRecord());
				assertEquals(sysCont.getTimer().getCurrentEvent().getEventId(), curPar.getLastRecord().getEventId());
				assertEquals("IND", curPar.getLastRecord().getEventName());
			}
			
			sysCont.addTask("12:01:22.0	START");
			waitForCommand("12:01:22.0 START");
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(0).getIsCompeting());
			assertEquals(315, sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getId());
			
			sysCont.addTask("12:01:24.0	NUM 435");
			waitForCommand("12:01:24.0 NUM 435");
			assertEquals(3, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(3, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			
			sysCont.addTask("12:01:26.0	FIN 234");
			waitForCommand("12:01:26.0 FIN 234");
			assertEquals(3, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertEquals(315, sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getId());
			assertFalse(sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getIsCompeting());
			
			sysCont.addTask("12:01:28.0	START");
			waitForCommand("12:01:28.0 START");
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(0).getIsCompeting());
			assertEquals(435, sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getId());
			
			sysCont.addTask("12:01:30.0	START");
			waitForCommand("12:01:30.0 START");
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(0).getIsCompeting());
			assertEquals(0, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			
			sysCont.addTask("12:01:32.0	DNF 315");
			waitForCommand("12:01:32.0 DNF 315");
			assertFalse(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(0).getIsCompeting());
			assertEquals(0, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			
			sysCont.addTask("12:01:34.0	FIN 435");
			waitForCommand("12:01:34.0 FIN 435");
			assertEquals(3, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertEquals(0, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			
			sysCont.addTask("12:01:36.0	PRINT");
			waitForCommand("12:01:36.0 PRINT");
			
			try {
				fileReader = new FileReader(sysCont.getEventLog().getEventFile());
				BufferedReader reader = new BufferedReader(fileReader);
				while (reader.ready()) {
					String line = reader.readLine();
					assertEquals("12:01:08.0", line.substring(0, 10));
					assertEquals("IND", line.substring(11));
					assertEquals("IND 12:01:08.0", reader.readLine().substring(2));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			sysCont.addTask("12:01:38.0	OFF");
			waitForCommand("12:01:38.0 OFF");
			assertNull(sysCont.getEventLog());
			assertNull(sysCont.getTimer());
			assertNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());
			
			sysCont.addTask("12:01:40.0	EXIT");
			waitForCommand("12:01:40.0 EXIT");
			assertNull(sysCont.getSystemTime());
			assertNull(sysCont.getEventLog());
			assertNull(sysCont.getTimer());
			assertNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\t-------Successfully tested addTask()-------\n");
		
		System.out.println("\t-------testing addTask() with sprint 2 file-------");
		try {
			sysCont = new SystemController();
			sysCont.addTask("12:00:00.0	TIME 12:01:00.0");
			sysCont.addTask("12:01:01.0	ON");
			waitForCommand("12:01:01.0 ON");
			//default Event is IND
			assertNotNull(sysCont.getEventLog());
			assertNotNull(sysCont.getTimer());
			assertEquals(EventType.IND, sysCont.getTimer().getCurrentEvent().getType());
			assertEquals(EventType.IND + "", sysCont.getTimer().getCurrentEvent().getEventName());
			assertNotNull(sysCont.getTimer().getTotalParticipants());
			assertNotNull(sysCont.getTimer().getCurrentEvent().getCurrentParticipants());
			assertNotNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());
			
			sysCont.addTask("12:01:02.0	OFF");
			waitForCommand("12:01:02.0 OFF");
			assertNull(sysCont.getEventLog());
			assertNull(sysCont.getTimer());
			assertNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());
			
			sysCont.addTask("12:01:03.0	ON");
			waitForCommand("12:01:03.0 ON");
			assertNotNull(sysCont.getTimer());
			assertEquals(EventType.IND, sysCont.getTimer().getCurrentEvent().getType());
			assertEquals(EventType.IND + "", sysCont.getTimer().getCurrentEvent().getEventName());
			assertNotNull(sysCont.getTimer().getTotalParticipants());
			assertNotNull(sysCont.getTimer().getCurrentEvent().getCurrentParticipants());
			assertNotNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());
			
			sysCont.addTask("12:01:04.0	ON");
			waitForCommand("12:01:04.0 ON");
			assertNotNull(sysCont.getTimer());
			assertEquals(EventType.IND, sysCont.getTimer().getCurrentEvent().getType());
			assertEquals(EventType.IND + "", sysCont.getTimer().getCurrentEvent().getEventName());
			assertNotNull(sysCont.getTimer().getTotalParticipants());
			assertNotNull(sysCont.getTimer().getCurrentEvent().getCurrentParticipants());
			assertNotNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());
			
			sysCont.addTask("12:01:05.0	CONN GATE 1");
			waitForCommand("12:01:05.0 CONN GATE 1");
			assertEquals(8, sysCont.getChannels().length);
			assertEquals(0, sysCont.getChannels()[0].getId());
			assertNotNull(sysCont.getChannels()[0].getSensor());
			assertEquals(SensorType.GATE.toString(), sysCont.getChannels()[0].getSensorType());
			assertTrue(sysCont.getChannels()[0].getSensorState());
			
			sysCont.addTask("12:01:06.0	CONN EYE 2");
			waitForCommand("12:01:06.0 CONN EYE 2");
			assertEquals(8, sysCont.getChannels().length);
			assertEquals(1, sysCont.getChannels()[1].getId());
			assertNotNull(sysCont.getChannels()[1].getSensor());
			assertEquals(SensorType.EYE.toString(), sysCont.getChannels()[1].getSensorType());
			assertTrue(sysCont.getChannels()[1].getSensorState());
			
			sysCont.addTask("12:01:07.0	CONN GATE 3");
			waitForCommand("12:01:07.0 CONN GATE 3");
			assertEquals(8, sysCont.getChannels().length);
			assertEquals(2, sysCont.getChannels()[2].getId());
			assertNotNull(sysCont.getChannels()[2].getSensor());
			assertEquals(SensorType.GATE.toString(), sysCont.getChannels()[2].getSensorType());
			assertTrue(sysCont.getChannels()[2].getSensorState());
			
			sysCont.addTask("12:01:08.0	CONN EYE 4");
			waitForCommand("12:01:08.0 CONN EYE 4");
			assertEquals(8, sysCont.getChannels().length);
			assertEquals(3, sysCont.getChannels()[3].getId());
			assertNotNull(sysCont.getChannels()[3].getSensor());
			assertEquals(SensorType.EYE.toString(), sysCont.getChannels()[3].getSensorType());
			assertTrue(sysCont.getChannels()[3].getSensorState());
			
			sysCont.addTask("12:01:09.0	EVENT PARIND");
			waitForCommand("12:01:09.0 EVENT PARIND");
			assertEquals(EventType.PARIND+ "", sysCont.getTimer().getCurrentEvent().getType().toString());
			assertEquals(EventType.PARIND + "", sysCont.getTimer().getCurrentEvent().getEventName());
			
			sysCont.addTask("12:01:10.0	TOGGLE 1");
			waitForCommand("12:01:10.0 TOGGLE 1");
			assertEquals(0, sysCont.getChannels()[0].getId());
			assertTrue(sysCont.getChannels()[0].getState());
			assertTrue(sysCont.getChannels()[0].getSensorState());
			assertEquals(SensorType.GATE.toString(), sysCont.getChannels()[0].getSensorType());
			
			sysCont.addTask("12:01:11.0	TOGGLE 2");
			waitForCommand("12:01:11.0 TOGGLE 2");
			assertEquals(1, sysCont.getChannels()[1].getId());
			assertTrue(sysCont.getChannels()[1].getState());
			assertTrue(sysCont.getChannels()[1].getSensorState());
			assertEquals(SensorType.EYE.toString(), sysCont.getChannels()[1].getSensorType());
			
			sysCont.addTask("12:01:12.0	TOGGLE 3");
			waitForCommand("12:01:12.0 TOGGLE 3");
			assertEquals(2, sysCont.getChannels()[2].getId());
			assertTrue(sysCont.getChannels()[2].getState());
			assertTrue(sysCont.getChannels()[2].getSensorState());
			assertEquals(SensorType.GATE.toString(), sysCont.getChannels()[2].getSensorType());
			
			sysCont.addTask("12:01:13.0	TOGGLE 4");
			waitForCommand("12:01:13.0 TOGGLE 4");
			assertEquals(3, sysCont.getChannels()[3].getId());
			assertTrue(sysCont.getChannels()[3].getState());
			assertTrue(sysCont.getChannels()[3].getSensorState());
			assertEquals(SensorType.EYE.toString(), sysCont.getChannels()[3].getSensorType());
			
			sysCont.addTask("12:01:14.0	NUM 234");
			waitForCommand("12:01:14.0 NUM 234");
			assertEquals(1, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(1, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(1, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertEquals(234, sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getId());
			
			sysCont.addTask("12:01:15.0	NUM 315");
			waitForCommand("12:01:15.0 NUM 315");
			assertEquals(2, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			
			sysCont.addTask("12:01:16.0	TRIG 1");
			waitForCommand("12:01:16.0 TRIG 1");
			assertTrue(sysCont.getChannels()[0].getSensorState());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(0).getIsCompeting());
			
			sysCont.addTask("12:01:17.0	TRIG 3");
			waitForCommand("12:01:17.0 TRIG 3");
			assertTrue(sysCont.getChannels()[2].getSensorState());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(1).getIsCompeting());
			
			sysCont.addTask("12:01:18.0	TRIG 2");
			waitForCommand("12:01:18.0 TRIG 2");
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertFalse(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(0).getIsCompeting());
			
			sysCont.addTask("12:01:19.0	TRIG 4");
			waitForCommand("12:01:19.0 TRIG 4");
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertFalse(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(1).getIsCompeting());
			
			sysCont.addTask("12:01:20.0	EXPORT");
			waitForCommand("12:01:20.0 EXPORT");
			sysCont.addTask("12:01:21.0	PRINT");
			waitForCommand("12:01:21.0 PRINT");
			sysCont.addTask("12:01:22.0	ENDRUN");
			waitForCommand("12:01:22.0 ENDRUN");
			sysCont.addTask("12:01:23.0	NEWRUN");
			waitForCommand("12:01:23.0 NEWRUN");
			sysCont.addTask("12:01:24.0	EVENT PARGRP");
			waitForCommand("12:01:24.0 EVENT PARGRP");
			assertEquals(EventType.PARGRP+ "", sysCont.getTimer().getCurrentEvent().getType().toString());
			assertEquals(EventType.PARGRP + "", sysCont.getTimer().getCurrentEvent().getEventName());
			
			sysCont.addTask("12:01:25.0	NUM 167");
			waitForCommand("12:01:25.0 NUM 167");
			assertEquals(3, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(1, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(1, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertEquals(167, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(0).getId());
			
			sysCont.addTask("12:01:26.0	NUM 166");
			waitForCommand("12:01:26.0 NUM 166");
			assertEquals(4, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertEquals(166, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(1).getId());
			
			sysCont.addTask("12:01:27.0	NUM 200");
			waitForCommand("12:01:27.0 NUM 200");
			assertEquals(5, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(3, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(3, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertEquals(200, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(2).getId());
			
			sysCont.addTask("12:01:28.0	NUM 201");
			waitForCommand("12:01:28.0 NUM 201");
			assertEquals(6, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(4, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(4, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertEquals(201, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(3).getId());
			
			sysCont.addTask("12:01:29.0	TRIG 1");
			waitForCommand("12:01:29.0 TRIG 1");
			assertTrue(sysCont.getChannels()[0].getSensorState());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(0).getIsCompeting());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(1).getIsCompeting());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(2).getIsCompeting());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(3).getIsCompeting());

			
			sysCont.addTask("12:01:30.0	TRIG 1");
			waitForCommand("12:01:30.0 TRIG 1");
			assertTrue(sysCont.getChannels()[0].getSensorState());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(0).getIsCompeting());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(1).getIsCompeting());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(2).getIsCompeting());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(3).getIsCompeting());
			
			sysCont.addTask("12:01:31.0	TRIG 3");
			waitForCommand("12:01:31.0 TRIG 3");
			assertTrue(sysCont.getChannels()[1].getSensorState());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(0).getIsCompeting());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(1).getIsCompeting());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(2).getIsCompeting());
			assertTrue(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(3).getIsCompeting());
			
			sysCont.addTask("12:01:32.0	TRIG 2");
			waitForCommand("12:01:32.0 TRIG 2");
			assertEquals(4, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertFalse(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(0).getIsCompeting());
			
			sysCont.addTask("12:01:33.0	TRIG 4");
			waitForCommand("12:01:33.0 TRIG 4");
			assertEquals(4, sysCont.getTimer().getCurrentEvent().getCurrentParticipants().size());
			assertFalse(sysCont.getTimer().getCurrentEvent().getCurrentParticipants().get(1).getIsCompeting());
			
			sysCont.addTask("12:01:34.0	PRINT");
			waitForCommand("12:01:34.0 PRINT");
			sysCont.addTask("12:01:35.0	EXPORT");
			waitForCommand("12:01:35.0 EXPORT");
			sysCont.addTask("12:01:36.3	OFF");
			waitForCommand("12:01:36.3 OFF");
			sysCont.addTask("12:01:37.3	ON");
			waitForCommand("12:01:37.3 ON");
			sysCont.addTask("12:01:38.0	NEWRUN");
			waitForCommand("12:01:38.0 NEWRUN");
			sysCont.addTask("12:01:39.1	EVENT GRP");
			waitForCommand("12:01:39.1 EVENT GRP");
			assertEquals(EventType.GRP+ "", sysCont.getTimer().getCurrentEvent().getType().toString());
			assertEquals(EventType.GRP + "", sysCont.getTimer().getCurrentEvent().getEventName());
			
			sysCont.addTask("12:01:40.4	TOGGLE 1");
			waitForCommand("12:01:40.4 TOGGLE 1");
			assertEquals(0, sysCont.getChannels()[0].getId());
			assertTrue(sysCont.getChannels()[0].getState());
			assertFalse(sysCont.getChannels()[0].getSensorState());
			//assertEquals(SensorType.GATE.toString(), sysCont.getChannels()[0].getSensorType());
			
			sysCont.addTask("12:01:41.0	TOGGLE 2");
			waitForCommand("12:01:41.0 TOGGLE 2");
			assertEquals(1, sysCont.getChannels()[1].getId());
			assertTrue(sysCont.getChannels()[1].getState());
			assertFalse(sysCont.getChannels()[1].getSensorState());
			//assertEquals(SensorType.EYE.toString(), sysCont.getChannels()[1].getSensorType());
			
			sysCont.addTask("12:01:42.1	TRIG 1");
			waitForCommand("12:01:42.1 TRIG 1");
			assertFalse(sysCont.getChannels()[0].getSensorState());
			
			sysCont.addTask("12:01:43.0	TRIG 2");
			waitForCommand("12:01:43.0 TRIG 2");
			assertFalse(sysCont.getChannels()[2].getSensorState());

			sysCont.addTask("12:01:44.0	TRIG 2");
			waitForCommand("12:01:44.0 TRIG 2");
			assertFalse(sysCont.getChannels()[2].getSensorState());

			sysCont.addTask("12:01:45.8	TRIG 2");
			waitForCommand("12:01:45.8 TRIG 2");
			assertFalse(sysCont.getChannels()[2].getSensorState());

			sysCont.addTask("12:01:46.0	TRIG 2");
			waitForCommand("12:01:46.0 TRIG 2");
			assertFalse(sysCont.getChannels()[2].getSensorState());

			sysCont.addTask("12:01:47.0	TRIG 2");
			waitForCommand("12:01:47.0 TRIG 2");
			assertFalse(sysCont.getChannels()[2].getSensorState());

			sysCont.addTask("12:01:48.3	TRIG 2");
			waitForCommand("12:01:48.3 TRIG 2");
			assertFalse(sysCont.getChannels()[2].getSensorState());

			sysCont.addTask("12:01:49.0	TRIG 2");
			waitForCommand("12:01:49.0 TRIG 2");
			assertFalse(sysCont.getChannels()[2].getSensorState());

			sysCont.addTask("12:01:50.0	TRIG 2");
			waitForCommand("12:01:50.0 TRIG 2");
			assertFalse(sysCont.getChannels()[2].getSensorState());

			sysCont.addTask("12:01:51.0	ENDRUN");
			waitForCommand("12:01:51.0 ENDRUN");
			sysCont.addTask("12:01:52.0	PRINT");
			waitForCommand("12:01:52.0 PRINT");
			sysCont.addTask("12:01:53.0	EXPORT");
			waitForCommand("12:01:53.0 EXPORT");
			sysCont.addTask("12:01:54.0	OFF");
			waitForCommand("12:01:54.0 OFF");
			sysCont.addTask("12:01:55.0	EXIT");
			waitForCommand("12:01:55.0 EXIT");
			System.out.println("\t-------Successfully tested addTask()-------\n");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
