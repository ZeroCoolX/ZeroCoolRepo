package com.zerocool.controllers;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebServiceLink {

	public static final String SERVICE_URL = "http://localhost:8888/zc_webservice";
	
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
	
	private static void setParticipantBib(int part_id, String bib){
		sendParams("part_id="+part_id+"&bib="+bib);
	}
	
	private static void setParticipantName(int part_id, String name){
		sendParams("part_id="+part_id+"&name="+name);
	}
	
	private static void setParticipantStart(int part_id, String start){
		sendParams("part_id="+part_id+"&start="+start);
	}
	
	private static void setParticipantEnd(int part_id, String end){
		sendParams("part_id="+part_id+"&end="+end);
	}
	
	private static void setParticipantElapsed(int part_id, String elapsed){
		sendParams("part_id="+part_id+"&elapsed="+elapsed);
	}
	
	private static void clear(){
		sendParams("version=-1");
	}
	
}
