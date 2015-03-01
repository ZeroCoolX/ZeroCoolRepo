package com.zerocool.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.controllers.SystemController;
import com.zerocool.controllers.Timer;
import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.Individual;
import com.zerocool.entities.Participant;
import com.zerocool.entities.Record;
import com.zerocool.services.EventLog;
import com.zerocool.services.SystemTime;

public class ChronoTimerSystemTests {

	SystemController systemController = null;
	SystemTime systemTime = new SystemTime();
	
	@Before
	public void setUp() throws Exception {
		systemController = new SystemController();
		systemTime.setTime(12 * 3600000
				+ 30 * 60000
				+ 10 * 1000);
	}

	@After
	public void tearDown() throws Exception {
		systemTime.reset();
	}
	
	@Test
	public void testGetRecordCount() {
		// Arrange
		String name = "John Doe";
		int id = 55;
		int numRecords = 10;
		Participant participant = new Participant(id, name);
		
		// Act
		for(int i = 1; i <= numRecords; ++i) {
			AbstractEvent ind = new Individual("IND" + i, i);
			participant.createNewRecord(ind.getEventName(), ind.getEventId());
		}
		
		// Assert
		assertEquals(numRecords, participant.getRecordCount());
		assertEquals(name, participant.getName());
		assertEquals(id, participant.getID());
	}
	
	@Test
	public void testRecord() {
		// Arrange
		AbstractEvent ind = new Individual("Record Event", (int)(Math.random() *10));
		Record record = new Record(ind.getEventName(), ind.getEventId());
		
		// Act
		long startTime = System.currentTimeMillis();
		long finishTime = System.currentTimeMillis() * 10000;
		long elapsedTime = finishTime - startTime;
		record.setStartTime(startTime);
		record.setFinishTime(finishTime);
		record.setDnf(false);
		
		// Assert
		assertEquals(false, record.getDnf());
		assertEquals(startTime, record.getStartTime());
		assertEquals(finishTime, record.getFinishTime());
		assertEquals(elapsedTime, record.getElapsedTime());		
	}
	
	@Test
	public void testEventLog() {
		// Arrange
		EventLog log = new EventLog();
		AbstractEvent event1 = new Individual("Indy500", 1);
		AbstractEvent event = new Individual("Indy600", 2);
		Timer timer = new Timer(systemTime);
		systemController = new SystemController(timer, log, 1);
		systemTime.start();
		
		for(int i = 0; i <= 100000000; ++i) {
			// adding delay before setting time
		}
		
		event1.setName("another event");
		event1.setEventTime(systemTime.getTime());
		event.setName("Cool Event");
		event.setEventTime(systemTime.getTime());
		
		// Act
		log.logEvent(event, systemTime);
		log.logEvent(event1, systemTime);
		
		systemTime.exit();
		
		// Assert
		// just making sure an output file is generated and printed with cmdPrint()
		try {
			systemController.cmdPrint();
			EventLog sysLog = systemController.getEventLog();
			assertEquals(sysLog, log);
			assertSame(sysLog.getEventFile(), log.getEventFile());
		} catch (Exception e) {
			e.printStackTrace();		
		}
		log.exit();
	}
	
	@Test
	public void testEventLogPrintParticipants() {
		// Arrange
		systemTime.start();
		EventLog log = new EventLog();
		Participant participant = new Participant(155, "Name 155");
		AbstractEvent individual = new Individual("Event 1", systemTime.getTime());
		systemController.currentTimer.addParticipantToStart(participant);
		
		// Act
		systemController.currentTimer.startNextParticipant();
		
		for(long i = 0; i <= 500000000l; ++i) {
			// adding delay before individual finishes
		}
		systemController.currentTimer.finishParticipant(participant);
		log.logParticipants(individual, systemTime);
		
		// Assert
		try(FileReader fileReader = new FileReader(log.getParticipantFile());
		BufferedReader reader = new BufferedReader(fileReader)) {
			
			String fileLine = reader.readLine();
			assertEquals(fileLine, "Run   BIB      Time");
			fileLine = reader.readLine();
			assertEquals(fileLine, participant.getFormattedData(individual.getEventId()));
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log.exit();
	}
}
