package com.zerocool.entities;

/**
 * 
 * @author ZeroCool
 * The Sensor Class
 * 
 * This class represents a sensor.  A sensor is 'triggered' either by 
 * a participant or manually on the console.  The signal is to only be
 * useful if the sensor is armed and is for starting/stopping times of
 * participants.
 *
 */
public class Sensor {

	private int signal;
	
	private boolean isArmed;
	
	private SensorType sensorType;
	
	public enum SensorType {
		EYE, GATE, PAD
	};
	
	public Sensor() {
		// do nothing
	}
	
	public Sensor(boolean arm) {
		isArmed = arm;
	}
	
	
	// ----- accessors ----- \\
	
	/**
	 * Checks if the sensor is armed er naw.
	 * @return - true if the sensor is armed else false.
	 */
	public boolean getState() {
		return isArmed;
	}
	
	/**
	 * Gets the type of the sensor
	 * @return a type of either EYE, GATE or PAD
	 */
	public String getType(){
		return sensorType.toString();
	}
	
	/**
	 * Sets the type of the sensor
	 * @param the type (a type of either EYE, GATE or PAD)
	 */
	public void setSensorType(String type){
		switch(type) { 
			case "EYE":
				sensorType = SensorType.EYE;
				break;
			case "GATE":
				sensorType = SensorType.GATE;
				break;
			case "PAD":
				sensorType = SensorType.PAD;
				break;
		}
	}
	
	
	// ----- mutators ----- \\
	
	/**
	 * Arms or disarms the sensor.
	 * @param arm - sets isArmed to true or false.
	 */
	public void setState(boolean arm) {
		isArmed = arm;
	}
	
	/**
	 * Exits the sensor when the system is exited
	 */
	public void exit(){
		signal = -1;
		isArmed = false;
	}
	
}
