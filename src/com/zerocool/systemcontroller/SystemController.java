package com.zerocool.systemcontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.lang3.time.StopWatch;

import com.zerocool.systemcontroller.channel.Channel;
import com.zerocool.systemcontroller.eventlog.EventLog;
import com.zerocool.systemcontroller.timer.Timer;

public class SystemController {

	public Timer currentTimer;
	public EventLog eventLog;
	public ArrayList<Channel> channels;
	public boolean isPrinterOn;
	public static long ID = 0;

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

	public SystemController(long ID, Timer currentTimer, EventLog eventLog,
			ArrayList<Channel> channels) {
		this.ID = ID;
		this.currentTimer = currentTimer;
		this.eventLog = eventLog;
		this.channels = channels;
	}

	// read in timestamp and commands from file
	public EventLog readFile(File file) {
		try {
			Scanner inFile = new Scanner(new FileReader(file));
			System.out.println("reading line in file");
			
			//while there is another line to read...read it 
			while (inFile.hasNextLine()) {
				String line = inFile.nextLine();
				int nextIndex = line.indexOf(':');
				int previousIndex = 0;
				//store minute from timestamp
				int min = (int) (Integer.parseInt(line.substring(
						previousIndex, nextIndex)));
				previousIndex = nextIndex+1;
				nextIndex = line.indexOf('.', nextIndex);
				//store second from timestamp
				int sec = (int) (Integer.parseInt(line.substring(
						previousIndex, nextIndex)));
				previousIndex = nextIndex+1;
				nextIndex = line.indexOf(',', nextIndex);
				//store milisecond from timestamp
				int milsec = (int) (Integer.parseInt(line.substring(
						previousIndex, nextIndex)));
				//create new Date() object
				Date inTime = new Date();
				//create formatter
				SimpleDateFormat f = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
				//format the new Date object to our format
				inTime = f.parse(("" + (inTime.getYear() + 1900) + "/"
						+ (inTime.getMonth() + 1) + "/" + inTime.getDate()
						+ " " + inTime.getHours() + ":" + inTime.getMinutes()
						+ ":" + inTime.getSeconds() + ".000"));
				//set minutes, seconds, and miliseconds
				inTime.setMinutes(min);
				inTime.setSeconds(sec);
				inTime.setTime(milsec);
				
				previousIndex = nextIndex+1;
				
				//store command  line.indexOf("\\w")
				String cmd = line.substring(previousIndex, nextIndex);
				// just for insurance reasons
				cmd = cmd.toUpperCase();
				previousIndex = nextIndex+1;
				boolean hasArgs = false;
				boolean parseTime = false;
				if(cmd.equals("TIME")){//the next character boundry for parsing is ':'
					hasArgs = true;
					parseTime = true;
					nextIndex = line.indexOf(':', nextIndex);
				}else if(cmd.equals("TOG") || cmd.equals("CONN") || cmd.equals("DISC") 
						|| cmd.equals("EVENT") || cmd.equals("PRINT") || cmd.equals("EXPORT")
						|| cmd.equals("NUM") || cmd.equals("CLR") || cmd.equals("TRIG")){//the next character boundry for parsing is ','
					hasArgs = true;
					parseTime = false;
					nextIndex = line.indexOf(',', nextIndex);
				}else{//there is no args for the rest of cmds
					hasArgs = false;
				}
				ArrayList<String> args = new ArrayList<String>();
				
				if(hasArgs){
					if(parseTime){
						min = (int) (Integer.parseInt(line.substring(
								previousIndex, nextIndex)));
						previousIndex = nextIndex+1;
						nextIndex = line.indexOf('.', nextIndex);
						//store second from timestamp
						sec = (int) (Integer.parseInt(line.substring(
								previousIndex, nextIndex)));
						previousIndex = nextIndex+1;
						nextIndex = line.indexOf(',', nextIndex);
						//store milisecond from timestamp
						milsec = (int) (Integer.parseInt(line.substring(
								previousIndex)));//there is no more line to parse
						
						
						//format the new Date object to our format
						Date argTime = new Date();
						argTime = f.parse(("" + (inTime.getYear() + 1900) + "/"
								+ (inTime.getMonth() + 1) + "/" + inTime.getDate()
								+ " " + inTime.getHours() + ":" + inTime.getMinutes()
								+ ":" + inTime.getSeconds() + ".000"));
						//set minutes, seconds, and miliseconds
						argTime.setMinutes(min);
						argTime.setSeconds(sec);
						argTime.setTime(milsec);
						args.add((""+argTime.getHours()));
						args.add((""+argTime.getMinutes()));
						args.add((""+argTime.getSeconds()));

					}else{
						//store command  line.indexOf("\\w")
						String arg = line.substring(previousIndex, nextIndex);
						// just for insurance reasons
						arg = arg.toUpperCase();
						args.add(arg);
						if(line.indexOf(',') != -1){
							arg = line.substring(previousIndex, nextIndex);
							// just for insurance reasons
							arg = arg.toUpperCase();
							args.add(arg);
						}
					}
					executeCommand(inTime, cmd, args);
				}else{
					executeCommand(inTime, cmd, args);
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR!!!!!!!!\n" + e.getMessage());
		}

		return null;
	}

	public EventLog readInput(String[] args) {
		// nothing yet!
		// niet!
		// hahahha
		return null;
	}

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
			// stuff
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
			// stuff
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

	public void setID(long ID) {
		this.ID = ID;
	}

	public void setTimer(Timer timer) {
		this.currentTimer = timer;
	}

	public void setEventLog(EventLog eventLog) {
		this.eventLog = eventLog;
	}

	public void setChannels(ArrayList<Channel> channels) {
		this.channels = channels;
	}

	public Timer getTimer() {
		return this.currentTimer;
	}

	public EventLog getEventLog() {
		return this.eventLog;
	}

	public ArrayList<Channel> getChannels() {
		return this.channels;
	}

	public long getId() {
		return this.ID;
	}

	public void setIsPrinterOn(boolean isPrinterOn) {
		this.isPrinterOn = isPrinterOn;
	}

	public boolean getIsPrinterOn() {
		return this.isPrinterOn;
	}

}
