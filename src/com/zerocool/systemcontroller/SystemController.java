package com.zerocool.systemcontroller;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.zerocool.systemcontroller.channel.Channel;
import com.zerocool.systemcontroller.event.Group;
import com.zerocool.systemcontroller.event.Individual;
import com.zerocool.systemcontroller.event.ParGroup;
import com.zerocool.systemcontroller.event.ParIndividual;
import com.zerocool.systemcontroller.eventlog.EventLog;
import com.zerocool.systemcontroller.timer.Timer;

public class SystemController {

	public Timer currentTimer;
	public EventLog eventLog;
	public ArrayList<Channel> channels;
	public boolean isPrinterOn;
	public static long ID = 0;
	public Date systemTime;
	SimpleDateFormat f = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");

	public SystemController() {
		this(ID);
	}

	public SystemController(long ID) {
		this(ID, new Timer());
	}

	public SystemController(long ID, Timer currentTimer) {
		this(ID, currentTimer, new EventLog());
	}

	public SystemController(long ID, Timer currentTimer, EventLog eventLog) {
		this(ID, currentTimer, eventLog, new ArrayList<Channel>());
	}

	public SystemController(long ID, Timer currentTimer, EventLog eventLog, ArrayList<Channel> channels) {
		this.ID = ID;
		this.currentTimer = currentTimer;
		this.eventLog = eventLog;
		this.channels = channels;
		this.systemTime = new Date();
		try{
			this.systemTime = f.parse(("" + (systemTime.getYear() + 1900) + "/"
				+ (systemTime.getMonth() + 1) + "/" + systemTime.getDate()
				+ " " + systemTime.getHours() + ":" + systemTime.getMinutes()
				+ ":" + systemTime.getSeconds() + ".000"));
		}catch(Exception e){
			System.out.println("ERROR!!!!!!!!\n" + e.getMessage());
		}
	}

	/**
	 * Read in timestamp and commands from a file.
	 * @param file - The file to read from.
	 */
	public void readFile(File file) {
		try {
			Scanner inFile = new Scanner(new FileReader(file));
			//System.out.println("reading line in file");
			
			//while there is another line to read...read it 
			while (inFile.hasNextLine()) {
				String line = inFile.nextLine();
				int nextIndex = line.indexOf(':');
				int previousIndex = 0;
				//store minute from timestamp
				//System.out.println("reading minute: " + line.substring(previousIndex, nextIndex));
				int min = (int) (Integer.parseInt(line.substring(
						previousIndex, nextIndex)));
				previousIndex = nextIndex + 1;
				nextIndex = line.indexOf('.', nextIndex);
				//store second from timestamp
				//System.out.println("reading second");
				int sec = (int) (Integer.parseInt(line.substring(
						previousIndex, nextIndex)));
				previousIndex = nextIndex + 1;
				nextIndex = line.indexOf(',', nextIndex);
				//store milisecond from timestamp
				//System.out.println("reading millisecond");
				int milsec = (int) (Integer.parseInt(line.substring(
						previousIndex, nextIndex)));
				//create new Date() object
				Date inTime = new Date();
				//create formatter
				//format the new Date object to our format
				//System.out.println("Formatting date");
				inTime = f.parse(("" + (inTime.getYear() + 1900) + "/"
						+ (inTime.getMonth() + 1) + "/" + inTime.getDate()
						+ " " + inTime.getHours() + ":" + inTime.getMinutes()
						+ ":" + inTime.getSeconds() + ".000"));
				//set minutes, seconds, and miliseconds
				inTime.setMinutes(min);
				inTime.setSeconds(sec);
				//inTime.setTime(milsec);
				//System.out.println("min = " + inTime.getMinutes());
				//System.out.println("sec = " + inTime.getSeconds());
				//System.out.println("milsec = " + inTime.getTime());

				previousIndex = nextIndex + 1;
				nextIndex = line.indexOf(',', previousIndex);
				//store command  line.indexOf("\\w")
				String cmd = line.substring(previousIndex, nextIndex);
				previousIndex = nextIndex + 1;
				// just for insurance reasons
				cmd = cmd.toUpperCase();
				//System.out.println("cmd = " + cmd);
				previousIndex = nextIndex + 1;
				boolean hasArgs = false;
				boolean parseTime = false;
				if (cmd.equals("TIME")) {//the next character boundry for parsing is ':'
					//System.out.println("cmd = " + cmd);
					hasArgs = true;
					parseTime = true;
					nextIndex = line.indexOf(':', previousIndex);
				} else if (cmd.equals("TOG") || cmd.equals("CONN") || cmd.equals("DISC") 
						|| cmd.equals("EVENT") || cmd.equals("PRINT") || cmd.equals("EXPORT")
						|| cmd.equals("NUM") || cmd.equals("CLR") || cmd.equals("TRIG")) {//the next character boundry for parsing is ','
					//System.out.println("cmd = " + cmd);
					hasArgs = true;
					parseTime = false;
					nextIndex = line.indexOf(',', previousIndex);
					//System.out.println("nextIndex = " + nextIndex);
				} else{//there is no args for the rest of cmds
					hasArgs = false;
				}
				//System.out.println("initializing arraylist");
				ArrayList<String> args = new ArrayList<String>();
				
				if (hasArgs) {
					if (parseTime) {
						int hours = (int) (Integer.parseInt(line.substring(
								previousIndex, nextIndex)));
						previousIndex = nextIndex + 1;
						nextIndex = line.indexOf('.', nextIndex);
						//store second from timestamp
						min = (int) (Integer.parseInt(line.substring(
								previousIndex, nextIndex)));
						previousIndex = nextIndex + 1;
						nextIndex = line.indexOf(',', nextIndex);
						//store milisecond from timestamp
						sec = (int) (Integer.parseInt(line.substring(
								previousIndex)));//there is no more line to parse
						
						
						//format the new Date object to our format
						Date argTime = new Date();
						argTime = f.parse(("" + (inTime.getYear() + 1900) + "/"
								+ (inTime.getMonth() + 1) + "/" + inTime.getDate()
								+ " " + inTime.getHours() + ":" + inTime.getMinutes()
								+ ":" + inTime.getSeconds() + ".000"));
						//set minutes, seconds, and miliseconds
						argTime.setHours(hours);
						argTime.setMinutes(min);
						argTime.setSeconds(sec);
						args.add(("" + argTime.getHours()));
						args.add(("" + argTime.getMinutes()));
						args.add(("" + argTime.getSeconds()));

					} else {
						//store command  line.indexOf("\\w")
						String arg = "";
						if (nextIndex == -1) {
							arg = line.substring(previousIndex);
							// just for insurance reasons
							arg = arg.toUpperCase();
							args.add(arg);
						} else {
							arg = line.substring(previousIndex, nextIndex);
							previousIndex = nextIndex + 1;
							// just for insurance reasons
							arg = arg.toUpperCase();
							args.add(arg);
							if (line.indexOf(',', previousIndex) != -1) {
								arg = line.substring(previousIndex);
								// just for insurance reasons
								arg = arg.toUpperCase();
								args.add(arg);
							}
						}
					}
					//not sure if this really makes sense..i mean it doesn't but in principle
					executeCommand(inTime, cmd, args);
					/*System.out.println("inTime = " + inTime + "\ncmd = " + cmd);
					for(String arg: args){
						System.out.println("arg = " + arg);
					}*/
				} else {
					//not sure if this really makes sense..i mean it doesn't but in principle
					executeCommand(inTime, cmd, args);
					/*System.out.println("inTime = " + inTime + "\ncmd = " + cmd);
					for(String arg: args){
						System.out.println("arg = " + arg);
					}*/
				}
			}
			String message = "";
			message += "\n"+systemTime;
			message += "\n"+currentTimer.getCurrentEvent().getType();
			System.out.println(message);
		} catch (Exception e) {
			System.out.println("ERROR!!!!!!!!\n" + e.getMessage());
		}
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public EventLog readInput(String[] args) {
		// nothing yet!
		// niet!
		// hahahha
		return null;
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
			systemTime.setHours(Integer.parseInt(args.get(0)));
			systemTime.setMinutes(Integer.parseInt(args.get(1)));
			systemTime.setSeconds(Integer.parseInt(args.get(2)));
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
	 * Set the ID of the system.
	 * @param ID - The new ID of the system.
	 */
	public void setID(long ID) {
		this.ID = ID;
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
		return this.ID;
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
