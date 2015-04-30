package com.zerocool.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

	private Thread loop;
	
	private WebServiceLink server;
	private TaskList taskList;
	private SystemTime systemTime;
	private Timer currentTimer;
	private EventLog eventLog;
	private AutoDetect detector;
	private Task lastTask;

	private boolean isPrinterOn;
	private boolean shouldPrint;
	private boolean running;
	
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
							executeCommand(task.getTaskCommand(), task.getTaskArgumentOne(), task.getTaskArgumentTwo());
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
	 * USE THIS FOR TESTING PURPOSES ONLY!
	 * 
	 * This method returns the TaskList of the SystemController.  This is very dangerous and
	 * should only be used for testing the SystemController.
	 * @return The TaskList.
	 */
	public TaskList getTaskList() {
		return taskList;
	}
	
	/**
	 * USE THIS FOR TESTING PURPOSES ONLY!
	 * 
	 * This method returns the current Timer.  This should only be used for testing.
	 * All of the other classes should be able to get all the needed info from this class.
	 * 
	 * @return The current Timer.
	 */
	public Timer getTimer() {
		// TODO Some classes use this method, change that.  (Sensor)
		return currentTimer;
	}
	
	/**
	 * USE THIS FOR TESTING PURPOSES ONLY!
	 * 
	 * This method returns the current EventLog.
	 * 
	 * @return The current EventLog.
	 */
	public EventLog getEventLog() {
		return eventLog;
	}
	
	/**
	 * USE THIS FOR TESTING PURPOSES ONLY!
	 * 
	 * This method returns the current array of Channels.
	 * 
	 * @return The current ArrayList of Channels.
	 */
	public Channel[] getChannels() {
		return channels;
	}
	
	/**
	 * USE THIS FOR TESTING PUROSES ONLY!
	 * 
	 * Gets the current USB detector.
	 * 
	 * @return The current USB detector.
	 */
	public AutoDetect getAutoDetect() {
		return detector;
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
					executeCommand(task.getTaskCommand(), task.getTaskArgumentOne(), task.getTaskArgumentTwo());
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
	 * Execute a command.
	 * 
	 * @param time - The current time
	 * @param cmd - The command to execute.
	 * @param args - Types of events to run.
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
	 * @throws IllegalArgumentException, IOException 
	 * 
	 */
	private void executeCommand(String cmd, String ...args) {
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
				cmdExport();
				break;
			case "NUM":
				// stuff
				cmdNum(Integer.parseInt(args[0]));
				break;
			case "CLR":
				cmdClr(Integer.parseInt(args[0]));
				break;
			case "SWAP":
				cmdSwap();
				break;
			case "RCL":
				cmdRcl();
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
			System.err.println(e.getMessage());
		}
	}

	private void cmdSwap() {
		currentTimer.swap();
	}
	
	private void cmdClr(int participantId) {
		currentTimer.clear(participantId);
	}
	
	private void cmdRcl() {
		if (lastTrigger > 0 && lastTrigger < 9) {
			cmdTrig(lastTrigger);
		}
	}
	
	/**
	 * Instantiates all the variables to initial states.
	 */
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
	 * Keep the SystemTime running but set everything else to null.
	 **/
	private void cmdOff() {
		// When the command OFF is entered/read then the time needs to stop.
		eventLog = null;
		currentTimer.resetEventId();
		currentTimer = null;
		channels = null;
		detector = null;
		isPrinterOn = false;
	}
	
	/**
	 * Ends the current running event if there is one.
	 **/
	private void cmdEndRun() {
		currentTimer.setDnf();
		
		server.postToServer(currentTimer.getEventParticipantView());
	}
	
	/**
	 * Creates a new Run.
	 **/
	private void cmdNewRun() {
		currentTimer.createNewRun();
	}

	/**
	 * Creates a new Event based upon the given String.  If the String is not a valid
	 * event, shit hits the fan.
	 * 
	 * @param event - The type of event to create.
	 */
	private void cmdEvent(String event) {
		currentTimer.createEvent(EventType.valueOf(event), event);
		eventLog.logEvent(currentTimer.getEventData(), systemTime);
	}

	/**
	 * Instantiates all the variables to initial states
	 **/
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
	 * from the given String.
	 * 
	 * @param time - String in this format: HR:MIN:SEC
	 **/
	private void cmdTime(String time) {
		// set the current time
		systemTime.setTime(time);
		systemTime.start();
	}

	/**
	 * Turns the given channel on if it was off or off if it was on.
	 * 
	 * @param channel - The channel to toggle.  [1, 8]
	 * @throws IllegalArgumentException If the channel was not [1, 8].
	 */
	private void cmdTog(int channel) {
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Invalid channel number. Channels are 1-8.");
		}
		
		channels[channel - 1].setState(!channels[channel - 1].getState());
	}

	/**
	 * Connects a sensor to the given channel.
	 * 
	 * @param sensorType - The type of sensor to add to the channel.  [GATE, EYE, PAD]
	 * @param channel - The channel to add the sensor to.
	 * @throws IllegalArgumentException If the channel was not [1, 8].
	 * @throws IllegalArgumentException If the string entered was not a valid SensorType.
	 */
	private void cmdConn(String sensorType, int channel) {
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Sensor cannot connect. Invalid channel number, channels are 1-8.");
		}
		
		channels[channel - 1].addSensor(sensorType);
	}

	/**
	 * Disconnects the given channel.
	 * 
	 * @param channel - The channel to disconnect. [1, 8]
	 * @throws IllegalArgumentException If the channel was not [1, 8].
	 */
	private void cmdDisc(int channel) {
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Invalid channel number. Channels are 1-8.");
		}
		
		channels[channel - 1].disconnectSensor();
	}

	/**
	 * Prints a specified run.
	 * 
	 * @throws IOException
	 */
	private void cmdPrint() throws IOException {
		shouldPrint = true;
	}

	/**
	 * Copies the data from the event log and writes the data to an external device (USB) if there is one connected.
	 * 
	 * @throws FileNotFoundException 
	 **/
	private void cmdExport() {
		AbstractEvent exportEvent = currentTimer.getCurrentEvent();

		if (exportEvent == null) {
			System.err.println("ERROR: No event!");
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
				String saveFilePath = "" + (detector.driveConnected() ? detector.getDrive() + "/" : "") + exportEvent.getEventName() + exportEvent.getEventId() + ".xml";
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

			} catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			} catch (TransformerException tfe) {
				tfe.printStackTrace();
			}	
			
	}

	/**
	 * Adds a Participant to the current event.
	 * 
	 * @param participantId - The id of the Participant.
	 */
	private void cmdNum(int participantId) {
		currentTimer.addParticipantToStart(participantId);
	}

	/**
	 * ShortHand for triggering Channel 1's sensor.
	 */
	private void cmdStart() {
		cmdTrig(1);
	}

	/**
	 * Cancels the current run and resets it.
	 */
	private void cmdCancel() {
		currentTimer.cancelStart();
	}

	/**
	 * Print the elapsed time of all the finished participants.
	 */
	private void cmdElapsed() {
		String elapsedText = "\nElapsed Time: " + currentTimer.getEventParticipantElapsedData() + "\n";
		System.out.println(elapsedText);
		// TODO Does this even work?
	}

	/**
	 * ShortHand for triggering Channel 2's sensor.
	 */
	private void cmdFinish() {
		cmdTrig(2);
	}

	/**
	 * End the participant within event..but...not as cool as the REGULAR finish.
	 */
	private void cmdDnf() {
		currentTimer.setDnf();
		
		if (currentTimer.getCurrentEvent().getRunningQueue().isEmpty()) {
			eventLog.logParticipants(currentTimer.getEventParticipantData(), systemTime);
		}
	}

	/**
	 * Exit the entire system. Go through all global variables calling their
	 * .exit() function and/or set them to null
	 */
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

		if (eventLog != null) {
			eventLog.exit();
		}

		detector = null;
		loop.interrupt();
		//cannot totally system exit for testing purposes...
		//System.exit(1);
	}

	/**
	 * Manually triggers a sensor on a specified channel.
	 * 
	 * @param channel - The channel of the sensor to trigger.
	 * @throws IllegalArgumentException If the channel was not [1, 8].
	 */
	private void cmdTrig(int channel) throws IllegalStateException {
		if (channel < 1 || channel > 8) {
			throw new IllegalArgumentException("Invalid channel number. Channels are 1-8.");
		}
		
		lastTrigger = channel;
		channels[channel - 1].triggerSensor();
		
		// TODO This is not always true.  In PAR events it is possible that a top channel
		//	could finish participants.
		if (channel % 2 == 0) {
			eventLog.logParticipants(currentTimer.getEventParticipantData(), systemTime);
		}
	}

	/**
	 * Creates an array of 8 Channels.
	 * 
	 * @return - The array of 8 Channels created.
	 */
	private Channel[] populateChannels() {
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
	 * Gets the Systime's time.
	 * 
	 * @return The System's time.
	 */
	public SystemTime getSystemTime() {
		return systemTime;
	}

}