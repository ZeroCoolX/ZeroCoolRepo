package com.zerocool.systemcontroller.channel;

public class Channel {

	private Sensor currentSensor;
	
	private boolean isActive;
	
	/**
	 * Default constructor.  Nothing to do.
	 */
	public Channel() {
		// nothign to do
	}
	
	/**
	 * Constructor to add a sensor.
	 * @param sensor - sensor to add to the channel.
	 */
	public Channel(Sensor sensor) {
		currentSensor = sensor;
	}
	
	/**
	 * Constructor to add a sensor and set the channel active er naw.
	 * @param sensor - sensor to add to the channel.
	 * @param active - set the channel to be active er naw.
	 */
	public Channel(Sensor sensor, boolean active) {
		this(sensor);
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
	 * Removes the channel's current sensor.
	 */
	public void removeSensor() {
		currentSensor = null;
	}
	
	
	// ----- accessors ----- \\
	
	/**
	 * Check if the current channel is activated er naw.
	 * @return - true if isActive else false
	 */
	public boolean getState() {
		return isActive;
	}
	
	public boolean getSensorState() {
		return true;
	}
	
	
}