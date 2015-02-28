package com.zerocool.controllers;

/*
 * AWESOME WORK SO FAR GUYS!!! ^___^
 * 
 * KEEP UP THE GREAT WORK!!!!!
 * */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Channel;
import com.zerocool.entities.Participant;
import com.zerocool.services.EventLog;
import com.zerocool.services.SystemTime;

public class SystemController {
	
	public ArrayList<Channel> channels;

	public Queue<ArrayList<String>> commandList;

	public SystemTime systemTime;
	public Timer currentTimer;
	public EventLog eventLog;

	public String validCommands;
	
	public int id;

	public boolean isPrinterOn;

	public SystemController() {
		channels = new ArrayList<Channel>();
		// create a Queue collection to store each line in a FIFO
		// mentality using .add() and .remove()
		commandList = new LinkedList<ArrayList<String>>();
		
		validCommands = "ON, OFF, EXIT, RESET, TIME, TOG, CONN, DISC, EVENT, NEWRUN, ENDRUN, PRINT, EXPORT, NUM, CLR, "
				+ "SWAP, RCL, START, FIN, TRIG";

		systemTime = new SystemTime();
		systemTime.start();

		currentTimer = new Timer(systemTime, EventType.IND, EventType.IND + "", new ArrayList<Participant>());
		eventLog = new EventLog();

		id = 0;
	}

	public SystemController(int id) {
		this();
		this.id = id;
	}

	public SystemController(Timer currentTimer, int id) {
		this(id);
		this.currentTimer = currentTimer;
	}

	public SystemController(Timer currentTimer, EventLog eventLog, int id) {
		this(currentTimer, id);
		this.eventLog = eventLog;
	}

	public SystemController(Timer currentTimer, EventLog eventLog,
			ArrayList<Channel> channels, int id) {
		this(currentTimer, eventLog, id);
		this.channels = channels;
	}

	/**
	 * Read in timestamp and commands from a file. Each line is parsed into a
	 * String[] using String.split(regex) and then that array is pushed into a
	 * Queue. The entire file will be read in and stored in the queue (FIFO
	 * mentality) before actual execution of each command takes place
	 * 
	 * @param file
	 *            - The file to read from.
	 */
	public void readFile(File file) {

		try {

			// read in file from given path
			Scanner inFile = new Scanner(new FileReader(file));
			String[] parsedLine = null;

			// while there is another line to read...read it
			while (inFile.hasNextLine()) {
				String line = inFile.nextLine();

				// call parsing method
				parsedLine = parse(line, "[:. \\t]");
				ArrayList<String> parsedList = new ArrayList<String>();
				for (String str : parsedLine) {
					parsedList.add(str);
				}

				// check to see if the cmd is TIME, if it is Execute that
				// immediately
				if (parsedList.get(4).equals("TIME")) {
					executeCommand(parsedList.get(4), parsedList);
				} else {
					// add line to the queue
					commandList.add(parsedList);
				}
			}

			inFile.close();

			// Uncomment to see the results of the input
			while (!commandList.isEmpty() && isValidCommand(commandList.peek().get(4))) {
					ArrayList<String> currentLine = commandList.remove();
					String[] systemTimeArr = parse(systemTime.toString(),
							"[:.]");

					while (!(currentLine.get(0).equals(systemTimeArr[0])
							&& currentLine.get(1).equals(systemTimeArr[1]) && currentLine
							.get(2).equals(systemTimeArr[2]))) {
						// KEEP CHECKING!!!!!!!!!!
						systemTimeArr = parse(systemTime.toString(), "[:.]");
					}
					System.out.println(systemTime.toString() + "\t"
							+ currentLine.get(4));
					executeCommand(currentLine.get(4), currentLine);
			}

		} catch (Exception e) {
			System.err.println("ERROR: " + e.getMessage());
			e.getStackTrace();			
			System.exit(1);
		}

	}

	/**
	 * Helper method that parses the given string by the given regex
	 * 
	 * @param line
	 *            - String that needs parsing
	 * @param regex
	 *            - Regular Expression which determines how to parse the String
	 * @return newLine - String array of parsed string
	 * **/
	private String[] parse(String line, String regex) {
		String[] newLine = line.split(regex);
		return newLine;
	}
	
	public boolean isValidCommand(String str){
		return validCommands.contains(str);
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public void readInput() {

		try {

			Scanner stdIn = new Scanner(System.in);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String[] parsedLine = null;
			boolean exit = false;
			do {
				System.out.print("mainframe$ ");
				/*
				 * Now in between parsing the command entered, and
				 * executing/storing it for now in the queue we need to
				 * append a timestamp of when the use typed in said command
				 * in the format <hour>:<min>:<second>.<millisecond>
				 */

				String input = br.readLine();
				if (input.equals("EXIT")) {
					exit = true;
				}

				// call parsing method for line
				parsedLine = parse((systemTime.toString() + "\t" + input),
						"[:. \\t]");
				ArrayList<String> parsedList = new ArrayList<String>();
				for (String str : parsedLine) {
					parsedList.add(str);
				}

				if(isValidCommand(parsedList.get(4))){
					// check to see if the cmd is TIME, if it is Execute that
					// immediately
					executeCommand(parsedList.get(4), parsedList);
				}else{
					exit = true;
				}

				// keep reading in lines from the terminal until exit has been
				// entered
			} while (!exit);

			// Uncomment to see the results of the input
			while (!commandList.isEmpty()) {
				for (String str : commandList.remove()) {
					System.out.println("LINE: " + str);
				}
			}

		} catch (Exception e) {
			System.err.println("ERROR: " + e.getMessage());
			e.getStackTrace();
			System.exit(1);
		}

	}

	/**
	 * Execute a command.
	 * 
	 * @param time
	 *            - The current time ?
	 * @param cmd
	 *            - The command to execute.
	 * @param args
	 *            - Types of events to run.
	 * 
	 *            Note for the different indexes used!
	 * 
	 *            whenever you see something like "args.get(5)" or any number in
	 *            the parens its grabbing a specific CONSTANT value from the
	 *            line in the file.
	 * 
	 *            The reason I say constant is because we have a CONSTANT file
	 *            format like below: 12:00:00.0 TIME 12:01:00 12:01:02.0 ON
	 *            12:01:12.0 CONN GATE 1 12:02:00.0 TOGGLE 1 12:02:10.0 NUM 234
	 * 
	 *            This allows us to ALWAYS know that the index 0 = hour, 1 =
	 *            minute, 2 = second, 3 = milisecond because every line is
	 *            preceeded by a timestamp.
	 * 
	 *            This also allows us to ALWAYS KNOW that the 4th index is the
	 *            command. always. everytime. no matter what.
	 * 
	 * 
	 *            This also allows us to make other assumptions like for example
	 *            the line below: 12:01:12.0 CONN GATE 1 index 0 1 2 3 4 5 6
	 * 
	 *            since we know the 4th index is the command, ALL indicies after
	 *            the 4th are the args. so for the line above which would have
	 *            its own Case statement in the actual executeCommand() method
	 *            we know that the 5th index is the type of sensor and the 6th
	 *            index is the channel id.
	 * 
	 *            Using these known assumptions about the format of the file is
	 *            the reason for specific indecies being used in each individual
	 *            case statement body.
	 * 
	 */
	public void executeCommand(String cmd, ArrayList<String> args) throws Exception {
		try {

			switch (cmd) {
			case "ON":
				/*
				 * --Turn system on-- create new Timer create new EventLog
				 * create new ArrayList<Channel> set isPrinterOn = false
				 * (default state) set ID = 0 (default state)
				 */
				cmdOn();
				break;
			case "OFF":
				/*
				 * --Turn system off (stay in simulator)--set currentTimer =
				 * nullset all channels within ArrayList<Channel> and sensors
				 * associated with such to inactive statesset isPrinterOn =
				 * false(I think the ID is kept...not sure)
				 */
				cmdOff();
				break;
			case "EXIT":
				/*
				 * --Turn system off (kill everything)--
				 */
				cmdExit();
				break;
			case "RESET":
				// stuff
				cmdReset();
				break;
			case "TIME":
				/*
				 * --Set the current time--
				 */
				cmdTime(args);
				break;
			case "TOG":
				// stuff
				cmdTog(Integer.parseInt(args.get(5)));
				break;
			case "CONN":
				// stuff
				cmdConn(args.get(5), Integer.parseInt(args.get(6)));
				break;
			case "DISC":
				// stuff
				cmdDisc(Integer.parseInt(args.get(5)));
				break;
			case "EVENT":
				/*
				 * IND | PARIND | GRP | PARGRP
				 * 
				 * --I guess this just creates a new Event? lets go with that --
				 */
				cmdEvent(args);
				break;
			case "NEWRUN":
				// stuff
				break;
			case "ENDRUN":
				// stuff
				break;
			case "PRINT":
				// stuff
				cmdPrint();
				break;
			case "EXPORT":
				// stuff
				break;
			case "NUM":
				// stuff
				cmdNum(Integer.parseInt(args.get(5)));
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
				cmdStart();
				break;
			case "FIN":
				// stuff
				cmdFinish();
				break;
			case "TRIG":
				// stuff
				break;
			case "DNF":
				// stuff
				cmdDnf();
				break;
			}

		} catch (Exception e) {
			throw e;
		}

	}

	// FOR THIS IMPLEMENTATION the channel supplied as a parameter's sensor's
	// state = true
	public void triggerSensor(int id, boolean sensorState, Channel chosenChannel) {
		chosenChannel.setSensorState(sensorState);
	}

	/**
	 * Instantiates all the variables to initial states
	 * **/
	public void cmdOn() throws Exception {
		// When the command ON is entered/read then the time needs to start.
		// As
		// of now upon instantiation of this class the systemTime is
		// started...not sure if that should happen HERE or THERE
		// What happens when OFF is entered...then ON again right after it?
		// IDK
		// we need to converse on this
		// printer set to false for default state
		if (eventLog == null) {
			eventLog = new EventLog();
		}
		if (currentTimer == null) {
			currentTimer = new Timer(systemTime, EventType.IND, EventType.IND + "", new ArrayList<Participant>());
		}
		if (channels == null) {
			channels = new ArrayList<Channel>();
		}
		// printer set to false for insurance
		isPrinterOn = false;
	}

	/**
	 * KEEP THE systemTime running set everything else to null
	 * **/
	public void cmdOff() throws Exception {
		// When the command OFF is entered/read then the time needs to stop.
		eventLog = null;
		currentTimer = null;
		channels = null;
		// printer set to false for insurance
		isPrinterOn = false;
	}

	/**
	 * Sets the currentTimer event to a new instance of the type of method given
	 * from index(5)
	 * 
	 * @param args
	 *            - ArrayList containing the line from the file looking like:
	 *            HR:MIN:SEC.MIL EVENT ARG **which will either be (IND, PARIND,
	 *            GRP, PARGRP)** args: 0 1 2 3 4 5 *
	 **/
	public void cmdEvent(ArrayList<String> args) throws Exception {
		if (args.get(5).equals("IND")) {
			currentTimer.createEvent(EventType.IND,
					EventType.IND.toString());
		} else if (args.get(5).equals("PARIND")) {
			currentTimer.createEvent(EventType.PARIND,
					EventType.PARIND.toString());
		} else if (args.get(5).equals("GRP")) {
			currentTimer.createEvent(EventType.GRP,
					EventType.GRP.toString());
		} else if (args.get(5).equals("PARGRP")) {
			currentTimer.createEvent(EventType.PARGRP,
					EventType.PARGRP.toString());
		}
	}

	/**
	 * Instantiates all the variables to initial states
	 * **/
	public void cmdReset() throws Exception {
		eventLog = new EventLog();
		currentTimer = new Timer(systemTime, EventType.IND, EventType.IND
				+ "", new ArrayList<Participant>());
		channels = new ArrayList<Channel>();
		// printer set to false for insurance
		isPrinterOn = false;
	}

	/**
	 * Sets the systemTime variable to the given hour, minute, and second
	 * denoted by the indicies (5,6,7) from the ArrayList
	 * 
	 * @param args
	 *            - ArrayList containing the line from the file looking like:
	 *            HR:MIN:SEC.MIL TIME HR:MIN:SEC args: 0 1 2 3 4 5 6 7
	 * **/
	public void cmdTime(ArrayList<String> args) throws Exception {
		// set the current time
		systemTime.setTime(Integer.parseInt(args.get(5)) * 3600000
				+ Integer.parseInt(args.get(6)) * 60000
				+ Integer.parseInt(args.get(7)) * 1000);
		systemTime.start();
	}

	/**
	 * Finds the channel within the ArrayList with id of the paramenter. If
	 * there is a channel found this channels sensor state is set to the
	 * opposite of whatever it is If there isn't a channel found then a new
	 * channel is created and added to the ArrayList
	 * 
	 * @param channel - the channel ID to either set state or create new instance of
	 * **/
	public void cmdTog(int channel) throws Exception {
		Channel toggle = findChannel(channel);
		if (toggle != null) {
			toggle.setSensorState((toggle.getSensorState() == true ? false : true));
		} else {
			Channel chnl = new Channel();
			chnl.setID(channel);
			chnl.setState(true);
			chnl.setSensorState(true);
			channels.add(chnl);
		}
	}

	/**
	 * Finds the given channel within the global ArrayList channels that matches
	 * the ID field given as the int parameter "channel" If the channel is
	 * matched and returned from the helper findChannel(Channel) method, then a
	 * new sensor of whatever type the sensorType parameter is is created and
	 * assigned to the channel. If the channel does not exist, then a new
	 * channel is created and added to the global ArrayList channels (with the
	 * given ID field) and still a sensor of type sensorType is created and
	 * added to the channel
	 * 
	 * @param sensorType
	 *            - type of sensor (EYE, GATE, PAD) to add to the given channel
	 * @param channel
	 *            - ID field for a channel to connect a sensor too
	 * **/
	public void cmdConn(String sensorType, int channel) throws Exception {
		Channel connect = findChannel(channel);
		if (connect != null) {
			connect.addSensor(sensorType);
		}else{
			Channel chnl = new Channel();
			chnl.setID(channel);
			chnl.setState(true);
			chnl.addSensor(sensorType);
			channels.add(chnl);
		}
	}

	/**
	 * Finds the given channel within the global ArrayList channels that matches
	 * the ID field given as the int parameter "channel" If the channel is
	 * found, then the sensor associated with that channel is set to a false
	 * state (off or disconnected) If the channel is not AN ERROR WILL BE THROWN
	 * IN THE FUTURE. NOT IMPLEMENTED YET
	 * 
	 * @param channel - the channel ID with which to set the sensor state
	 */
	public void cmdDisc(int channel) throws Exception {
		Channel disc = findChannel(channel);
		if (disc != null) {
			disc.setSensorState(false);
			disc.disconnectSensor();
		}
	}

	/**
	 * Calls the EventLog's print(may not be named this after Adam changes
	 * stuff) method to output stats to the console
	 * **/
	public void cmdPrint() throws Exception {
		//HAHA I think we're adding to the eventLog like 9X as much...we should have 3 entries...instead we have 27
		try {
			FileReader fileReader = new FileReader(eventLog.getFile());
			BufferedReader reader = new BufferedReader(fileReader);
			System.out.println("PRINTING EVENTLOG DATA:\n\n\n");
			while(reader.ready()){
				System.out.println("\t"+reader.readLine()+"\n\t"+reader.readLine()+"\n");
			}
			System.out.println("\n\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Finds the given Participant within the global Timer time's ArrayList of
	 * participants that matches the ID field given as the int parameter
	 * "participant If the participant is found then that participant's isNext
	 * field is set to true If the participant is NOT found then a new
	 * participant is created, added to the ArrayList or Participants wihtin
	 * currentTimer, and isNext state is set to true
	 * 
	 * @param participant - ID field of the participant
	 * **/
	public void cmdNum(int participantId) throws Exception {
		Participant par = currentTimer.findParticipant(participantId);
		if (par != null) {
			par.setIsNext(true);
		} else {
			currentTimer.addNewParticipant(participantId);
		}
	}

	/**
	 * start the participant within event
	 * **/
	public void cmdStart() throws Exception {
		currentTimer.startEvent();
	}

	/**
	 * End the participant within event
	 * **/
	public void cmdFinish() throws Exception {
		currentTimer.endEvent();
		eventLog.logEvent(currentTimer.getCurrentEvent(), systemTime);
	}

	/**
	 * End the participant within event..but...not as cool as the REGULAR finish.
	 * **/
	public void cmdDnf() throws Exception {
		//System.out.println("Oh my gosh I'm tired...I'll do this later. lol");
		currentTimer.endEvent();
		eventLog.logEvent(currentTimer.getCurrentEvent(), systemTime);
	}

	/**
	 * Exit the entire system. Go through all global variables calling their
	 * .exit() function and/or set them to null
	 * **/
	public void cmdExit() throws Exception {
		// when the command EXIT is entered/read then the time needs to
		// completely die
		systemTime.exit();
		systemTime = null;
		isPrinterOn = false;
		if (currentTimer != null) {
			currentTimer.exit();
			currentTimer = null;
		}
		if (commandList != null) {
			while (!commandList.isEmpty()) {
				commandList.remove();
			}
			commandList = null;
		}
		if (channels != null) {
			for (Channel chnl : channels) {
				chnl.exit();
			}
		}
		id = -1;
		if (eventLog != null) {
			System.out.println("exiting eventLog");
			eventLog.exit();
		}
		//cannot totally system exit for testing purposes...
		//System.exit(1);
	}

	/**
	 * Helper method that goes through all channels in channels looking for a
	 * matching ID field as the parameter id
	 * 
	 * @param id
	 *            - the ID field for the saught after Channel
	 * @return chnl - the Channel with ID field that matches parameter id
	 * **/
	private Channel findChannel(int id) {
		if (channels != null) {
			for (Channel chnl : channels) {
				if (chnl.getId() == id) {
					return chnl;
				}
			}
		}
		return null;
	}

	/**
	 * Set the Timer of the system.
	 * 
	 * @param timer
	 *            - The Timer to set the system.
	 */
	public void setTimer(Timer timer) {
		this.currentTimer = timer;
	}

	/**
	 * Set the EventLog of the system.
	 * 
	 * @param eventLog
	 *            - The EventLog to set to the system.
	 */
	public void setEventLog(EventLog eventLog) {
		this.eventLog = eventLog;
	}

	/**
	 * Set the Channels of the system.
	 * 
	 * @param channels
	 *            - The ArrayList of Channels to set to the system.
	 */
	public void setChannels(ArrayList<Channel> channels) {
		this.channels = channels;
	}

	/**
	 * Get's the system's current Timer.
	 * 
	 * @return The current Timer.
	 */
	public Timer getTimer() {
		return this.currentTimer;
	}

	/**
	 * Get's the system's current EventLog.
	 * 
	 * @return The current EventLog.
	 */
	public EventLog getEventLog() {
		return this.eventLog;
	}

	/**
	 * Get's the system's current ArrayList of Channels.
	 * 
	 * @return The current ArrayList of Channels.
	 */
	public ArrayList<Channel> getChannels() {
		return this.channels;
	}

	/**
	 * Get's the system's current ID.
	 * 
	 * @return The current ID.
	 */
	public int getId() {
		return this.id;
	}

	public Queue<ArrayList<String>> getCommandList(){
		return this.commandList;
	}

	/**
	 * Set's the Printer on or off.
	 * 
	 * @param isPrinterOn
	 *            - True to turn on the printer else false.
	 */
	public void setIsPrinterOn(boolean isPrinterOn) {
		this.isPrinterOn = isPrinterOn;
	}

	/**
	 * Checks whether the Printer is on or off.
	 * 
	 * @return True if the printer is on else off.
	 */
	public boolean getIsPrinterOn() {
		return this.isPrinterOn;
	}

	public SystemTime getSystemTime() {
		return systemTime;
	}

}
