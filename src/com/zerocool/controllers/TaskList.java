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
				argumentOne = arguments.get(5) + ":" + arguments.get(6) + ":" + arguments.get(7) + "." + (arguments.size() >= 9 ? arguments.get(8) + "00" : "000");
			} else {
				argumentOne = arguments.size() >= 6 ? arguments.get(5) : "";
			}

			argumentTwo = arguments.size() >= 7 ? arguments.get(6) : "";
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

	}

	// The Queue of Tasks.
	private Queue<Task> tasks;

	// Holds all the valid commands to search through when parsing.
	private Pattern validCommands;
	private Pattern simpleCommands;
	private Pattern complexCommands;
	
	private String errorMessage;

	/**
	 * Default constructor that initializes the Queue and valid commands.
	 */
	public TaskList() {
		tasks = new LinkedList<Task>();
		validCommands = Pattern.compile("\\b(?:ON|OFF|EXIT|RESET|TIME|TOGGLE|CONN|DISC|EVENT|NEWRUN|ENDRUN|PRINT|EXPORT|NUM|CLR|SWAP|RCL|"
				+ "START|FIN|TRIG|ELAPSED|CANCEL|DNF)\\b");

		simpleCommands = Pattern.compile("\\b(?:ON|OFF|EXIT|RESET|NEWRUN|ENDRUN|SWAP|RCL|START|FIN|ELAPSED|CANCEL|DNF)\\b");
		complexCommands = Pattern.compile("\\b(?:TIME|TOGGLE|CONN|DISC|EVENT|PRINT|EXPORT|NUM|CLR|TRIG)\\b");
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
	 * 
	 * @param input - The String to parse and create a new Task from.
	 * @return True if valid arguments else false.
	 */
	public boolean addTask(String input) {
		System.out.println("input = " + input);
		if (input == null) {
			return false;
		}

		ArrayList<String> arguments = new ArrayList<String>();
		errorMessage = "";

		if (parse(input, arguments)) {
			tasks.add(new Task(arguments));
			System.out.println("Parsed: " + arguments + "\n");
			System.out.flush();
			return true;
		}

		return false;
	}

	/**
	 * USE FOR TESTING PURPOSES ONLY!
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
	 * Returns the current error message.
	 * 
	 * @return The current error message.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	/**
	 * Gets the list of all of the possible valid commands.
	 * 
	 * @return A String array of all the valid commands.
	 */
	public String[] getCommandList() {
		return validCommands.pattern().replaceAll("[\\\\?b:()]", "").split("\\|");
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
	 */
	private boolean parse(String input, ArrayList<String> arguments) {
		String command = input;
		if (!validCommands.matcher(command).find()) {
			System.out.println("returning false");
			return error("For " + command + " the format is invalid or the command is not supported.");
		}

		String[] split = input.split("[:. \\t]");
		if (complexCommands.matcher(split[4]).find() && !validateComplex(command, split)) {
			return false;
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
	 * @return True if it's a valid command else false.
	 */
	private boolean validateComplex(String input, String[] args) {
		if (args.length > 4 && complexCommands.matcher(args[4]).find() && isDigit(args[0], args[1], args[2])) {
			switch(args[4]) {
			
			case "TIME":
				// Invalid if the length is not 8 and if the 3 arguments are not numbers.
				if (args.length != 9) {
					return error("For " + input + " the format is incorrect.");
				} else if (!isDigit(args[5], args[6], args[7], args[8])) {
					return error("For " + input + " one or more of the arguments are not valid numbers.");
				}
				
				return true;
				
			case "CONN":
				// Invalid if the length is not 7, argument 1 is not a valid sensor type, argument 2 is not a number and if argument 2 is
				// not a valid channel.
				if (args.length != 7) {
					return error("For " + input + " the format is incorrect.");
				} else if (!Sensor.isValidSensorType(args[5])) {
					return error("For " + input + " argument " + args[5] + " is not a valid Sensor Type.");
				} else if (!isDigit(args[6])) {
					return error("For " + input + " argument " + args[6] + " is not a valid number.");
				} else if (!withinBounds(1, 8, Integer.parseInt(args[6]))) {
					return error("For " + input + " argument " + args[6] + " is not a valid channel.");
				}
				
				return true;
				
			case "EVENT":
				// Invalid if the length is not 6 and argument 1 is not a valid event type.
				if (args.length != 6) {
					return error("For " + input + " the format is incorrect.");
				} else if (!AbstractEvent.isValidEventType(args[5])) {
					return error("For " + input + " argument " + args[5] + " is not a valid Event Type.");
				}
				
				return true;
				
			case "PRINT": case "EXPORT": case "NUM": case "CLR":
				// Invalid if the length is not 6 and argument 1 is not a number.
				if (args.length != 6) {
					return error("For " + input + " the format is incorrect.");
				} else if (!isDigit(args[5])) {
					return error("For " + input + " argument " + args[5] + " is not a valid number.");
				}
				
				return true;
				
			case "TRIG": case "DISC": case "TOGGLE":
				// Invalid if the length is not 6, argument 1 is not a number and argument 1 is not a valid channel.
				if (args.length != 6) {
					return error("For " + input + " the format is incorrect.");
				} else if (!isDigit(args[5])) {
					return error("For " + input + " argument " + args[5] + " is not a valid number.");
				} else if (!withinBounds(1, 8, Integer.parseInt(args[5]))) {
					return error("For " + input + " argument " + args[5] + " is not a valid channel.");
				}
				
				return true;
				
			default: 
				errorMessage = "Unknown Error...";
				System.err.println("TaskList validation error.  Should never happen!");
				return false;
			}
		}
		
		return error("For " + input + " the format is incorrect.");
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
	
	/**
	 * Private helper method to return false and set the error message.
	 * 
	 * @param error - The error message to be seen.
	 * @return Always false.
	 */
	private boolean error(String error) {
		errorMessage = error;
		return false;
	}
	
}
