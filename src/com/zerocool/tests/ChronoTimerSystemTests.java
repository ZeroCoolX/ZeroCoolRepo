package com.zerocool.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.controllers.SystemController;
import com.zerocool.controllers.Timer;
import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.Individual;
import com.zerocool.entities.Participant;
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
	}
	
	@Test
	public void testEventLogPrintParticipants() {
		// Arrange
		systemTime.start();
		EventLog log = new EventLog();
		Participant participant = new Participant(155, "Name 155");
		AbstractEvent individual = new Individual("Event 1", 1);
		individual.addNewParticipant(participant);
		
		// Act
		individual.initializeEvent();
		individual.startOneParticipant(participant, systemTime.getTime());
		
		for(long i = 0; i <= 500000000l; ++i) {
			// adding delay before individual finishes
		}
		individual.finishOneParticipant(participant, systemTime.getTime());
		log.logParticipants(individual, systemTime);
		
		// Assert
		try(FileReader fileReader = new FileReader(log.getParticipantFile());
		BufferedReader reader = new BufferedReader(fileReader)) {
			
			String fileLine = reader.readLine();
			assertEquals(fileLine, "Run   BIB      Time");
			fileLine = reader.readLine();
			assertEquals(fileLine, participant.getFormattedData(2));
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
