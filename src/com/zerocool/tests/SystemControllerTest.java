package com.zerocool.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.controllers.SystemController;
import com.zerocool.services.EventLog;
import com.zerocool.services.SystemTime;

public class SystemControllerTest {
	
	SystemController sysCont = null;

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
	public void testReadFile(){
		System.out.println("Testing readFile()");
		File file = new File("test_files/syscontroljunittestfile.txt");
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
