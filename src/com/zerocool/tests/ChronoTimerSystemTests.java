package com.zerocool.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.controllers.SystemController;
import com.zerocool.controllers.Timer;
import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.Individual;
import com.zerocool.services.EventLog;
import com.zerocool.services.SystemTime;

public class ChronoTimerSystemTests {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testExecuteCommand() {
		
	}
	
	@Test
	public void testEventLog() {
		// Arrange
		SystemTime time = new SystemTime();
		EventLog log = new EventLog();
		AbstractEvent event1 = new Individual("Indy500", 1);
		AbstractEvent event = new Individual("Indy600", 2);
		Timer timer = new Timer(time);
		SystemController controller = new SystemController(timer, log, 1);
		time.start();
		event1.setName("another event");
		event1.setEventTime(time.getTime());
		event.setName("Cool Event");
		event.setEventTime(time.getTime());
		
		// Act
		log.logEvent(event, time);
		// log.logEvent(event1, time);
		// Assert
		try {
			controller.cmdPrint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInputFile() {
        System.out.println("Testing...testing....\n\n");
		
		SystemController systemController = new SystemController();
		File file = new File("test_files/syscontrolFastFileTest.txt");
		System.out.println("\n\nTesting readFile\n\n");
		systemController.readFile(file);
		System.out.println("\n\nTesting readInput\n\n");
	}
	
}
