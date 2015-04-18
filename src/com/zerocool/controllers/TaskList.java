package com.zerocool.controllers;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.Sensor;
import com.zerocool.services.SystemTime;

/**
 * This class represents a Queue of Tasks that the SystemController should execute.  Sometimes the Tasks 
 * will be executed right away so this also plays the role of parsing a String and making sure the command
 * is valid for use within SystemController.
 * 
 * @author ZeroCool
 *
 */
public class TaskList {

	/**
	 * Nested class to help handle and hold data for a Task.  A Task is just a command to be executed by the SystemController.
	 * 
	 * @author ZeroCool
	 */
	public class Task {

		String time;
		String command;
		String argumentOne;
		String argumentTwo;

		/**
		 * Only constructor for a Task because there is no Task if it doesn't have a command.
		 * 
		 * @param arguments - The string of arguments entered.
		 */
		public Task(ArrayList<String> arguments) {
			time = "" + arguments.get(0) + ":" + arguments.get(1) + ":" + arguments.get(2) + "." + arguments.get(3);
			command = arguments.get(4);

			if (command.equals("TIME")) {
				argumentOne = arguments.get(5) + ":" + arguments.get(6) + ":" + arguments.get(7) + "." + (arguments.size() >= 9 ? arguments.get(8) : "0");
			} else {
				argumentOne = arguments.size() >= 6 ? arguments.get(5) : "";
				argumentTwo = arguments.size() >= 7 ? arguments.get(6) : "";
			}
		}

		/**
		 * Gets the command's desired execution time.
		 * 
		 * @return The time the command should be executed.
		 */
		public String getTaskTime() {
			return time;
		}

		/**
		 * Gets the command of the task.
		 * 
		 * @return The command of the task.
		 */
		public String getTaskCommand() {
			return command;
		}

		/**
		 * Gets the first argument if there is any.
		 * 
		 * @return The first argument otherwise null.
		 */
		public String getTaskArgumentOne() {
			return argumentOne;
		}

		/**
		 * Gets the second argument if there is any.
		 * 
		 * @return The second argument otherwise null.
		 */
		public String getTaskArgumentTwo() {
			return argumentTwo;
		}
		
		@Override
		public String toString() {
			return (time + " " + command + " " + (argumentOne != null ? argumentOne : "") + " " + (argumentTwo != null ? argumentTwo : "")).trim();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Task) {
				Task task = (Task) obj;
				if (task.time.equals(time) && task.command.equals(command)) {
					return (argumentOne != null ? task.getTaskArgumentOne().equals(argumentOne) : true) && (argumentTwo != null ? task.getTaskArgumentTwo().equals(argumentTwo) : true);
				}
			}
			
			return false;
		}

	}

	// The Queue of Tasks.
	private Queue<Task> tasks;

	// Holds all the valid commands to search through when parsing.
	private Pattern validCommands;
	private Pattern extendedCommands;
	private Pattern complexCommands;

	/**
	 * Default constructor that initializes the Queue and valid commands.
	 */
	public TaskList() {
		tasks = new LinkedList<Task>();
		validCommands = Pattern.compile("\\b(?:ON|OFF|EXIT|RESET|TIME|TOGGLE|CONN|DISC|EVENT|NEWRUN|ENDRUN|PRINT|EXPORT|NUM|CLR|SWAP|RCL|"
				+ "START|FIN|TRIG|ELAPSED|CANCEL|DNF)\\b");
		extendedCommands = Pattern.compile("\\b(?:ON|OFF|EXIT|RESET|TIME|TOGGLE|CONN GATE|CONN EYE|CONN PAD|DISC|EVENT IND|EVENT GRP|"
				+ "EVENT PARIND|EVENT PARGRP|NEWRUN|ENDRUN|PRINT|EXPORT|NUM|CLR|SWAP|RCL|START|FIN|TRIG|ELAPSED|CANCEL|DNF)\\b");
		
		complexCommands = Pattern.compile("\\b(?:TIME|TOGGLE|CONN|DISC|EVENT|NUM|CLR|TRIG)\\b");
	}

	/**
	 * Reads in from a file and Queues all the valid tasks.
	 * (Note: could return true even tho there are no Tasks in the Queue 
	 * 	if all the arguments in the file were invalid.)
	 * 
	 * @param file - The file to read from.
	 * @return True if no exceptions were thrown else false.
	 */
	public boolean addTask(File file) {
		if (file == null) {
			return false;
		}

		try {
			Scanner in = new Scanner(new FileReader(file));

			while (in.hasNextLine()) {
				try {
					addTask(in.nextLine());
				} catch (Exception e) { };
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
	 * 
	 * @param input - The String to parse and create a new Task from.
	 * @return True if valid arguments else false.
	 * @throws IllegalArgumentException If there was an error validating the input.
	 */
	public boolean addTask(String input) throws IllegalArgumentException {
		if (input == null) {
			return false;
		}

		ArrayList<String> arguments = new ArrayList<String>();

		if (parse(input, arguments)) {
			tasks.add(new Task(arguments));
			return true;
		}

		return false;
	}

	/**
	 * USE FOR TESTING PURPOSES ONLY!
	 * 
	 * Returns the Queue of Tasks.
	 * 
	 * @return The Task Queue.
	 */
	public Queue<Task> getTaskList() {
		return tasks;
	}

	/**
	 * Peeks at the next Task to be executed.  Does not remove
	 * it from the Queue.
	 * 
	 * @return The next Task.
	 */
	public Task peekNextTask() {
		return tasks.peek();
	}

	/**
	 * Polls the next Task from the Queue to be executed.  Removes
	 * it from the Queue.
	 * 
	 * @return The next Task.
	 */
	public Task pollNextTask() {
		return tasks.poll();
	}

	/**
	 * Checks the next Task's execution time.
	 * 
	 * @return The next Task's execution time.
	 */
	public String nextTaskTime() {
		return tasks.isEmpty() ? null : tasks.peek().getTaskTime();
	}

	/**
	 * Checks the next Task's command.
	 * 
	 * @return The next Task's command.
	 */
	public String nextTaskCommand() {
		return tasks.isEmpty() ? null : tasks.peek().getTaskCommand();
	}
	
	/**
	 * Gets the list of all the valid commands.  The extended version includes
	 * the first argument for CONN and EVENT.
	 * 
	 * @param extended - Whether to use the extended version.
	 * @return The string array of valid commands.
	 */
	public String[] getCommandList(boolean extended) {
		return extended ? extendedCommands.pattern().replaceAll("[\\\\?b:()]", "").split("\\|") : validCommands.pattern().replaceAll("[\\\\?b:()]", "").split("\\|");
	}

	/**
	 * Checks whether the Queue is empty or not.
	 * 
	 * @return True if there are no Tasks else false.
	 */
	public boolean isEmpty() {
		return tasks.isEmpty();
	}

	/**
	 * Determines whether the next Task is within a specific time frame 
	 * of when the task should be executed.  Using a delta to create a
	 * time frame of when the command is valid for executing.  This is because
	 * things don't happen instantaneously so we account for it with delta.
	 * 
	 * @param time - The current system time.
	 * @return True if within time frame else false.
	 */
	public boolean nextTaskReady(long time) {
		if (nextTaskCommand().equals("TIME")) {
			return true;
		}
		long delta = 1000;
		long taskTime = SystemTime.getTimeInMillis(nextTaskTime());
		return taskTime <= time && taskTime + delta >= time;
	}
	
	/**
	 * Removes all remaning Tasks from the Queue.
	 */
	public void clearTasks() {
		tasks.clear();
	}

	/**
	 * Private helper method to parse the String wanting to become a Task.  First it checks
	 * to make sure there is a valid command within the String, then it splits the String
	 * up into parts and checks whether it's a complex command or not, if it is it checks
	 * all the valid arguments for the command, if it's still valid, it adds the split string
	 * to arguments and double checks before it returns.
	 * 
	 * @param input - The string to parse.
	 * @param arguments - The ArrayList to add the parsed arguments into.
	 * @return True if it was a valid String else false.
	 * @throws IllegalArgumentException If something went wrong, it shows the error.
	 */
	private boolean parse(String input, ArrayList<String> arguments) throws IllegalArgumentException {
		String command = input;
		if (!validCommands.matcher(command).find()) {
			throw new IllegalArgumentException("For " + command + " the format is invalid or the command is not supported.");
		}

		String[] split = input.split("[:. \\t]");
		if (complexCommands.matcher(split[4]).find()) {
			validateComplex(command, split);
		}
		
		for (String s : split) {
			arguments.add(s);
		}

		return arguments.size() > 4 && validCommands.matcher(arguments.get(4)).find();
	}

	/**
	 * Private helper method to check validation for the commands that contain 1 or more 
	 * arguments to them.  Depending on the command it checks for the correct length, checks
	 * for numbers, types, ect.
	 * 
	 * @param input - The original entry of the command.
	 * @param args - The split string of the entered command.
	 * @throws IllegalArgumentException If the command is invalid.
	 */
	private void validateComplex(String input, String[] args) throws IllegalArgumentException {
		if (args.length > 4 && complexCommands.matcher(args[4]).find() && isDigit(args[0], args[1], args[2])) {
			switch(args[4]) {
			
			case "TIME":
				// Invalid if the length is not 8 and if the 3 arguments are not numbers.
				if (args.length != 9) {
					throw new IllegalArgumentException("For " + input + " the format is incorrect.");
				} else if (!isDigit(args[5], args[6], args[7], args[8])) {
					throw new IllegalArgumentException("For " + input + " one or more of the arguments are not valid numbers.");
				}
				
				return;
				
			case "CONN":
				// Invalid if the length is not 7, argument 1 is not a valid sensor type, argument 2 is not a number and if argument 2 is
				// not a valid channel.
				if (args.length != 7) {
					throw new IllegalArgumentException("For " + input + " the format is incorrect.");
				} else if (!Sensor.isValidSensorType(args[5])) {
					throw new IllegalArgumentException("For " + input + " argument " + args[5] + " is not a valid Sensor Type.");
				} else if (!isDigit(args[6])) {
					throw new IllegalArgumentException("For " + input + " argument " + args[6] + " is not a valid number.");
				} else if (!withinBounds(1, 8, Integer.parseInt(args[6]))) {
					throw new IllegalArgumentException("For " + input + " argument " + args[6] + " is not a valid channel.");
				}
				
				return;
				
			case "EVENT":
				// Invalid if the length is not 6 and argument 1 is not a valid event type.
				if (args.length != 6) {
					throw new IllegalArgumentException("For " + input + " the format is incorrect.");
				} else if (!AbstractEvent.isValidEventType(args[5])) {
					throw new IllegalArgumentException("For " + input + " argument " + args[5] + " is not a valid Event Type.");
				}
				
				return;
				
			case "NUM": case "CLR":
				// Invalid if the length is not 6 and argument 1 is not a number.
				if (args.length != 6) {
					throw new IllegalArgumentException("For " + input + " the format is incorrect.");
				} else if (!isDigit(args[5])) {
					throw new IllegalArgumentException("For " + input + " argument " + args[5] + " is not a valid number.");
				}
				
				return;
				
			case "TRIG": case "DISC": case "TOGGLE":
				// Invalid if the length is not 6, argument 1 is not a number and argument 1 is not a valid channel.
				if (args.length != 6) {
					throw new IllegalArgumentException("For " + input + " the format is incorrect.");
				} else if (!isDigit(args[5])) {
					throw new IllegalArgumentException("For " + input + " argument " + args[5] + " is not a valid number.");
				} else if (!withinBounds(1, 8, Integer.parseInt(args[5]))) {
					throw new IllegalArgumentException("For " + input + " argument " + args[5] + " is not a valid channel.");
				}
				
				return;
				
			default:
				throw new IllegalArgumentException("TaskList validation error.  Should never happen!");
				
			}
		}
		
		throw new IllegalArgumentException("For " + input + " the format is incorrect.");
	}

	/**
	 * Private helper method to determine if any number of strings is a valid number or not.
	 * 
	 * @param args - The strings to check.
	 * @return True if all the given strings were valid numbers else false.
	 */
	private boolean isDigit(String ...args) {
		for (int i = 0; i < args.length; ++i) {
			for (int j = 0; j < args[i].length(); ++j) {
				if (!Character.isDigit(args[i].charAt(j))) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Enter a low and high for the bounds to check (inclusive).  Then checks if the
	 * given number is within the given bounds.
	 * 
	 * @param low - The low end of the bounds.
	 * @param high - The high end of the bounds.
	 * @param check - The number to check the bounds of.
	 * @return True if within given bounds else false.
	 */
	private boolean withinBounds(int low, int high, int check) {
		return check >= low && check <= high;
	}
	
}
