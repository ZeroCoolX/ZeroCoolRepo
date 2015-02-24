package com.zerocool.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.systemcontroller.event.*;
import com.zerocool.systemcontroller.eventlog.EventLog;
import com.zerocool.systemcontroller.systemtime.SystemTime;

public class ChronoTimerSystemTests {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testEventLog() {
		EventLog log = new EventLog();
		AbstractEvent event = new Individual();
		SystemTime time = new SystemTime();
		
		log.logEvent(event, time);
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}
}
