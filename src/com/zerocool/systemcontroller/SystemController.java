package com.zerocool.systemcontroller;

import java.io.File;
import java.util.ArrayList;

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
 	
 	public EventLog readFile(File file) {
 		
 		return null;
 	}
 	
 	public EventLog readInput(String[] args) {
 		
 		return null;
 	}
 	
 	public void executeCommand(String cmd) {
 		
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
