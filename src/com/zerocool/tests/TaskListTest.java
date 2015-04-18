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
		try {
			assertFalse(task.addTask(input + " T"));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + input + " T the format is invalid or the command is not supported.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		input += " TIME";
		try {
			assertFalse(task.addTask(input));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + input + " the format is incorrect.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		String other = input + " a0:00:00.0";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " one or more of the arguments are not valid numbers.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " 0a:00:00.0";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " one or more of the arguments are not valid numbers.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}
		
		other = input + " 00:a0:00.0";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " one or more of the arguments are not valid numbers.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " 00:0a:00.0";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " one or more of the arguments are not valid numbers.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " 00:00:a0.0";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " one or more of the arguments are not valid numbers.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " 00:00:0a.0";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " one or more of the arguments are not valid numbers.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " 00:00:00.a";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " one or more of the arguments are not valid numbers.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " 00:00:00.0";
		try {
			assertTrue(task.addTask(other));
		} catch (Exception e) {
			fail("Should not throw any Exception!");
		}

		other = input + " 10:00:00.0";
		try {
			assertTrue(task.addTask(other));
		} catch (Exception e) {
			fail("Should not throw any Exception!");
		}
	}

	@Test
	public void testConn() {
		try {
			assertFalse(task.addTask(input + " C"));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + input + " C the format is invalid or the command is not supported.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		input += " CONN";
		try {
			assertFalse(task.addTask(input));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + input + " the format is incorrect.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		String other = input + " GATE";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " the format is incorrect.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " G 1";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " argument G is not a valid Sensor Type.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " GATE A";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " argument A is not a valid number.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " GATE 0";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " argument 0 is not a valid channel.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " GATE 1";
		try {
			assertTrue(task.addTask(other));
		} catch (Exception e) {
			fail("Should not throw any Exception!");
		}

		other = input + " EYE 1";
		try {
			assertTrue(task.addTask(other));
		} catch (Exception e) {
			fail("Should not throw any Exception!");
		}

		other = input + " PAD 1";
		try {
			assertTrue(task.addTask(other));
		} catch (Exception e) {
			fail("Should not throw any Exception!");
		}
	}

	@Test
	public void testEvent() {
		try {
			assertFalse(task.addTask(input + " E"));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + input + " E the format is invalid or the command is not supported.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		input += " EVENT";
		try {
			assertFalse(task.addTask(input));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + input + " the format is incorrect.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		String other = input + " IN";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " argument IN is not a valid Event Type.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " IND 1";
		try {
			assertFalse(task.addTask(other));
		} catch (IllegalArgumentException arg) {
			assertEquals("For " + other + " the format is incorrect.", arg.getMessage());
		} catch (Exception e) {
			fail("Should throw IllegalArgumentException not " + e);
		}

		other = input + " IND";
		try {
			assertTrue(task.addTask(other));
		} catch (Exception e) {
			fail("Should not throw any Exception!");
		}
		
	}

	@Test
	public void testtNumClr() {
		fail("Not yet implemented");
	}

	@Test
	public void testTrigDiscToggle() {
		fail("Not yet implemented");
	}

}
