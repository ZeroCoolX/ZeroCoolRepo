package com.zerocool.controllers;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.zerocool.entities.ParticipantView;
import com.zerocool.gui.Observer;

public class WebServiceLink {

	public static final String SERVICE_URL = "http://localhost:8888/zc_webservice";

	public static final String APP_ENGINE_URL = "http://1-dot-zerocoolchronotimer9000.appspot.com/zc_webservice";
	
	private Gson g;
	
	public WebServiceLink() {
		g = new Gson();
	}
	
	public void postToServer(ArrayList<ParticipantView> list) {
		System.out.println(g.toJson(list));
		try {
			URL site = new URL(APP_ENGINE_URL);
			HttpURLConnection conn = (HttpURLConnection) site.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			String json = "data=" + g.toJson(list);
			out.writeBytes(json);
			out.flush();
			out.close();
			new InputStreamReader(conn.getInputStream());
			System.out.println("Sent to server");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

}
