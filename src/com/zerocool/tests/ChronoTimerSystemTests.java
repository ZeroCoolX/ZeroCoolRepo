package com.zerocool.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.systemcontroller.SystemController;
import com.zerocool.systemcontroller.event.*;
import com.zerocool.systemcontroller.eventlog.EventLog;
import com.zerocool.systemcontroller.systemtime.SystemTime;
import com.zerocool.systemcontroller.timer.Timer;

public class ChronoTimerSystemTests {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testEventLog() {
		// Arrange
		SystemTime time = new SystemTime(System.currentTimeMillis());
		EventLog log = new EventLog();
		AbstractEvent event1 = new Individual();
		AbstractEvent event = new Individual();
		Timer timer = new Timer();
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
		controller.cmdPrint();
		
//		SystemController controller = new SystemController(timer, log, 1);
//		// set the time and name of event and test to see if 
//		// it outputs a file in the working directory.
//		controller.executeCommand(new Date(),"ON", new ArrayList<String>());
//		controller.executeCommand(new Date(),"ON", new ArrayList<String>());
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}
}
