package com.zerocool.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.controllers.SystemController;
import com.zerocool.controllers.Timer;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Participant;
import com.zerocool.entities.Record;
import com.zerocool.services.EventLog;
import com.zerocool.services.SystemTime;

public class ChronoTimerSystemTests {

	Queue<String> commandList;
	
	SystemController systemController;
	SystemTime systemTime;
	Timer timer;
	EventLog eventLog;
	Participant john;

	@Before
	public void setUp() {
		commandList = new LinkedList<String>();
		systemController = new SystemController();
		systemTime = systemController.getSystemTime();
		timer = systemController.getTimer();
		eventLog = systemController.getEventLog();
		john = new Participant("John Doe", 55);
	}

	@After
	public void tearDown() {
		commandList = null;
		systemController = null;
		systemTime = null;
		timer = null;
		eventLog = null;
		john = null;
	}
	
	private void executeCommands() {
		while (!commandList.isEmpty()) {
			try {			
				systemController.executeCommand(commandList.poll());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			Thread.sleep(1000);
		} catch (Exception e) { };
	}
	
	@Test
	public void testOneStartAndFinished() {
		// Arrange
		Participant competitor;
		
		long startTime = 0;
		long finishTime = 0;
		long elapsedTime = 0;
		
		commandList.add("10:00:00.0	TIME 10:01:00.0");
		commandList.add("10:01:02.0	ON");
		commandList.add("10:01:04.0 CONN GATE 1");
		commandList.add("10:01:06.0 TOGGLE 1");
		commandList.add("10:01:08.0 CONN GATE 2");
		commandList.add("10:01:10.0 TOGGLE 2");
		commandList.add("10:01:20.0	NUM 555");
		commandList.add("10:01:22.0	START");
		commandList.add("10:02:26.0	FIN");
		
		timer.resetEventId();
		
		// Act
		executeCommands();
		
		startTime = SystemTime.getTimeInMillis("10:01:22.0");
		finishTime = SystemTime.getTimeInMillis("10:02:26.0");
		elapsedTime = finishTime - startTime;
		
		timer = systemController.getTimer();
		competitor = timer.findParticipant(555);
		elapsedTime = (finishTime - startTime);
		
		// Assert
		assertNotNull(competitor);
		assertEquals(1, competitor.getLastRecord().getEventId());
		assertEquals(startTime, competitor.getLastRecord().getStartTime());
		assertEquals(finishTime, competitor.getLastRecord().getFinishTime());
		assertEquals(elapsedTime, competitor.getLastRecord().getElapsedTime());
		assertFalse(competitor.getLastRecord().getDnf());
	}
	
	@Test
	public void testOneStartAndDNF() {
		// Arrange
		Participant competitor;
		
		long startTime = 0;
		long finishTime = -1;
		
		commandList.add("10:00:00.0	TIME 10:01:00");
		commandList.add("10:01:02.0	ON");
		commandList.add("10:01:04.0 CONN GATE 1");
		commandList.add("10:01:06.0 TOGGLE 1");
		commandList.add("10:01:20.0	NUM 555");
		commandList.add("10:01:22.0	START");
		commandList.add("10:02:00.0	DNF");
		
		// Act
		executeCommands();
		
		startTime = SystemTime.getTimeInMillis("10:01:22.0");
		
		timer = systemController.getTimer();
		competitor = timer.findParticipant(555);
		
		// Assert
		assertNotNull(competitor);
		assertEquals(startTime, competitor.getLastRecord().getStartTime());
		assertEquals(finishTime, competitor.getLastRecord().getFinishTime());
		assertTrue(competitor.getLastRecord().getDnf());
	}
	
	@Test
	public void testStartAndCanceled() {
		fail("Not implemented yet");
		
		// Ensure that on cancel participant is queed to start
	}

	@Test
	public void testGetRecordCount() {		
		// Act
		for (int i = 0; i < 10; ++i) {
			timer.createEvent(EventType.IND, "Practice " + i);
			timer.addParticipantToStart(john);
			timer.triggered(1);
			timer.triggered(2);
		}

		// Assert
		assertEquals(10, john.getRecordCount());
		assertEquals("John Doe", john.getName());
		assertEquals(55, john.getId());
	}

	@Test
	public void testRecordTimes() {
		// Arrange
		int[] sleep = { 100, 200, 500, 1000, 1200 };
		long[] startTime = { 0, 0, 0, 0, 0 };
		long[] finishTime = { 0, 0, 0, 0, 0 };

		timer.resetEventId();
		
		// Act
		for (int i = 0; i < 5; ++i) {
			timer.createEvent(EventType.IND, "Practice " + i);
			timer.addParticipantToStart(john);

			// Stop the time so the times are equal.
			systemTime.suspend();;
			timer.triggered(1);
			startTime[i] = systemTime.getTime();
			// Start the time again so we get fun numbers.
			systemTime.resume();

			try {
				Thread.sleep(sleep[i]);
			} catch (Exception e) { };

			// Stop the time so the times are equal.
			systemTime.suspend();
			timer.triggered(2);
			finishTime[i] = systemTime.getTime();
			// Start the time again for fun times.
			systemTime.resume();
		}

		// Assert
		for (int i = 0; i < 5; ++i) {
			Record rec = john.getRecord(i);
			assertEquals(i + 1, rec.getEventId());
			assertEquals("Practice " + i, rec.getEventName());
			assertEquals(startTime[i], rec.getStartTime());
			assertEquals(finishTime[i], rec.getFinishTime());
			assertEquals(finishTime[i] - startTime[i], rec.getElapsedTime());
			assertFalse(rec.getDnf());
		}
	}

	@Test
	public void testOneEventLogEvent() {
		// Arrange
		File eventFile = new File("eventOut.txt");
		String eventData = "";
		String fileData = "";
		String line = "";

		// Act
		timer.createEvent(EventType.IND, "Indie 500");
		systemTime.suspend();
		eventData = systemTime + " " + timer.getEventData();

		eventLog.logEvent(timer.getEventData(), systemTime);

		try {
			BufferedReader br = new BufferedReader(new FileReader(eventFile));
			line = br.readLine();

			while (line != null) {
				fileData += line + "\n";
				line = br.readLine();
			}

			br.close();
		} catch (Exception e) {	
			System.err.println("Couldn't read the event file.");
		}

		// Assert
		assertEquals(eventData, fileData);

		eventLog.exit();
	}

	@Test
	public void testOneEventLogParticipant() {
		// Arrange
		File eventFile = new File("participantOut.txt");
		String participantData = "";
		String fileData = "";
		String line = "";

		// Act
		timer.createEvent(EventType.IND, "Indie 500");
		timer.addParticipantToStart(john);
		timer.triggered(1);
		
		try {
			Thread.sleep(1000);
		} catch (Exception e) { };
		
		timer.triggered(2);
		
		systemTime.suspend();
		participantData = "Run\tBIB\tTime\n" + timer.getEventParticipantData();

		eventLog.logParticipants(timer.getEventParticipantData(), systemTime);

		try {
			BufferedReader br = new BufferedReader(new FileReader(eventFile));
			line = br.readLine();

			while (line != null) {
				fileData += line + "\n";
				line = br.readLine();
			}

			br.close();
		} catch (Exception e) {	
			System.err.println("Couldn't read the participant file.");
		}

		// Assert
		assertEquals(participantData, fileData);

		eventLog.exit();
	}
}
