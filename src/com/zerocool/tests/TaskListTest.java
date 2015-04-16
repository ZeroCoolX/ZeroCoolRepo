package com.zerocool.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zerocool.controllers.TaskList;

public class TaskListTest {

	private TaskList task;
	private String input;
	
	@Before
	public void setUp() throws Exception {
		task = new TaskList();
		input = "10:00:00.0";
	}

	@After
	public void tearDown() throws Exception {
		task = null;
		input = null;
	}

	@Test
	public void testTime() {
		assertFalse(task.addTask(input + " T"));
		assertEquals(task.getErrorMessage(), "For " + input + " T the format is invalid or the command is not supported.");
		
		input += " TIME";
		assertFalse(task.addTask(input));
		assertEquals(task.getErrorMessage(), "For " + input + " the format is incorrect.");
		
		String other = input + " a0:00:00.0";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " one or more of the arguments are not valid numbers.");
		
		other = input + " 0a:00:00.0";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " one or more of the arguments are not valid numbers.");
		
		other = input + " 00:a0:00.0";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " one or more of the arguments are not valid numbers.");
		
		other = input + " 00:0a:00.0";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " one or more of the arguments are not valid numbers.");
		
		other = input + " 00:00:a0.0";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " one or more of the arguments are not valid numbers.");
		
		other = input + " 00:00:0a.0";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " one or more of the arguments are not valid numbers.");
		
		other = input + " 00:00:00.a";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " one or more of the arguments are not valid numbers.");
		
		other = input + " 00:00:00.0";
		assertTrue(task.addTask(other));
		assertEquals(task.getErrorMessage(), "");
		
		other = input + " 10:00:00.0";
		assertTrue(task.addTask(other));
		assertEquals(task.getErrorMessage(), "");
	}
	
	@Test
	public void testConn() {
		assertFalse(task.addTask(input + " C"));
		assertEquals(task.getErrorMessage(), "For " + input + " C the format is invalid or the command is not supported.");
		
		input += " CONN";
		assertFalse(task.addTask(input));
		assertEquals(task.getErrorMessage(), "For " + input + " the format is incorrect.");
		
		String other = input + " GATE";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " the format is incorrect.");
		
		other = input + " G 1";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " argument G is not a valid Sensor Type.");
		
		other = input + " GATE A";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " argument A is not a valid number.");
		
		other = input + " GATE 0";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " argument 0 is not a valid channel.");
		
		other = input + " GATE 1";
		assertTrue(task.addTask(other));
		assertEquals(task.getErrorMessage(), "");
		
		other = input + " EYE 1";
		assertTrue(task.addTask(other));
		assertEquals(task.getErrorMessage(), "");
		
		other = input + " PAD 1";
		assertTrue(task.addTask(other));
		assertEquals(task.getErrorMessage(), "");
	}
	
	@Test
	public void testEvent() {
		assertFalse(task.addTask(input + " E"));
		assertEquals(task.getErrorMessage(), "For " + input + " E the format is invalid or the command is not supported.");
		
		input += " EVENT";
		assertFalse(task.addTask(input));
		assertEquals(task.getErrorMessage(), "For " + input + " the format is incorrect.");
		
		String other = input + " IN";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " argument IN is not a valid Event Type.");
		
		other = input + " IND 1";
		assertFalse(task.addTask(other));
		assertEquals(task.getErrorMessage(), "For " + other + " the format is incorrect.");
		
		other = input + " IND";
		assertTrue(task.addTask(other));
		assertEquals(task.getErrorMessage(), "");
	}

	@Test
	public void testPrintExportNumClr() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testTrigDiscToggle() {
		fail("Not yet implemented");
	}

}
