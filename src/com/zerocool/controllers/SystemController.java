package com.zerocool.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.zerocool.controllers.TaskList.Task;
import com.zerocool.entities.AbstractEvent;
import com.zerocool.entities.AbstractEvent.EventType;
import com.zerocool.entities.Channel;
import com.zerocool.entities.Participant;
import com.zerocool.gui.Observer;
import com.zerocool.services.EventLog;
import com.zerocool.services.SystemTime;

public class SystemController {

	private ArrayList<Observer> observers;
	
	private Channel[] channels;

	private TaskList taskList;
	private SystemTime systemTime;
	private Timer currentTimer;
	private EventLog eventLog;
	private AutoDetect detector;
	private TaskList.Task lastTask;

	private int id;

	private boolean isPrinterOn;
	private boolean running;

	public SystemController() {
		channels = populateChannels();

		taskList = new TaskList();

		systemTime = new SystemTime();

		currentTimer = new Timer(systemTime, EventType.IND, EventType.IND.toString());
		eventLog = new EventLog();
		eventLog.logEvent(currentTimer.getEventData(), systemTime);

		detector = new AutoDetect();

		id = 0;

		systemTime.start();
		observers = new ArrayList<Observer>();

		running = true;
		run();
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

	public SystemController(Timer currentTimer, EventLog eventLog, Channel[] channels, int id) {
		this(currentTimer, eventLog, id);
		if (channels.length != 8) {
			throw new IllegalArgumentException("Need to only have 8 channels not " + channels.length + "!");
		}
		this.channels = channels;
	}

	private void run() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (running) {
					try {
						Thread.sleep(1);
					} catch (Exception e) { e.printStackTrace(); };

				//	updateChannels();
					updateObservers();
				}
			}

		});

		t.start();
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
	public String executeCommand(String arguments, boolean doWait) {
		String command = null;		
		taskList.addTask(arguments);
		lastTask = taskList.peekNextTask();
		if (!taskList.isEmpty()) {
			while (doWait && !taskList.nextTaskCommand().equals("TIME") && !taskList.nextTaskTime().equals(systemTime.toString())) { };

			Task t = taskList.pollNextTask();
			command = t.getTaskCommand();
			try {
				System.out.println("executing");
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
	public void executeCommand(String cmd, String ...args) {
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
				cmdTime(args[0]);
				break;
			case "TOGGLE":
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
				cmdNewRun();
				break;
			case "ENDRUN":
				// stuff
				cmdEndRun();
				break;
			case "PRINT":
				// stuff
				cmdPrint();
				break;
			case "EXPORT":
				// stuff
				cmdExport(Integer.parseInt(args[0]));
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
				cmdTrig(Integer.parseInt(args[0]));
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
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Command " + cmd + " " + Arrays.toString(args) + " not executed because it was not the right format!");
		}
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
			//NO event should be logging because...there isn't en event to be logged till the user specifies what event to run with the cmd "for example": EVENT IND
			cmdEvent("IND");
		}
		if (channels == null) {
			channels = populateChannels();
		}
		if (detector == null) {
			detector = new AutoDetect();
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
		currentTimer.resetEventId();
		currentTimer = null;
		channels = null;
		detector = null;
		// printer set to false for insurance
		isPrinterOn = false;
	}
	
	/***
	 * Goes through the rest of participants running (AKA those who've not finished) and finishes them with DNF
	 * */
	private void cmdEndRun(){
		System.out.println("ending run");
		while(!currentTimer.getCurrentEvent().getRunningQueue().isEmpty()){
			System.out.println("setting dnf");
			currentTimer.setDnf();
		}
		System.out.println("ending things");
	}
	
	/**
	 * Resets all the collections in an event to be re filled
	 * EventID stays the same
	 * **/
	private void cmdNewRun(){
		currentTimer.getCurrentEvent().newRun();
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
		currentTimer.createEvent(EventType.valueOf(event), event);
		eventLog.logEvent(currentTimer.getEventData(), systemTime);
	}

	/**
	 * Instantiates all the variables to initial states
	 * **/
	private void cmdReset() {
		eventLog = new EventLog();
		currentTimer = new Timer(systemTime, EventType.IND, EventType.IND + "");
		eventLog.logEvent(currentTimer.getEventData(), systemTime);

		channels = populateChannels();
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
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Integer must be 1 <= " + channel + " <= 8!");
		}
		
		channels[channel - 1].setState(!channels[channel - 1].getState());
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
	 * @param sensorType - Type of sensor (EYE, GATE, PAD) to add to the given channel
	 * @param channel - ID field for a channel to connect a sensor too
	 * **/
	private void cmdConn(String sensorType, int channel) {
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Integer must be 1 <= " + channel + " <= 8!");
		}
		System.out.println("adding channel: " + (channel-1));
		channels[channel - 1].addSensor(sensorType);
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
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Integer must be 1 <= " + channel + " <= 8!");
		}
		
		channels[channel - 1].disconnectSensor();
	}

	/**
	 * Calls the EventLog's print(may not be named this after Adam changes
	 * stuff) method to output stats to the console
	 * @throws IOException 
	 * **/
	private void cmdPrint() throws IOException {
		System.out.println(eventLog.read());
		//printer.printData();
	}

	/**
	 * Copies the data from the event log and writes the data to an external device (USB) if there is one connected
	 * @throws FileNotFoundException 
	 * **/
	private void cmdExport(int exportId) {
		AbstractEvent exportEvent = null;
		for (AbstractEvent eve: currentTimer.getTotalEvents()) {
			if(eve.getEventId() == exportId){
				exportEvent = eve;
			}
		}
		if(exportEvent == null){
			System.err.println("ERROR: No event Id matching the given ID field");
			return;
		}
			
			try {

				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				// root elements
				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("logged_data");
				doc.appendChild(rootElement);

				Element eventData = doc.createElement("event_data");
				rootElement.appendChild(eventData);	


				Element timestamp = doc.createElement("timestamp");
				timestamp.appendChild(doc.createTextNode(""+systemTime));
				eventData.appendChild(timestamp);

				Element eventName = doc.createElement("event_name");
				eventName.appendChild(doc.createTextNode(""+exportEvent.getEventName()));
				eventData.appendChild(eventName);	

				Element eventId = doc.createElement("event_ID");
				eventId.appendChild(doc.createTextNode(""+exportEvent.getEventId()));
				eventData.appendChild(eventId);

				Element eventType = doc.createElement("event_type");
				eventType.appendChild(doc.createTextNode(""+exportEvent.getType()));
				eventData.appendChild(eventType);

				Element eventTime = doc.createElement("event_time");
				eventTime.appendChild(doc.createTextNode(""+exportEvent.getFormattedEventTime()));
				eventData.appendChild(eventTime);

				//END event_data root child element


				Element parRun = doc.createElement("participant_data");
				rootElement.appendChild(parRun);

				int i = 0;
				ArrayList<Element> elements = new ArrayList<Element>();
				for (Participant p: currentTimer.getCurrentEvent().getCurrentParticipants()) {
						int k = i;

						elements.add(doc.createElement("participant_event_ID_"+k));
						elements.get(i).appendChild(doc.createTextNode(""+currentTimer.getCurrentEvent().getEventId()));
						parRun.appendChild(elements.get(i));

						++i;

						elements.add(doc.createElement("participant_BIB_"+k));
						elements.get(i).appendChild(doc.createTextNode(""+p.getId()));
						parRun.appendChild(elements.get(i));

						++i;

						elements.add(doc.createElement("participant_time_"+k));
						elements.get(i).appendChild(doc.createTextNode(""+SystemTime.formatTime(p.getLastRecord().getElapsedTime())));
						parRun.appendChild(elements.get(i));
					
						++i;

				}

				//END participant_run root child element

				//END participant root element

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				DOMSource source = new DOMSource(doc);
				String saveFilePath = "" + (detector.driveConnected() ? detector.getDrive() + "/" : "") + currentTimer.getCurrentEvent().getEventName() + ".xml";
				if (detector.driveConnected()) {
					System.out.println("Saving file to external device: " + saveFilePath);
				} else {
					//there isn't a usb drive to export to...throw error and gtfo.
					System.err.println("NO USB DRIVE DETECTED\nSaving file locally to: " + saveFilePath);
				}
				StreamResult result = new StreamResult(new File(saveFilePath));

				// Output to console for testing
				// StreamResult result = new StreamResult(System.out);

				transformer.transform(source, result);

				System.out.println("File saved!");

			} catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			} catch (TransformerException tfe) {
				tfe.printStackTrace();
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
	private void cmdNum(int participantId) {
		currentTimer.addParticipantToStart(participantId);
	}

	/**
	 * ShortHand for triggering Channel 1's sensor.
	 */
	private void cmdStart() {
		cmdTrig(1);
		//currentTimer.start();
	}

	/**
	 * stop the current participant from competing but keep them as the next queued to go
	 * **/
	private void cmdCancel() {
		currentTimer.cancelStart();
	}

	/**
	 * Get the current time.
	 * **/
	private void cmdElapsed() {
		String elapsedText = "\nElapsed Time: " + currentTimer.getEventParticipantElapsedData() + "\n";
		System.out.println(elapsedText);
		//printer.addText(elapsedText);
	}

	/**
	 * ShortHand for triggering Channel 2's sensor.
	 */
	private void cmdFinish() {
		cmdTrig(2);
		//		currentTimer.finish(participantId, false);
		/*if (currentTimer.getCurrentEvent().getRunningQueue().isEmpty()) {
			eventLog.logParticipants(currentTimer.getEventParticipantData(), systemTime);
		}*/
	}

	/**
	 * End the participant within event..but...not as cool as the REGULAR finish.
	 * **/
	private void cmdDnf() {
		currentTimer.setDnf();
		//		currentTimer.finish(participantId, true);
		if (currentTimer.getCurrentEvent().getRunningQueue().isEmpty()) {
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
		running = false;
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
			for (Channel chan : channels) {
				chan.exit();
			}
		}

		id = -1;
		if (eventLog != null) {
			eventLog.exit();
		}

		detector = null;
		//cannot totally system exit for testing purposes...
		//System.exit(1);
	}

	private void cmdTrig(int channel) {
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Integer must be 1 <= " + channel + " <= 8!");
		}
		
		channels[channel - 1].triggerSensor();
		//printer.addText(""+ (channel%2==0 ? "Finishing participant" : "Starting Participants..."));
		/*if (currentTimer.getCurrentEvent().getRunningQueue().isEmpty()) {
			eventLog.logParticipants(currentTimer.getEventParticipantData(), systemTime);
		}*/
		if (channel%2==0) {
			eventLog.logParticipants(currentTimer.getEventParticipantData(), systemTime);
		}
	}

	/**
	 * Helper method that goes through all channels in channels looking for a
	 * matching ID field as the parameter id
	 * 
	 * @param id - The ID of the Channel to find.
	 * @return The Channel with ID field that matches parameter id.
	 * **/
	private Channel[] populateChannels() {
		Channel[] chans = new Channel[8];
		
		for (int i = 0; i < chans.length; ++i) {
			chans[i] = new Channel(this, null, i);
		}
		
		return chans;
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
	public void setChannels(Channel[] channels) {
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
	public Channel[] getChannels() {
		return this.channels;
	}

	/**
	 * Get's the list of all the valid commands.
	 * 
	 * @return The list of valid commands.
	 */
	public String[] getCommandList(boolean useExtendedList) {
		return taskList.getCommandList(useExtendedList);
	}
	
	public TaskList.Task getLastTask() {
		return lastTask;
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
		return isPrinterOn;
	}

	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	public void updateObservers() {
		for (Observer o : observers) {
			o.update();
		}
	}

	/**
	 * Get's the Systime's time.
	 * @return - The System's time.
	 */
	public SystemTime getSystemTime() {
		return systemTime;
	}

	public AutoDetect getAutoDetect() {
		return detector;
	}

}