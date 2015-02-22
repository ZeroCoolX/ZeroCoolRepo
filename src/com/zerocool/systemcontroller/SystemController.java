package com.zerocool.systemcontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
 	
 	public SystemController(long ID){
 		this(ID, new Timer());
 	}
 	
 	public SystemController(long ID, Timer currentTimer){
 		this(ID, currentTimer, new EventLog());
 	}
 	
 	public SystemController(long ID, Timer currentTimer, EventLog eventLog){
 		this(ID, currentTimer, eventLog, new ArrayList<Channel>());
 	}
 	
 	public SystemController(long ID, Timer currentTimer, EventLog eventLog, ArrayList<Channel> channels){
 		this.ID = ID;
 		this.currentTimer = currentTimer;
 		this.eventLog = eventLog;
 		this.channels = channels;
 	}
 	
 	//read in timestamp and commands from file
 	public EventLog readFile(File file) {
 		try{
			Scanner inFile = new Scanner(new FileReader(file));
			System.out.println("reading line in file");
			while(inFile.hasNextLine()){
				String line = inFile.nextLine();
				int nextIndex = line.indexOf('>');
				int previousIndex = (line.indexOf('<')+1);
				int min = (int) (Integer.parseInt(line.substring(previousIndex+1,nextIndex)));
				previousIndex = nextIndex;
				nextIndex = line.indexOf('>', nextIndex+1);
				int sec = (int) (Integer.parseInt(line.substring(previousIndex+3, nextIndex)));
				previousIndex = nextIndex;
				nextIndex = line.indexOf('>', nextIndex+1);
				int milsec = (int) (Integer.parseInt(line.substring(previousIndex+3, nextIndex)));
				
				Date inTime = new Date();
				inTime.setMinutes(min);
				inTime.setSeconds(sec);
				inTime.setTime(milsec);

				
				String cmd = line.substring(line.indexOf("\\w"));
				//just for insurance reasons
				cmd = cmd.toUpperCase();
				executeCommand(inTime, cmd);
			}
 		}catch (Exception e) {
			System.out.println("ERROR!!!!!!!!\n" + e.getMessage());
		}
 		
 		return null;
 	}
 	
 	public EventLog readInput(String[] args) {
 		//nothing yet!
 		//niet!
 		//hahahha
 		return null;
 	}
 	
 	public void executeCommand(Date time, String cmd) {
 		
		switch(cmd){
		case "ON":
			//stuff
			break;
		case "OFF":
			//stuff
			break;
		case "EXIT":
			//stuff
			break;
		case "RESET":
			//stuff
			break;
		case "TIME":
			//stuff
			break;
		case "TOG":
			//stuff
			break;
		case "CONN":
			//stuff
			break;
		case "DISC":
			//stuff
			break;
		case "EVENT":
			//stuff
			break;
		case "NEWRUN":
			//stuff
			break;
		case "ENDRUN":
			//stuff
			break;
		case "PRINT":
			//stuff
			break;
		case "EXPORT":
			//stuff
			break;
		case "NUM":
			//stuff
			break;
		case "CLR":
			//stuff
			break;
		case "SWAP":
			//stuff
			break;
		case "RCL":
			//stuff
			break;
		case "START":
			//stuff
			break;
		case "FINISH":
			//stuff
			break;
		case "TRIG":
			//stuff
			break;
	}
 		
 	}
 	
 	//FOR THIS IMPLEMENTATION the channel supplied as a parameter's sensor's state = true
  	public void triggerSensor(int id, boolean sensorState, Channel chosenChannel) {
 		chosenChannel.setSensorState(sensorState);
 	}
  	
  	public void setID(long ID){
  		this.ID = ID;
  	}
 	
 	public void setTimer(Timer timer){
 		this.currentTimer = timer;
 	}
 	
 	public void setEventLog(EventLog eventLog){
 		this.eventLog = eventLog;
 	}
 	
 	public void setChannels(ArrayList<Channel> channels){
 		this.channels = channels;
 	}
 	
 	public Timer getTimer(){
 		return this.currentTimer;
 	}
 	
 	public EventLog getEventLog(){
 		return this.eventLog;
 	}
 	
 	public ArrayList<Channel> getChannels(){
 		return this.channels;
 	}
 	
 	public long getId(){
 		return this.ID;
 	}
 	
 	public void setIsPrinterOn(boolean isPrinterOn){
 		this.isPrinterOn = isPrinterOn;
 	}
 	
 	public boolean getIsPrinterOn(){
 		return this.isPrinterOn;
 	}
 	
	
}
