package com.zerocool.controllers;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * This class represents a Queue of Tasks that the SystemController should execute.  Sometimes the Tasks 
 * will be executed right away so this also plays the role of parsing a String and making sure the command
 * is valid for use within SystemController.
 * @author ZeroCool
 *
 */
public class TaskList {

	/**
	 * Nested class to help handle and hold data for a Task.  A Task is just a command to be executed by the SystemController.
	 * @author ZeroCool
	 */
	public class Task {
		
		String time;
		String command;
		String argumentOne;
		String argumentTwo;
		
		/**
		 * Only constructor for a Task because there is no Task if it doesn't have a command.
		 * @param arguments - The string of arguments entered.
		 */
		public Task(ArrayList<String> arguments) {
			time = "" + arguments.get(0) + ":" + arguments.get(1) + ":" + arguments.get(2) + "." + arguments.get(3) + "00";
			command = arguments.get(4);
			
			if (command.equals("TIME")) {
				argumentOne = arguments.get(5) + ":" + arguments.get(6) + ":" + arguments.get(7) + "." + (arguments.size() >= 9 ? arguments.get(8) + "00" : "000");
			} else {
				argumentOne = arguments.size() >= 6 ? arguments.get(5) : null;
			}
			
			argumentTwo = arguments.size() >= 7 ? arguments.get(6) : null;
		}
		
		/**
		 * Get's the command's desired execution time.
		 * @return - The time the command should be executed.
		 */
		public String getTaskTime() {
			return time;
		}
		
		/**
		 * Get's the command of the task.
		 * @return - The command of the task.
		 */
		public String getTaskCommand() {
			return command;
		}
		
		/**
		 * Get's the first argument if there is any.
		 * @return - The first argument otherwise null.
		 */
		public String getTaskArgumentOne() {
			return argumentOne;
		}
		
		/**
		 * Get's the second argument if there is any.
		 * @return - The second argument otherwise null.
		 */
		public String getTaskArgumentTwo() {
			return argumentTwo;
		}
		
	}
	
	// The Queue of Tasks.
	private Queue<Task> tasks;
	
	// Holds all the valid commands to search through when parsing.
	private Pattern validCommands;
	private Pattern extendedCommands;
	
	/**
	 * Default constructor that initializes the Queue and valid commands.
	 */
	public TaskList() {
		tasks = new LinkedList<Task>();
		validCommands = Pattern.compile("\\b(?:ON|OFF|EXIT|RESET|TIME|TOGGLE|CONN|DISC|EVENT|NEWRUN|ENDRUN|PRINT|EXPORT|NUM|CLR|SWAP|RCL|"
				+ "START|FIN|TRIG|ELAPSED|CANCEL|DNF)\\b");
		extendedCommands = Pattern.compile("\\b(?:ON|OFF|EXIT|RESET|TIME|TOGGLE|CONN GATE|CONN EYE|CONN PAD|DISC|EVENT IND|EVENT GRP|EVENT PARIND|EVENT PARGRP|NEWRUN|ENDRUN|PRINT|EXPORT|NUM|CLR|SWAP|RCL|"
				+ "START|FIN|TRIG|ELAPSED|CANCEL|DNF)\\b");
	}
	
	/**
	 * Reads in from a file and Queues all the valid tasks.
	 * @param file - The file to read from.
	 * @return - True if no exceptions were thrown else false.
	 * 
	 * (Note: could return true even tho there are no Tasks in the Queue 
	 * 	if all the arguments in the file were invalid.)
	 */
	public boolean addTask(File file) {
		if (file == null) {
			return false;
		}
		
		try {
			Scanner in = new Scanner(new FileReader(file));
			
			while (in.hasNextLine()) {
				addTask(in.nextLine());
			}
			
			in.close();
			return true;
		} catch (Exception e) { }
		
		return false;
	}
	
	/**
	 * Adds a Task to the Queue from a String.  This method parses the String
	 * to check for validation then creates a new Task and adds it to the
	 * Queue.
	 * @param input - The String to parse and create a new Task from.
	 * @return - True if valid arguments else false.
	 */
	public boolean addTask(String input) {
		System.out.println("input = " + input);
		if (input == null) {
			return false;
		}
		
		ArrayList<String> arguments = new ArrayList<String>();
		
		if (parse(input, arguments)) {
			tasks.add(new Task(arguments));
			System.out.println("Parsed: " + arguments);
			System.out.flush();
			return true;
		}
		
		return false;
	}
	
	/**
	 * USE FOR TESTING PURPOSES ONLY!
	 * Returns the Queue of Tasks.
	 * @return - The Task Queue.
	 */
	public Queue<Task> getTaskList() {
		return tasks;
	}
	
	/**
	 * Peeks at the next Task to be executed.  Does not remove
	 * it from the Queue.
	 * @return - The next Task.
	 */
	public Task peekNextTask() {
		return tasks.peek();
	}
	
	/**
	 * Polls the next Task from the Queue to be executed.  Removes
	 * it from the Queue.
	 * @return - The next Task.
	 */
	public Task pollNextTask() {
		return tasks.poll();
	}
	
	/**
	 * Checks the next Task's execution time.
	 * @return - The next Task's execution time.
	 */
	public String nextTaskTime() {
		return tasks.isEmpty() ? null : tasks.peek().getTaskTime();
	}
	
	/**
	 * Checks the next Task's command.
	 * @return - The next Task's command.
	 */
	public String nextTaskCommand() {
		return tasks.isEmpty() ? null : tasks.peek().getTaskCommand();
	}
	
	public String[] getCommandList(boolean useExtendedList) {
		return useExtendedList ? extendedCommands.pattern().replaceAll("[\\\\?b:()]", "").split("\\|") : validCommands.pattern().replaceAll("[\\\\?b:()]", "").split("\\|");
	}
	
	/**
	 * Checks whether the Queue is empty or not.
	 * @return - True if there are no Tasks else false.
	 */
	public boolean isEmpty() {
		return tasks.isEmpty();
	}
	
	/**
	 * Removes all remaning Tasks from the Queue.
	 */
	public void clearTasks() {
		tasks.clear();
	}
	
	/**
	 * Private helper method to parse the String wanting to become a Task.  First it checks
	 * to make sure there is a valid command within the String, there it splits the String
	 * up into parts and adds them to arguments and double checks before it returns.
	 * @param input - The string to parse.
	 * @param arguments - The ArrayList to add the parsed arguments into.
	 * @return - True if it was a valid String else false.
	 */
	private boolean parse(String input, ArrayList<String> arguments) {
		if (!validCommands.matcher(input).find()) {
			System.out.println("returning false");
			return false;
		}
		
		String[] split = input.split("[:. \\t]");
		for (String s : split) {
			System.out.println("addding = " + s);
			arguments.add(s);
		}
		
		return arguments.size() > 4 && validCommands.matcher(arguments.get(4)).find();
	}
	
}
