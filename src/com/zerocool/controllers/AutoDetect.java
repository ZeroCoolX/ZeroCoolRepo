package com.zerocool.controllers;

import java.io.File;
import java.util.Stack;

import org.apache.commons.lang3.SystemUtils;

import com.zerocool.gui.USBPort;

/**
 * AutoDetect FIRST stores a list of the writable open directories within the system
 * Then, it continuously creates/(overwrites each time) another list of the writable open directories within the system AT THIS MOMENT IN TIME
 * Each time the most recent or current list of directories is created/overwritten, the length of the list is compared to the original list
 * 
 * If the new list is larger than the original list, this means between the time this class was instantiated and NOW one or more (one for our implementation) new directories
 * have come into existence which indicates that an external device (USB in our implementation) has been connected.
 * This newly connected device's location (file) is stored in a stack. (Since our implementation only currently supports ONE single device...a stack works.)
 * Then the old list is updated to resemble new list making them the same length and re-loops to keep checking.
 * 
 * If the new list is smaller than the original list, then this means this means between the time this the original list was updated and NOW one or more (one for our implementation)directories
 * have disappeared or disconnected which indicates that an external device (USB in our implementation) has was disconnected.
 * In this scenario, the last device (like I said this only works with a stack for our SINGLE DEVICE implementation because technically the top device may not really be the one thats
 * removed...but in our case...it always will be ^__^) is popped from the stack and the old list is updated to copy the new list which makes their lengths equal and re-loops to begin checking again
 * 
 * IF NEITHER OF THE TWO SCENARIOS ABOVE OCCUR...it just re-loops back up to check again and again until the system is terminated
 * **/

public class AutoDetect {
	
	private Stack<File> usbDrives = new Stack<File>();
	
	private File[] oldListRoot;
	private File volumes;
	private File oldFiles[];
	private File files[];
	
	private USBPort usb;

	public AutoDetect() {
		oldListRoot = File.listRoots();
		volumes = new File("/Volumes");
		oldFiles = volumes.listFiles();
		
		if (SystemUtils.IS_OS_WINDOWS) {
			pcWaitForNotifying();
		} else if (SystemUtils.IS_OS_MAC) {
			macWaitForNotifying();
		} else {
			System.err.println("Your OS '" + SystemUtils.OS_NAME + "' is not supported!");
		}
	}

	public void setUsbPort(USBPort usbPort){
		this.usb = usbPort;
	}
	
	public File getDrive() {
		return usbDrives.peek();
	}
	
	public boolean driveConnected() {
		return usbDrives.isEmpty();
	}

	private void macWaitForNotifying() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
						volumes = new File("/Volumes");
						files = volumes.listFiles();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (files.length > oldFiles.length) {
						System.out.println("new drive detected");
						oldFiles = volumes.listFiles();
						usb.setNewText("[connected]");
						System.out.println("drive" + oldFiles[oldFiles.length - 1] + " detected");
						usbDrives.push(oldFiles[oldFiles.length - 1]);
					} else if (files.length < oldFiles.length) {
						System.out.println(oldFiles[oldFiles.length - 1] + " drive removed");
						oldFiles = volumes.listFiles();
						usb.setNewText("[         ]");
						usbDrives.pop();
					}
				}
			}
		});
		t.start();
	}

	private void pcWaitForNotifying() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (File.listRoots().length > oldListRoot.length) {
						System.out.println("new drive detected");
						usb.setNewText("[connected]");
						oldListRoot = File.listRoots();
						System.out.println("drive" + oldListRoot[oldListRoot.length - 1] + " detected");
						usbDrives.push(oldFiles[oldFiles.length - 1]);
					} else if (File.listRoots().length < oldListRoot.length) {
						System.out.println(oldListRoot[oldListRoot.length - 1] + " drive removed");
						usb.setNewText("[         ]");
						oldListRoot = File.listRoots();
						usbDrives.pop();
					}

				}
			}
		});
		t.start();
	}
}
