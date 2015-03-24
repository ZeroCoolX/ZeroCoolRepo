package com.zerocool.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.zerocool.controllers.TaskList.Task;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Channel;
import com.zerocool.entities.Participant;
import com.zerocool.services.EventLog;
import com.zerocool.services.SystemTime;

public class SystemController {

	private ArrayList<Channel> channels;

	private TaskList taskList;
	private SystemTime systemTime;
	private Timer currentTimer;
	private EventLog eventLog;
	private AutoDetect detector;

	private int id;

	private boolean isPrinterOn;

	public SystemController() {
		channels = new ArrayList<Channel>();

		taskList = new TaskList();

		systemTime = new SystemTime();

		currentTimer = new Timer(systemTime, EventType.IND, EventType.IND.toString());
		eventLog = new EventLog();
		
		detector = new AutoDetect();

		id = 0;

		systemTime.start();
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
	 * Takes in a file and sends it to the TaskList to add all the commands from the file
	 * to the Queue.  Then it goes and executes all of the commands in the queue leaving
	 * out any invalid ones that may have been in the file.
	 * @param file - The file to read the commands from.
	 */
	public void readFile(File file) {
		taskList.addTask(file);
		while (taskList != null && !taskList.isEmpty()) {
			if (taskList.nextTaskCommand().equals("TIME") || taskList.nextTaskTime().equals(systemTime.toString())) {
				Task t = taskList.pollNextTask();
				try {
					System.out.println("executing: " + t.getTaskCommand());
					executeCommand(t.getTaskCommand(), t.getTaskArgumentOne(), t.getTaskArgumentTwo());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * USE THIS FOR TESTING PURPOSES ONLY!
	 * 
	 * This is a testing method used to just test the TaskList reading the commands
	 * from a file without executing them all.
	 * @param file - The file to read the commands from.
	 */
	public void testReadFile(File file) {
		taskList.addTask(file);
	}


	/**
	 * When this method is called, you can enter commands from the console for the
	 * SystemController to execute.  It scans from the console and executes the command
	 * only if it's a valid one.  It has some error handling meaning you can enter an
	 * invalid command and continue to enter commands.  So make some typos!
	 */
	public void readInput() {
		try {
			Scanner in = new Scanner(System.in);
			String input;
			String command;

			boolean exit = false;

			do {
				System.out.print("mainframe$ ");

				input = systemTime + "\t" + in.nextLine();

				command = executeCommand(input, false);
				exit = command != null ? command.equalsIgnoreCase("exit") : false;
				
				if (command == null) {
					System.err.println("\nInvalid Command!\n");
					System.err.flush();
					System.out.flush();
				}

			} while (!exit);

			in.close();
		} catch (Exception e) {
			System.err.println("ERROR: " + e.getMessage());
			e.getStackTrace();
			System.exit(1);
		}

	}

	/**
	 * USE THIS FOR TESTING PURPOSES ONLY!
	 * 
	 * This method returns the TaskList of the SystemController.  This is very dangerous and
	 * should only be used for testing the SystemController.
	 * @return - The TaskList.
	 */
	public TaskList getTaskList() {
		return taskList;
	}
	
	/**
	 * Executes a command.
	 * @param arguments - The String to parse and execute.
	 * @return - True if executed else false.
	 */
	public String executeCommand(String arguments) {
		return executeCommand(arguments, true);
	}
	
	/**
	 * This method is private because of the boolean which decides whether or not to wait for the command
	 * to execute.  This is only used internally for readInput() otherwise it should always be waiting for
	 * the time of the Task to execute.  This method adds a new Task to the TaskList (only if the string was
	 * valid) and then executes it.
	 * @param arguments - The command to execute.
	 * @param doWait - True for timed executing else false.
	 * @return - The command executed.  Null if command was invalid.
	 */
	private String executeCommand(String arguments, boolean doWait) {
		String command = null;		
		
		taskList.addTask(arguments);

		if (!taskList.isEmpty()) {
			while (doWait && !taskList.nextTaskCommand().equals("TIME") && !taskList.nextTaskTime().equals(systemTime.toString())) { }

			Task t = taskList.pollNextTask();
			command = t.getTaskCommand();
			try {
				executeCommand(t.getTaskCommand(), t.getTaskArgumentOne(), t.getTaskArgumentTwo());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return command;
	}

	/**
	 * Execute a command.
	 * 
	 * @param time
	 *            - The current time
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
	public void executeCommand(String cmd, String ...args) throws Exception {
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
			cmdTime(args[0]);
			break;
		case "TOG":
			// stuff
			cmdTog(Integer.parseInt(args[0]));
			break;
		case "CONN":
			// stuff
			cmdConn(args[0], Integer.parseInt(args[1]));
			break;
		case "DISC":
			// stuff
			cmdDisc(Integer.parseInt(args[0]));
			break;
		case "EVENT":
			/*
			 * IND | PARIND | GRP | PARGRP
			 * 
			 * --I guess this just creates a new Event? lets go with that --
			 */
			cmdEvent(args[0]);
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
			cmdExport();
			break;
		case "NUM":
			// stuff
			cmdNum(Integer.parseInt(args[0]));
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
		case "ELAPSED":
			// stuff
			cmdElapsed();
			break;
		case "CANCEL":
			// stuff
			cmdCancel();
			break;
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
	private void cmdOn() {
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
			currentTimer = new Timer(systemTime, EventType.IND, EventType.IND.toString());
			eventLog.logEvent(currentTimer.getEventData(), systemTime);
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
	private void cmdOff() {
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
	private void cmdEvent(String event) {
		if (event.equals("IND")) {
			currentTimer.createEvent(EventType.IND,
					EventType.IND.toString());
		} else if (event.equals("PARIND")) {
			currentTimer.createEvent(EventType.PARIND,
					EventType.PARIND.toString());
		} else if (event.equals("GRP")) {
			currentTimer.createEvent(EventType.GRP,
					EventType.GRP.toString());
		} else if (event.equals("PARGRP")) {
			currentTimer.createEvent(EventType.PARGRP,
					EventType.PARGRP.toString());
		} else {
			throw new IllegalArgumentException("Not a valid Event type.");
		}
		eventLog.logEvent(currentTimer.getEventData(), systemTime);
	}

	/**
	 * Instantiates all the variables to initial states
	 * **/
	private void cmdReset() {
		eventLog = new EventLog();
		currentTimer = new Timer(systemTime, EventType.IND, EventType.IND
				+ "", new ArrayList<Participant>());
		eventLog.logEvent(currentTimer.getEventData(), systemTime);

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
	private void cmdTime(String time) {
		// set the current time
		systemTime.setTime(time);
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
	private void cmdTog(int channel) {
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
	private void cmdConn(String sensorType, int channel) {
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
	private void cmdDisc(int channel) {
		Channel disc = findChannel(channel);
		if (disc != null) {
			disc.setSensorState(false);
			disc.disconnectSensor();
		}
	}

	/**
	 * Calls the EventLog's print(may not be named this after Adam changes
	 * stuff) method to output stats to the console
	 * @throws IOException 
	 * **/
	private void cmdPrint() throws IOException {
		FileReader fileReader = new FileReader(eventLog.getEventFile());
		BufferedReader reader = new BufferedReader(fileReader);
		System.out.println("\tEVENTLOG DATA:\n\n\n");

		while (reader.ready()) {
			System.out.println("\t"+reader.readLine()+"\n\t"+reader.readLine()+"\n");
		}

		System.out.println("\n\n");

		fileReader = new FileReader(eventLog.getParticipantFile());
		reader = new BufferedReader(fileReader);
		System.out.println("\tPARTICIPANT DATA:\n\n\n");

		while (reader.ready()) {
			System.out.println("\t"+reader.readLine()+"\n\t"+reader.readLine()+"\n");
		}

		reader.close();
	}
	
	/**
	 * Copies the data from the event log and writes the data to an external device (USB) if there is one connected
	 * @throws FileNotFoundException 
	 * **/
	private void cmdExport() throws FileNotFoundException{
	   /*
		InputStream inStream = null;
		OutputStream outStream = null;
	 
		//check to see that there actually is a usb drive ready for export
		if(!detector.usbDrives.isEmpty()){
	    	try{
	 
	    	    File exportFile =new File(""+detector.usbDrives.peek()+"/RUN.txt");
	    	    File eventLogFile = eventLog.getEventFile().getAbsoluteFile();
	 
	    	    inStream = new FileInputStream(eventLogFile);
	    	    outStream = new FileOutputStream(exportFile);
	 
	    	    byte[] buffer = new byte[1024];
	 
	    	    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = inStream.read(buffer)) > 0){
	 
	    	    	outStream.write(buffer, 0, length);
	 
	    	    }
	 
	    	    inStream.close();
	    	    outStream.close();
	 
	    	    //delete the original file
	    	    eventLogFile.delete();
	 
	    	    System.out.println("File is copied successful!");
	 
	    	}catch(IOException e){
	    	    e.printStackTrace();
	    	}
		}else{
			//there isn't a usb drive to export to...throw error and gtfo.
			System.out.println("NO USB DRIVE DETECTED");
			throw new FileNotFoundException();
		}
		*/
		if(!detector.usbDrives.isEmpty()){
	
		try {
			 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("event");
			doc.appendChild(rootElement);
	 
			// staff elements
			Element run = doc.createElement("run");
			rootElement.appendChild(run);
	 
			// set attribute to staff element
			/*Attr attr = doc.createAttribute("id");
			attr.setValue("1");
			staff.setAttributeNode(attr);*/
	 
			// shorten way
			// staff.setAttribute("id", "1");
	 
			// firstname elements
			Element firstname = doc.createElement("timestamp");
			firstname.appendChild(doc.createTextNode(""+currentTimer.getEventTime()));
			run.appendChild(firstname);
	 
			// lastname elements
			Element lastname = doc.createElement("event");
			lastname.appendChild(doc.createTextNode(""+currentTimer.getCurrentEvent().getEventName()));
			run.appendChild(lastname);
	 
			// staff elements
			Element eventData = doc.createElement("event_data");
			rootElement.appendChild(eventData);
			
			
			
			// nickname elements
			Element nickname = doc.createElement("event_ID");
			nickname.appendChild(doc.createTextNode(""+currentTimer.getCurrentEvent().getEventId()));
			eventData.appendChild(nickname);
	 
			// salary elements
			Element salary = doc.createElement("event_type");
			salary.appendChild(doc.createTextNode(""+currentTimer.getCurrentEvent().getType()));
			eventData.appendChild(salary);
			
			// salary elements
			Element eventTime = doc.createElement("event_time");
			eventTime.appendChild(doc.createTextNode(""+currentTimer.getCurrentEvent().getEventTime()));
			eventData.appendChild(salary);
	 
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(""+detector.usbDrives.peek()+"/"+currentTimer.getCurrentEvent().getEventName()+".xml"));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	 
			System.out.println("File saved!");
	 
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }	
		}else{
			//there isn't a usb drive to export to...throw error and gtfo.
			System.out.println("NO USB DRIVE DETECTED");
			throw new FileNotFoundException();
		}
		return;
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
	private void cmdNum(int participantId) {
		currentTimer.addParticipantToStart(participantId);
	}

	/**
	 * start the participant within event
	 * **/
	private void cmdStart() {
		currentTimer.startNextParticipant();
	}
	
	
	/**
	 * stop the current participant from competing but keep them as the next queued to go
	 * **/
	private void cmdCancel() {
		currentTimer.cancelStart();
	}
	
	/**
	 * get the current time
	 * **/
	private void cmdElapsed() {
		System.out.println("\nElapsed Time: " + currentTimer.getEventParticipantElapsedData() + "\n");
	}

	/**
	 * End the participant within event
	 * **/
	private void cmdFinish() {
		currentTimer.finishAllParticipants(false);
		if(currentTimer.getCurrentEvent().getCompetingParticipants().isEmpty()){
			eventLog.logParticipants(currentTimer.getEventParticipantData(), systemTime);
		}
	}

	/**
	 * End the participant within event..but...not as cool as the REGULAR finish.
	 * **/
	private void cmdDnf() {
		//System.out.println("Oh my gosh I'm tired...I'll do this later. lol");
		currentTimer.finishAllParticipants(true);
		if (currentTimer.getCurrentEvent().getCompetingParticipants().isEmpty()) {
			eventLog.logParticipants(currentTimer.getEventParticipantData(), systemTime);
		}
	}

	/**
	 * Exit the entire system. Go through all global variables calling their
	 * .exit() function and/or set them to null
	 * **/
	private void cmdExit() {
		// when the command EXIT is entered/read then the time needs to
		// completely die
		systemTime.exit();
		systemTime = null;
		isPrinterOn = false;

		if (currentTimer != null) {
			currentTimer.exit();
			currentTimer = null;
		}

		taskList.clearTasks();
		taskList = null;

		if (channels != null) {
			for (Channel chnl : channels) {
				chnl.exit();
			}
		}

		id = -1;
		if (eventLog != null) {
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
	 * Get's the list of all the valid commands.
	 * 
	 * @return The list of valid commands.
	 */
	public String[] getCommandList() {
		return taskList.getCommandList();
	}

	/**
	 * Get's the system's current ID.
	 * 
	 * @return The current ID.
	 */
	public int getId() {
		return this.id;
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

	/**
	 * Get's the Systime's time.
	 * @return - The System's time.
	 */
	public SystemTime getSystemTime() {
		return systemTime;
	}
	
	
	
	
	private boolean exportData(){
		
		return false;
	}
	
	

}