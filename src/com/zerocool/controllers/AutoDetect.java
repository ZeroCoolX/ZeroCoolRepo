package com.zerocool.controllers;

import java.io.File;
import java.util.Stack;

public class AutoDetect {

	private static File volumes = new File("/Volumes");
    private static File oldFiles[] = volumes.listFiles();
    private static File files[];
    
    public static Stack<File> usbDrives = new Stack<File>();
    
    	public AutoDetect(){
    		main(new String[1]);
    	}

 
	    public static void main(String[] args) {
	        AutoDetect.waitForNotifying();
	    }

	    public static void waitForNotifying() {
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
	                        System.out.println("drive"+oldFiles[oldFiles.length-1]+" detected");
	                        usbDrives.push(oldFiles[oldFiles.length-1]);

	                    } else if (files.length < oldFiles.length) {
	        System.out.println(oldFiles[oldFiles.length-1]+" drive removed");

	                        oldFiles = volumes.listFiles();
	                        usbDrives.pop();
	                    }

	                }
	            }
	        });
	        t.start();
	    }
}
