package com.zerocool.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.zerocool.controllers.TaskList.Task;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Channel;
import com.zerocool.gui.Observer;
import com.zerocool.services.EventLog;
import com.zerocool.services.SystemTime;
import com.zerocool.systemcommands.CancelCommand;
import com.zerocool.systemcommands.ClearCommand;
import com.zerocool.systemcommands.Command;
import com.zerocool.systemcommands.ConnectCommand;
import com.zerocool.systemcommands.DisconnectCommand;
import com.zerocool.systemcommands.DnfCommand;
import com.zerocool.systemcommands.ElapsedCommand;
import com.zerocool.systemcommands.EndRunCommand;
import com.zerocool.systemcommands.EventCommand;
import com.zerocool.systemcommands.ExitCommand;
import com.zerocool.systemcommands.ExportCommand;
import com.zerocool.systemcommands.FinishCommand;
import com.zerocool.systemcommands.NewRunCommand;
import com.zerocool.systemcommands.NumCommand;
import com.zerocool.systemcommands.OffCommand;
import com.zerocool.systemcommands.OnCommand;
import com.zerocool.systemcommands.PrintCommand;
import com.zerocool.systemcommands.RecallCommand;
import com.zerocool.systemcommands.ResetCommand;
import com.zerocool.systemcommands.StartCommand;
import com.zerocool.systemcommands.SwapCommand;
import com.zerocool.systemcommands.TimeCommand;
import com.zerocool.systemcommands.ToggleCommand;
import com.zerocool.systemcommands.TriggerCommand;

/**
 * @author ZeroCool
 *
 */
public class SystemController {

	private ArrayList<Observer> observers;
	
	private Channel[] channels;

	private Thread loop;
	
	private WebServiceLink server;
	private TaskList taskList;
	private SystemTime systemTime;
	private Timer currentTimer;
	private EventLog eventLog;
	private AutoDetect detector;
	private Task lastTask;
	private Command currentCommand;
	
	private boolean isPrinterOn;
	private boolean shouldPrint;
	private boolean running;
	private boolean on;
	
	private int lastTrigger;

	public SystemController() {
		channels = populateChannels();

		server = new WebServiceLink();
		
		taskList = new TaskList();

		systemTime = new SystemTime();

		currentTimer = new Timer(systemTime, EventType.IND, EventType.IND.toString());
		eventLog = new EventLog();
		eventLog.logEvent(currentTimer.getEventData(), systemTime);

		detector = new AutoDetect();

		systemTime.start();
		observers = new ArrayList<Observer>();

		running = true;
		run();
	}

	/**
	 * Internal Thread that runs the System.  It checks the TaskList for any new commands to execute,
	 * executes the next command if there is one, then updates all the observers.  Sleeps for 1 millisecond.
	 */
	private void run() {
		loop = new Thread(new Runnable() {

			@Override
			public void run() {
				while (running) {
					try {
						Thread.sleep(50);
					} catch (Exception e) { e.printStackTrace(); };
					
					if (taskList != null && !taskList.isEmpty()) {
						if (taskList.nextTaskReady(systemTime.getTime())) {
							Task task = taskList.pollNextTask();
							lastTask = task;
							System.err.println(SystemTime.formatTime(systemTime.getTime()) + " Executing " + task + "...");
							setCurrentCommand(task.getTaskCommand());
							currentCommand.execute(task.getTaskArgumentOne(), task.getTaskArgumentTwo());
						}
					}
					
					updateObservers();
				}
			}

		});

		loop.start();
	}

	/**
	 * Takes in a file and sends it to the TaskList to add all the commands from the file
	 * to the Queue.  Then it goes and executes all of the commands in the queue leaving
	 * out any invalid ones that may have been in the file.
	 * @param file - The file to read the commands from.
	 */
	public void readFile(File file) {
		taskList.addTask(file);
	}

	/**
	 * This method returns the TaskList of the SystemController. Used by commands.
	 * @return The TaskList.
	 */
	public TaskList getTaskList() {
		return taskList;
	}
	
	/**
	 * @param taskList - can be null because of commands like exit.
	 */
	public void setTaskList(TaskList taskList) {
		this.taskList = taskList;
	}
	
	/**
	 * This method returns the current Timer.  Used by commands.
	 * 
	 * @return The current Timer.
	 */
	public Timer getTimer() {
		return currentTimer;
	}
	
	/**
	 * Can be set to null with commands such as exit and reset.
	 * 
	 * @param timer - the timer to set current timer to
	 */
	public void setTimer(Timer timer) {
		currentTimer = timer;
	}
	
	/**
	 * 
	 * This method returns the current EventLog.
	 * 
	 * @return The current EventLog.
	 */
	public EventLog getEventLog() {
		return eventLog;
	}
	
	
	/**
	 * Sets the event log.  Only used by commands.
	 */
	public void setEventLog(EventLog log) {
		eventLog = log;
	}
	
	/**
	 * This method returns the current array of Channels.
	 * Used in command implementations.
	 * 
	 * @return The current ArrayList of Channels.
	 */
	public Channel[] getChannels() {
		return channels;
	}
	
	/**
	 * Used by the commands
	 */
	public void setChannels(Channel[] channels) {
		this.channels = channels;
	}
	
	/**
	 * Used in commands
	 * 
	 * Gets the current USB detector.
	 * 
	 * @return The current USB detector.
	 */
	public AutoDetect getAutoDetect() {
		return detector;
	}
	
	public void setAutoDetect(AutoDetect detector) {
		this.detector = detector;
	}
	
	/**
	 * When this method is called, you can enter commands from the console for the
	 * SystemController to execute.  It scans from the console and executes the command
	 * only if it's a valid one.  It has some error handling meaning you can enter an
	 * invalid command and continue to enter commands.  So make some typos!
	 */
	public void readInput() {
		try {
			Scanner input = new Scanner(System.in);
			String command;

			boolean exit = false;

			do {
				System.out.print("mainframe$ ");

				command = input.nextLine();

				if (taskList.addTask(systemTime + " " + command)) {
					Task task = taskList.pollNextTask();
					exit = task.getTaskCommand().equals("EXIT");
					lastTask = task;
					System.err.println(SystemTime.formatTime(systemTime.getTime()) + " Executing " + task + "...");
					setCurrentCommand(task.getTaskCommand());
					currentCommand.execute(task.getTaskArgumentOne(), task.getTaskArgumentTwo());
				}
				
			} while (!exit);

			input.close();
		} catch (Exception e) {
			System.err.println("ERROR: " + e.getMessage());
			e.getStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Add a command for the system to execute.
	 * 
	 * @param command - The command to execute.
	 * @return True if it was a valid command else false.
	 * @throws IllegalArgumentException If there was an error validating the command.
	 */
	public boolean addTask(String command) throws IllegalArgumentException {
		return taskList.addTask(command);
	}

	
	/**
	 * Sets the current command to the command entered by the user.
	 * @param command - a String command that is on the approved list of commands
	 */
	private void setCurrentCommand(String command) {
		switch (command) {
		case "ON":
			/*
			 * --Turn system on-- create new Timer create new EventLog
			 * create new ArrayList<Channel> set isPrinterOn = false
			 * (default state) set ID = 0 (default state)
			 */
			currentCommand = new OnCommand(this);
			break;
		case "OFF":
			 //Turn system off (stay in simulator)--
			currentCommand = new OffCommand(this);
			break;
		case "EXIT":
			 //Turn system off (kill everything)--
			currentCommand = new ExitCommand(this);
			break;
		case "RESET":
			currentCommand = new ResetCommand(this);
			break;
		case "TIME":
			 //Set the current time--
			currentCommand = new TimeCommand(this);
			break;
		case "TOGGLE":
			// toggles on or off a sensor
			currentCommand = new ToggleCommand(this);
			break;
		case "CONN":
			// connects a chanel to a sensor
			currentCommand = new ConnectCommand(this);
			break;
		case "DISC":
			// disconnects a chanel from a sensor
			currentCommand = new DisconnectCommand(this);
			break;
		case "EVENT":
			// creates an event  
			currentCommand = new EventCommand(this);
			break;
		case "NEWRUN":
			// clears competing, finish, start queues and readies for a new event
			currentCommand = new NewRunCommand(this);
			break;
		case "ENDRUN":
			// finishes all non finished particiapnts with DNF 
			currentCommand = new EndRunCommand(this);
			break;
		case "PRINT":
			// prints event log data to the printer screen
			currentCommand = new PrintCommand(this);
			break;
		case "EXPORT":
			// if an external, WRITABLE, (read only will not work...duh...) storage device is connected, eventLog data is copied to a .xml file onto the device
			currentCommand = new ExportCommand(this);
			break;
		case "NUM":
			// adds a participant with an id as the args to a collection of event participants
			currentCommand = new NumCommand(this);
			break;
		case "CLR":
			//clears 
			currentCommand = new ClearCommand(this);
			break;
		case "SWAP":
			// switches placement of the 1st and the 2nd participant in the competing queue
			currentCommand = new SwapCommand(this);
			break;
		case "RCL":
			currentCommand = new RecallCommand(this);
			break;
		case "START":
			// Start a participant competing in an event
			currentCommand = new StartCommand(this);
			break;
		case "FIN":
			// Finish a participant competing in an event
			currentCommand = new FinishCommand(this);
			break;
		case "TRIG":
			// Triggers a sensor to be tripped
			currentCommand = new TriggerCommand(this);
			break;
		case "DNF":
			// the participant did not finish the event and will have DNF as a finish time
			currentCommand = new DnfCommand(this);
			break;
		case "ELAPSED":
			// get the time from system start till now
			currentCommand = new ElapsedCommand(this);
			break;
		case "CANCEL":
			// cancel the curent command
			currentCommand = new CancelCommand(this);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Creates an array of 8 Channels.
	 * 
	 * @return - The array of 8 Channels created.
	 */
	public Channel[] populateChannels() {
		Channel[] chans = new Channel[8];
		
		for (int i = 0; i < chans.length; ++i) {
			chans[i] = new Channel(this, null, i);
		}
		
		return chans;
	}

	/**
	 * Private helper method to get a channel by an id.
	 * 
	 * @param id - The id of the channel to get.
	 * @return The channel from the specified id or null if id is 
	 * not a valid channel.
	 */
	private Channel getChannel(int id) {
		if (channels == null || id < 0 || id > channels.length) {
			return null;
		}
		return channels[id];
	}
	
	/**
	 * Checks if the channel is active or not.
	 * 
	 * @param id - The id of the channel to check.
	 * @return True if the channel is active else false.  If the id was
	 * not a valid channel, it also returns false.
	 */
	public boolean isChannelActive(int id) {
		Channel chan = getChannel(id - 1);
		return chan != null ? chan.getState() : false;
	}
	
	/**
	 * Checks if the channel's sensor is active or not.
	 * 
	 * @param id - The id of the channel to check.
	 * @return True if the sensor is active else false.  If the id was
	 * not a valid channel, it also returns false.
	 */
	public boolean isSensorActive(int id) {
		Channel chan = getChannel(id - 1);
		return chan != null ? chan.getSensorState() : false;
	}
	
	/**
	 * Gets the list of all the valid commands.  The extended version includes
	 * the first argument for CONN and EVENT.
	 * 
	 * @param extended - Whether to use the extended version.
	 * @return The string array of valid commands.
	 */
	public String[] getCommandList() {
		return taskList.getCommandList();
	}
	
	/**
	 * Gets the special arguments for a given command.  The only two
	 * commands supported are CONN & EVENT since the others only require
	 * numbers.
	 * 
	 * @param command - The command to get the arguments for.
	 * @return The string array of arguments or null if not a valid command.
	 */
	public String[] getCommadArgs(String command) {
		return taskList.getCommadArgs(command);
	}
	
	/**
	 * Gets the last task executed by the system.
	 * 
	 * @return The last Task executed.
	 */
	public Task getLastTask() {
		return lastTask;
	}

	public String getStartingQueue() {
		return currentTimer.getStartingQueue();
	}
	
	public String getRunningQueue() {
		return currentTimer.getRunningQueue();
	}
	
	public String getFinishedQueue() {
		return currentTimer.getFinishedQueue();
	}
	
	/**
	 * Set's the Printer on or off.
	 * 
	 * @param isPrinterOn - True to turn on the printer else false.
	 */
	public void setIsPrinterOn(boolean isPrinterOn) {
		this.isPrinterOn = isPrinterOn;
	}

	/**
	 * Checks whether the Printer is on or off.
	 * 
	 * @return True if the printer is on else false.
	 */
	public boolean getIsPrinterOn() {
		return isPrinterOn;
	}
	
	public boolean shouldPrint() {
		return shouldPrint;
	}
	
	public String getPrintData() {
		shouldPrint = false;
		return eventLog.read();
	}
	
	/**
	 * Checks whether there is a USB drive connected.
	 * 
	 * @return True if there is a USB drive connected else false.
	 */
	public boolean isDriveConnected() {
		return detector != null && detector.driveConnected();
	}
	
	public boolean isOn() {
		return on;
	}
	
	/**
	 * Add an Observer to the list of Observers to be updated by the System.
	 * 
	 * @param observer - The observer to be added.
	 */
	public void addObserver(Observer observer) {
		observers.add(observer);
	}
	
	/**
	 * Runs through the list of Observers and updates them.
	 */
	private void updateObservers() {
		for (Observer o : observers) {
			o.update();
		}
	}
	
	/**
	 * @return The integer value of the last channel triggered.
	 */
	public int getLastTrigger() {
		return this.lastTrigger;
	}
	
	/**
	 * Sets the integer value of the last channel triggered.
	 * @param lastTrigger
	 */
	public void setLastTrigger(int lastTrigger) {
		this.lastTrigger = lastTrigger;
	}

	/**
	 * Gets the Systime's time.
	 * 
	 * @return The System's time.
	 */
	public SystemTime getSystemTime() {
		return systemTime;
	}
	
	public void setOn(boolean on) {
		this.on = on;
	}
	
	/**
	 * Sets the System Time
	 * @param systemTime
	 */
	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}
	
	/**
	 * @param shouldPrint
	 */
	public void setShouldPrint(boolean shouldPrint) {
		this.shouldPrint = shouldPrint;
	}
	
	/**
	 * Sets if isRunning
	 * @param isRunning - true or false
	 */
	public void setIsRunning(boolean isRunning) {
		running = isRunning;
	}
	
	/**
	 * Interrupts the event loop of the controller.
	 */
	public void stopEventLoop() {
		loop.interrupt();
	}
	
	/**
	 * Posts the most recent run results to the web server.
	 */
	public void postResultsToServer() {
		server.postToServer(currentTimer.getEventParticipantView());
	}
	
	public void resetServer() {
		server.postToServer(null);
	}
}