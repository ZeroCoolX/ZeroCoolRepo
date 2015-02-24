package com.zerocool.systemcontroller.systemtime;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.StopWatch;

public class SystemTime extends StopWatch {

	// Used when setting the time of the system to a specific time.
	private long offset;
	
	public SystemTime() {
		super();
	}
	
	public SystemTime(long offset) {
		this();
		this.offset = offset;
	}
	
	/**
	 * Used to format milliseconds to the desired look.
	 * @param millis - The milliseconds to format.
	 * @return A string representing the milliseconds in the desired format.
	 */
	public static String formatTime(long millis) {
		return DurationFormatUtils.formatDuration(millis, "HH:mm:ss.S");
	}
	
	/**
	 * Gets the offset.
	 * @return The offset of the time.
	 */
	public long getOffset() {
		return offset;
	}
	
	/**
	 * Set the offset of the time.
	 * @param millis - How many milliseconds to offset by.
	 */
	public void setOffset(long millis) {
		offset = millis;
	}
	
	/**
	 * Just overrides the original method to add the offset so we get the
	 * desired result.
	 */
	@Override
	public long getTime() {
		return super.getTime() + offset;
	}
	
	/**
	 * Overrides the toString() method to output the formatted version.
	 */
	@Override
	public String toString() {
		return formatTime(getTime());
	}
	
	public void exit(){
		offset = 000;
	}
	
}
