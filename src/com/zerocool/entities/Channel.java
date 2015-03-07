package com.zerocool.entities;
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
		// nothing to do
	}
	
	/**
	 * Constructor to add a sensor.
	 * @param type - sensor to add to the channel.  Should be of type EYE, GATE, or PAD.
	 */
	public Channel(String type) throws IllegalArgumentException{
		if(!type.equalsIgnoreCase("EYE") || !type.equalsIgnoreCase("GATE") || !type.equalsIgnoreCase("PAD"))
			throw new IllegalArgumentException();
		currentSensor = new Sensor();
		currentSensor.setSensorType(type);
	}
	
	/**
	 * Constructor to add a sensor and set the channel active er naw.
	 * @param type - sensor to add to the channel.  Should be of type EYE, GATE, or PAD.
	 * @param active - set the channel to be active er naw.
	 */
	public Channel(String type, boolean active) {
		this(type);
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
	public void addSensor(String type) throws IllegalArgumentException {
		if(type.equalsIgnoreCase("EYE") || type.equalsIgnoreCase("GATE") || type.equalsIgnoreCase("PAD")){
			currentSensor = new Sensor();
			currentSensor.setSensorType(type);
			currentSensor.setState(false);
		}else{
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Disconnects the current sensor by setting it to null.
	 */
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
	
	/**
	 * Gets the sensor type.  If there is no sensor type
	 * @return
	 */
	public String getSensorType(){
		return currentSensor != null ? currentSensor.getType() : null;
	}
	
	/**
	 * Gets the id of the Channel
	 * @return - an integer representing the Channel's id number.
	 */
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
