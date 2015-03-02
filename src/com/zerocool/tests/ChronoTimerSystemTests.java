package com.zerocool.tests;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
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

	SystemController systemController;
	SystemTime systemTime;
	Timer timer;
	EventLog eventLog;
	Participant john;
	Queue<String> commandList;

	@Before
	public void setUp() {
		systemController = new SystemController();
		systemTime = systemController.getSystemTime();
		timer = systemController.getTimer();
		eventLog = systemController.getEventLog();
		john = new Participant("John Doe", 55);
	}

	@After
	public void tearDown() {
		systemController = null;
		systemTime = null;
		timer = null;
		eventLog = null;
		john = null;
	}
	
	private ArrayList<String> helperParser(String str) {
		String [] atrArr = str.split("[:. \\t]");
		ArrayList<String> parsedList = new ArrayList<String>();
		
		for (String stri : atrArr) {
			parsedList.add(stri);
		}
		
		return parsedList;
	}
	
	@Test
	public void testStartAndFinished() {
		commandList = new LinkedList<String>();
		
	}
	
	@Test
	public void testStartAndDNF() {
		commandList = new LinkedList<String>();
	}
	
	@Test
	public void testStartAndCanceled() {
		commandList = new LinkedList<String>();
	}

	@Test
	public void testGetRecordCount() {		
		// Act
		for (int i = 0; i < 10; ++i) {
			timer.createEvent(EventType.IND, "Practice " + i);
			timer.addParticipantToStart(john);
			timer.startNextParticipant();
			timer.finishAllParticipants(false);
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

		// Act
		for (int i = 0; i < 5; ++i) {
			timer.createEvent(EventType.IND, "Practice " + i);
			timer.addParticipantToStart(john);

			// Stop the time so the times are equal.
			systemTime.suspend();;
			timer.startNextParticipant();
			startTime[i] = systemTime.getTime();
			// Start the time again so we get fun numbers.
			systemTime.resume();

			try {
				Thread.sleep(sleep[i]);
			} catch (Exception e) { };

			// Stop the time so the times are equal.
			systemTime.suspend();
			timer.finishParticipant(john);
			finishTime[i] = systemTime.getTime();
			// Start the time again for fun times.
			systemTime.resume();
		}

		// Assert
		for (int i = 0; i < 5; ++i) {
			Record rec = john.getRecord(i);
			assertEquals(startTime[i], rec.getStartTime());
			assertEquals(finishTime[i], rec.getFinishTime());
			assertEquals(finishTime[i] - startTime[i], rec.getElapsedTime());
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
		timer.startNextParticipant();
		
		try {
			Thread.sleep(1000);
		} catch (Exception e) { };
		
		timer.finishParticipant(john);
		
		systemTime.suspend();
		participantData = "Run\tBIB\tTime\n" + timer.getEventParticipantData();

		eventLog.logParticipants(timer.getCurrentEvent().getCurrentParticipants(), systemTime);

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
