package com.zerocool.systemcontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import com.zerocool.systemcontroller.SystemTime.SystemTime;
import com.zerocool.systemcontroller.channel.Channel;
import com.zerocool.systemcontroller.event.Group;
import com.zerocool.systemcontroller.event.Individual;
import com.zerocool.systemcontroller.event.ParGroup;
import com.zerocool.systemcontroller.event.ParIndividual;
import com.zerocool.systemcontroller.eventlog.EventLog;
import com.zerocool.systemcontroller.timer.Timer;

public class SystemController {

	public ArrayList<Channel> channels;

	public Queue<String[]> commandList;

	public SystemTime systemTime;
	public Timer currentTimer;
	public EventLog eventLog;

	public long id;

	public boolean isPrinterOn;

	public SystemController() {
		// create a Queue collection to store each line in a FIFO
		// mentality using .add() and .remove()
		commandList = new LinkedList<String[]>();
		
		systemTime = new SystemTime();
		systemTime.start();
	}

	public SystemController(long id) {
		this();
		this.id = id;
	}

	public SystemController(Timer currentTimer, long id) {
		this(id);
		this.currentTimer = currentTimer;
	}

	public SystemController(Timer currentTimer, EventLog eventLog, long id) {
		this(currentTimer, id);
		this.eventLog = eventLog;
	}

	public SystemController(Timer currentTimer, EventLog eventLog, ArrayList<Channel> channels, long id) {
		this(currentTimer, eventLog, id);
		this.channels = channels;
	}

	/**
	 * Read in timestamp and commands from a file.
	 * Each line is parsed into a String[] using String.split(regex) and then that array is pushed into a Queue.
	 * The entire file will be read in and stored in the queue (FIFO mentality) before actual execution of each command takes place
	 * @param file - The file to read from.
	 */
	public void readFile(File file) {
		try {
			//read in file from given path 
			Scanner inFile = new Scanner(new FileReader(file));
			String[] parsedLine = null;

			// while there is another line to read...read it
			while (inFile.hasNextLine()) {
				String line = inFile.nextLine();

				//call parsing method
				parsedLine = parse(line, "[:. ]+ +\\t");

				// add line to the queue
				commandList.add(parsedLine);
			}
		} catch (Exception e) {
			System.out.println("ERROR!!!\n" + e.getMessage());
			e.printStackTrace();
		}


		//Uncomment to see the results of the input
		while(!commandList.isEmpty()) {
			for(String str: commandList.remove()) {
				System.out.println(str);
			}
		}

	}

	// regex to split the line by colon, period, or space!
	public String[] parse(String line, String regex) {
		return line.split(regex);
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public void readInput() {
		Scanner stdIn = new Scanner(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String [] parsedLine = null;
		boolean exit = false;
		do {
			try {
				System.out.print("mainframe$ ");
				/*
				 * Now in between parsing the command entered, and executing/storing it for now in the queue
				 * we need to append a timestamp of when the use typed in said command in the format <hour>:<min>:<second>.<millisecond>
				 * */

				String input = br.readLine();
				if(input.equals("EXIT")){
					exit = true;
				}

				//call parsing method for line
				parsedLine = parse(systemTime.toString(), "[:. ]+ \\n+\\t");


				// add line to the queue
				commandList.add(parsedLine);


			} catch (IOException ioe) {
				System.out.println("IO error trying to read your name!");
				System.exit(1);
			}
			//keep reading in lines from the terminal until exit has been entered
		}while(!exit);



		//Uncomment to see the results of the input
		while(!commandList.isEmpty()){
			for(String str: commandList.remove()){
				System.out.println("LNIE: " + str);
			}
		}
	}


	/**
	 * Excute a command.
	 * @param time - The current time ?
	 * @param cmd - The command to excute.
	 * @param args - Types of events to run.
	 */
	public void executeCommand(Date time, String cmd, ArrayList<String> args) {

		switch (cmd) {
		case "ON":
			/*
			 * --Turn system on--
			 * create new Timer 
			 * create new EventLog 
			 * create new ArrayList<Channel>
			 * set isPrinterOn = false (default state)
			 * set ID = 0 (default state)
			 * */
			break;
		case "OFF":
			/*
			 *--Turn system off (stay in simulator)--
			 *set currentTimer = null
			 *set all channels within ArrayList<Channel> and sensors associated with such to inactive states
			 *set isPrinterOn = false
			 *(I think the ID is kept...not sure)
			 * */
			break;
		case "EXIT":
			/*
			 * --Turn system off (kill everything)--
			 * */
			break;
		case "RESET":
			// stuff
			break;
		case "TIME":
			/*
			 * --Set the current time--
			 * */
			
			systemTime.setOffset(Integer.parseInt(args.get(0)) * 3600000 + Integer.parseInt(args.get(1)) * 60000 + Integer.parseInt(args.get(2)) * 1000);
			break;
		case "TOG":
			// stuff
			break;
		case "CONN":
			// stuff
			break;
		case "DISC":
			// stuff
			break;
		case "EVENT":
			/* IND | PARIND | GRP | PARGRP
			 *
			 *--I guess this just creates a new Event? lets go with that --
			 * */
			String eventType;
			if (args.get(0).equals("IND")) {
				currentTimer.setEvent(new Individual());
			}else if (args.get(0).equals("PARIND")) {
				currentTimer.setEvent(new ParIndividual());
			}else if (args.get(0).equals("GRP")) {
				currentTimer.setEvent(new Group());
			}else if (args.get(0).equals("PARGRP")) {
				currentTimer.setEvent(new ParGroup());   
			}
			break;
		case "NEWRUN":
			// stuff
			break;
		case "ENDRUN":
			// stuff
			break;
		case "PRINT":
			// stuff
			break;
		case "EXPORT":
			// stuff
			break;
		case "NUM":
			// stuff
			break;
		case "CLR":
			// stuff
			break;
		case "SWAP":
			// stuff
			break;
		case "RCL":
			// stuff
			break;
		case "START":
			// stuff
			break;
		case "FINISH":
			// stuff
			break;
		case "TRIG":
			// stuff
			break;
		}
	}

	// FOR THIS IMPLEMENTATION the channel supplied as a parameter's sensor's
	// state = true
	public void triggerSensor(int id, boolean sensorState, Channel chosenChannel) {
		chosenChannel.setSensorState(sensorState);
	}

	/**
	 * Set the Timer of the system.
	 * @param timer - The Timer to set the system.
	 */
	public void setTimer(Timer timer) {
		this.currentTimer = timer;
	}

	/**
	 * Set the EventLog of the system.
	 * @param eventLog - The EventLog to set to the system.
	 */
	public void setEventLog(EventLog eventLog) {
		this.eventLog = eventLog;
	}

	/**
	 * Set the Channels of the system.
	 * @param channels - The ArrayList of Channels to set to the system.
	 */
	public void setChannels(ArrayList<Channel> channels) {
		this.channels = channels;
	}

	/**
	 * Get's the system's current Timer.
	 * @return The current Timer.
	 */
	public Timer getTimer() {
		return this.currentTimer;
	}

	/**
	 * Get's the system's current EventLog.
	 * @return The current EVentLog.
	 */
	public EventLog getEventLog() {
		return this.eventLog;
	}

	/**
	 * Get's the system's current ArrayList of Channels.
	 * @return The current ArrayList of Channels.
	 */
	public ArrayList<Channel> getChannels() {
		return this.channels;
	}

	/**
	 * Get's the system's current ID.
	 * @return The current ID.
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Set's the Printer on or off.
	 * @param isPrinterOn - True to turn on the printer else false.
	 */
	public void setIsPrinterOn(boolean isPrinterOn) {
		this.isPrinterOn = isPrinterOn;
	}

	/**
	 * Checks whether the Printer is on or off.
	 * @return True if the printer is on else off.
	 */
	public boolean getIsPrinterOn() {
		return this.isPrinterOn;
	}

}
