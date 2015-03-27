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

	public static enum SensorType {
		EYE, GATE, PAD
	};
	
	private int signal;
	
	private boolean isArmed;
	
	private SensorType sensorType;
	
	public Sensor(String sensorType) {
		setSensorType(sensorType);
	}
	
	public Sensor(String sensorType, boolean arm) {
		this(sensorType);
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
	 * Gets the type of the sensor.
	 * @return - The type of Sensor.
	 */
	public String getType() {
		return sensorType.toString();
	}
	
	/**
	 * Sets the type of the sensor.
	 * @param sensorType - The type of sensor to set.  Must be a valid SensorType.
	 * @throws IllegalArgumentException - If the string entered was not a valid SensorType.
	 */
	public void setSensorType(String sensorType) {
		if (sensorType != null) {
			this.sensorType = SensorType.valueOf(sensorType);
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
	public void exit() {
		signal = -1;
		isArmed = false;
	}
	
}
