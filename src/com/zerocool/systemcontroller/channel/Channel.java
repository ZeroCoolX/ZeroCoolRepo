package com.zerocool.systemcontroller.channel;


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

	private Sensor currentSensor;
	
	private int id;
	
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
	
	public void addSensor(String type) {
		currentSensor = new Sensor();
		currentSensor.setSensorType(type);
	}
	
	public void disconnectSensor(){
		currentSensor = null;
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
		return currentSensor != null ? currentSensor.getState() : false;
	}
	
	public String getSensorType(){
		return currentSensor != null ? currentSensor.getType() : null;
	}
	
	public int getId(){
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
	
	/**
	 * Sets the state of the current sensor.
	 * @param state - The state to set the sensor to.
	 */
	public void setSensorState(boolean state) {
		currentSensor.setState(state);
	}
	
	public void setSensorType(String type){
		currentSensor.setSensorType(type);
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void exit(){
		id = -1;
		isActive = false;
		currentSensor.exit();
		currentSensor = null;
	}
	
}
