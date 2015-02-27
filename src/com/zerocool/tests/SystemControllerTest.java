package com.zerocool.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.controllers.SystemController;
import com.zerocool.entities.Participant;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Sensor.SensorType;
import com.zerocool.services.EventLog;
import com.zerocool.services.SystemTime;

public class SystemControllerTest {
	
	SystemController sysCont = null;
	File file = null;

	@Before
	public void setUp() throws Exception {
		sysCont = new SystemController();
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testSystemController(){
		System.out.println("Testing SystemController()");
		assertNotNull(sysCont.getChannels());
		assertNotNull(sysCont.getEventLog());
		assertNotNull(sysCont.getTimer());
		assertNotNull(sysCont.getSystemTime());
		assertEquals(0, sysCont.getId());
	}
	
	@Test
	public void executeCommand() {
		try {
			ArrayList<String> testString;
			
			testString = helperParser("12:01:02.0	ON");
			sysCont.executeCommand(testString.get(4), testString);
			assertNotNull(sysCont.getEventLog());
			assertNotNull(sysCont.getTimer());
			assertEquals(EventType.IND, sysCont.getTimer().getCurrentEvent().getType());
			assertEquals(EventType.IND + "", sysCont.getTimer().getCurrentEvent().getEventName());
			assertNotNull(sysCont.getTimer().getCurrentEvent().getParticipants());
			assertNotNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());

			
			testString = helperParser("12:01:04.0	OFF");
			sysCont.executeCommand(testString.get(4), testString);
			assertNull(sysCont.getEventLog());
			assertNull(sysCont.getTimer());
			assertNull(sysCont.getChannels());
			assertFalse(sysCont.getIsPrinterOn());
			
			
			testString = helperParser("12:01:08.0	ON");
			sysCont.executeCommand(testString.get(4), testString);
			
			
			testString = helperParser("12:01:10.0	CONN GATE 1");
			sysCont.executeCommand(testString.get(4), testString);
			assertEquals(1, sysCont.getChannels().size());
			assertEquals(1, sysCont.getChannels().get(0).getId());
			assertTrue(sysCont.getChannels().get(0).getState());
			assertEquals(SensorType.GATE, sysCont.getChannels().get(0).getSensorType());
			assertFalse(sysCont.getChannels().get(0).getSensorState());
			
			
			testString = helperParser("12:01:14.0	TOGGLE 1");
			sysCont.executeCommand(testString.get(4), testString);
			assertEquals(1, sysCont.getChannels().size());
			assertTrue(sysCont.getChannels().get(0).getState());
			assertTrue(sysCont.getChannels().get(0).getSensorState());
			assertEquals(SensorType.GATE, sysCont.getChannels().get(0).getSensorType());
			
			
			testString = helperParser("12:01:18.0	NUM 234");
			sysCont.executeCommand(testString.get(4), testString);
			assertEquals(1, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(1, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(1, sysCont.getTimer().getCurrentEvent().getParticipants());
			assertEquals(234, sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getID());
			
			
			testString = helperParser("12:01:20.0	NUM 315");
			sysCont.executeCommand(testString.get(4), testString);
			assertEquals(2, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(2, sysCont.getTimer().getCurrentEvent().getParticipants());
			
			
			testString = helperParser("12:01:22.0	START");
			sysCont.executeCommand(testString.get(4), testString);
			for (Participant curPar : sysCont.getTimer().getCurrentEvent().getParticipants()) {
				assertNotNull(curPar.getLastRecord());
				assertEquals(1, curPar.getLastRecord().getEventID());
				assertEquals("IND", curPar.getLastRecord().getEventName());
			}
			assertTrue(sysCont.getTimer().getCurrentEvent().getCompetingParticipant().getIsCompeting());
			
			assertEquals(315, sysCont.getTimer().getCurrentEvent().getStartingQueue().peek().getID());
			
			
			testString = helperParser("12:01:24.0	NUM 435");
			assertEquals(3, sysCont.getTimer().getTotalParticipants().size());
			assertEquals(3, sysCont.getTimer().getCurrentEvent().getStartingQueue().size());
			assertEquals(3, sysCont.getTimer().getCurrentEvent().getParticipants());
			
			
			testString = helperParser("12:01:26.0	FIN");
			testString = helperParser("12:01:28.0	START");
			testString = helperParser("12:01:30.0	START");
			testString = helperParser("12:01:32.0	DNF");
			testString = helperParser("12:01:34.0	FIN");
			testString = helperParser("12:01:36.0	PRINTF");
			testString = helperParser("12:01:38.0	OFF");
			testString = helperParser("12:01:40.0	EXIT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> helperParser(String str){
		String [] atrArr = str.split("[:. \\t]");
		ArrayList<String> parsedList = new ArrayList<String>();
		for (String stri : atrArr) {
			parsedList.add(stri);
		}
		return parsedList;
	}
	
	
	@Test
	public void testReadFile(){
		System.out.println("Testing readFile()");
		file = new File("test_files/syscontroljunittestfile.txt");
		sysCont.readFile(file);
		Queue<ArrayList<String>> commandList = sysCont.getCommandList();
		ArrayList<String> cmd = commandList.remove();
		assertEquals("TESTON", cmd.get(4));
		cmd = commandList.remove();
		assertEquals("TESTOFF", cmd.get(4));
		cmd = commandList.remove();
		assertEquals("TESTCONN", cmd.get(4));
		assertEquals("GATE", cmd.get(5));
		assertEquals("1", cmd.get(6));
		cmd = commandList.remove();
		assertEquals("TESTTOG", cmd.get(4));
		assertEquals("1", cmd.get(5));
		cmd = commandList.remove();
		assertEquals("TESTNUM", cmd.get(4));
		assertEquals("315", cmd.get(5));
		cmd = commandList.remove();
		assertEquals("TESTSTART", cmd.get(4));
		cmd = commandList.remove();
		assertEquals("TESTFIN", cmd.get(4));
		cmd = commandList.remove();
		assertEquals("TESTDNF", cmd.get(4));
		cmd = commandList.remove();
		assertEquals("TESTPRINT", cmd.get(4));
		cmd = commandList.remove();
		assertEquals("TESTEXIT", cmd.get(4));
	}

}
