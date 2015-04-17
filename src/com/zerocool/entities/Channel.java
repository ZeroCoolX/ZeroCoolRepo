package com.zerocool.entities;

import com.zerocool.controllers.SystemController;

/**
 * 
 * @author ZeroCool
 * The Channel Class
 * 
 * This class represents a channel for a sensor to be connected to.
 * Each channel has exactly 0 or 1 sensors to trigger an event.
 * 
 */
public class Channel {

	private SystemController admin;
	private Sensor currentSensor;

	private int id;

	private boolean isActive;
	
	/**
	 * Constructor to add a sensor.
	 * @param type - sensor to add to the channel.  Should be of type EYE, GATE, or PAD.
	 */
	public Channel(SystemController admin, String sensorType, int id) {
		this.admin = admin;
		if (sensorType != null) {
			currentSensor = new Sensor(admin, sensorType, id);
		}
		this.id = id;
	}

	/**
	 * Constructor to add a sensor and set the channel active er naw.
	 * @param type - sensor to add to the channel.  Should be of type EYE, GATE, or PAD.
	 * @param active - set the channel to be active er naw.
	 */
	public Channel(SystemController admin, String sensorType, int id, boolean active) {
		this(admin, sensorType, id);
		isActive = active;
	}


	// ----- functional methods ----- \\

	/**
	 * Add/Change the channel's sensor.
	 * @param sensor - the sensor to add/change to the channel.
	 */
	public void addSensor(Sensor sensor) {
		currentSensor = sensor;
	}

	/**
	 * Adds a sensor of the type of passed in string
	 * @param type - should either be EYE, GATE, or PAD
	 */
	public void addSensor(String sensorType) throws IllegalArgumentException {
		currentSensor = new Sensor(admin, sensorType, id);
	}

	/**
	 * Disconnects the current sensor by setting it to null.
	 */
	public void disconnectSensor() {
		currentSensor = null;
	}
	
	/**
	 * Triggers the current sensor if there is one.
	 */
	public boolean triggerSensor() throws IllegalStateException {
		if (currentSensor != null && isActive) {
			currentSensor.trigger();
			return true;
		}
		
		return false;
	}


	// ----- accessors ----- \\

	/**
	 * Check if the current channel is activated er naw.
	 * @return - true if isActive else false.
	 */
	public boolean getState() {
		return isActive;
	}


	/**
	 * Check if the sensor is armed or not.
	 * @return - true if the sensor is armed else false.
	 */
	public boolean getSensorState() {
		return getSensorType() != null;
	}

	public boolean getSensorTrigger() {
		return currentSensor != null && currentSensor.getTrigger();
	}
	
	/**
	 * Gets the sensor type.  If there is no sensor type
	 * @return
	 */
	public String getSensorType() {
		return currentSensor != null ? currentSensor.getType() : null;
	}

	/**
	 * USE FOR TEST PURPOSES ONLY!
	 * Get's the current sensor.
	 * @return - The current sensor or null if there isn't one.
	 */
	public Sensor getSensor() {
		return currentSensor;
	}
	
	/**
	 * Gets the id of the Channel
	 * @return - an integer representing the Channel's id number.
	 */
	public int getId() {
		return id;
	}


	// ----- mutators ----- \\

	/**
	 * Sets the state of the channel to active er naw.
	 * @param active - The state to set the channel to.
	 */
	public void setState(boolean active) {
		this.isActive = active;
	}

	public void setSensorType(String sensorType) throws IllegalArgumentException {
		if (currentSensor != null) {
			currentSensor.setSensorType(sensorType);
		} else {
			this.addSensor(sensorType);
		}
		
	}

	public void resetSensorTrigger() {
		if (currentSensor != null) {
			currentSensor.resetTrigger();
		}
	}
	
	public void setID(int id) {
		this.id = id;
	}

	public void exit() {
		id = -1;
		isActive = false;
		if (currentSensor != null) {
			currentSensor.exit();
		}
		currentSensor = null;
	}

}
