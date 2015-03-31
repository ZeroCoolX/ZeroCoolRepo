package com.zerocool.entities;

import com.zerocool.controllers.SystemController;

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
	
	private SystemController admin;
	
	private boolean triggered;
	
	private SensorType sensorType;
	
	private int id;
	
	public Sensor(SystemController admin, String sensorType, int id) {
		this.admin = admin;
		this.id = id;
		setSensorType(sensorType);
	}
	
	public void trigger() {
		admin.getTimer().triggered(id + 1);
		triggered = true;
	}
	
	// ----- accessors ----- \\
	
	public boolean getTrigger() {
		return triggered;
	}
	
	/**
	 * Gets the type of the sensor.
	 * @return - The type of Sensor.
	 */
	public String getType() {
		return sensorType != null ? sensorType.toString() : null;
	}
	
	/**
	 * Sets the type of the sensor.
	 * @param sensorType - The type of sensor to set.  Must be a valid SensorType.
	 * @throws IllegalArgumentException - If the string entered was not a valid SensorType.
	 */
	public void setSensorType(String sensorType) throws IllegalArgumentException {
		if (sensorType != null) {
			this.sensorType = SensorType.valueOf(sensorType);
		}
	}
	
	
	// ----- mutators ----- \\
	
	public void resetTrigger() {
		triggered = false;
	}
	
	/**
	 * Exits the sensor when the system is exited
	 */
	public void exit() {
		triggered = false;
		sensorType = null;
	}
	
}
