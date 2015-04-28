package com.zerocool.entities;

import com.zerocool.services.SystemTime;

public class ParticipantView {

	private String eventName;
	private String startTime;
	private String finishTime;
	private String elapsed;
	private long fin;
	private int bib;
	
	public ParticipantView(Participant par) {
		Record lastRec = par.getLastRecord();
		if (lastRec != null) {
			eventName = lastRec.getEventName();
			bib = par.getId();
			fin = lastRec.getElapsedTime();
			startTime = SystemTime.formatTime(lastRec.getStartTime());
			finishTime = SystemTime.formatTime(lastRec.getFinishTime());
			elapsed = SystemTime.formatTime(lastRec.getElapsedTime());
		}
	}
	
}
