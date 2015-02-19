package com.zerocool.systemcontroller.channel;

public class Sensor {

	private int signal;
	
	private boolean isArmed;
	
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
	
	
	// ----- mutators ----- \\
	
	/**
	 * Arms or disarms the sensor.
	 * @param arm - sets isArmed to true or false.
	 */
	public void setState(boolean arm) {
		isArmed = arm;
	}
	
}
