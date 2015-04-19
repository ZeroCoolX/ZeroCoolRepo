package com.zerocool.controllers;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebServiceLink {

	public static final String SERVICE_URL = "http://localhost:8888/zerocoolwebservice";
	
	private static void sendParams(String params){
		try {
			URL site = new URL(SERVICE_URL);
			HttpURLConnection conn = (HttpURLConnection) site.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			String content = params;
			System.out.println(content);
			out.writeBytes(content);
			out.flush();
			out.close();
			System.out.println("Sent to server");
			new InputStreamReader(conn.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void setTitle(String title){
		sendParams("title="+title);
	}
	
}
