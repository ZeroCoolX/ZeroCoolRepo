package com.zerocool.controllers;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebServiceLink {

	public static final String SERVICE_URL = "http://localhost:8888/";
	
	private static void sendParams(String params) {
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
	
	public static void setTitle(String title) {
		sendParams("title=" + title);
	}
	
	public static void setParticipantBib(int part_id, String bib) {
		sendParams("part_id=" + part_id + "&bib=" + bib);
	}
	
	public static void setParticipantName(int part_id, String name) {
		sendParams("part_id=" + part_id + "&name=" + name);
	}
	
	public static void setParticipantStart(int part_id, String start) {
		sendParams("part_id=" + part_id + "&start=" + start);
	}
	
	public static void setParticipantEnd(int part_id, String end) {
		sendParams("part_id=" + part_id + "&end=" + end);
	}
	
	public static void setParticipantElapsed(int part_id, String elapsed) {
		sendParams("part_id=" + part_id + "&elapsed=" + elapsed);
	}
	
	public static void clear() {
		sendParams("version=-1");
	}
	
}
