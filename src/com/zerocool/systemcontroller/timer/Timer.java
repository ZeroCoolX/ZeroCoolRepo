package com.zerocool.systemcontroller.timer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import org.apache.commons.lang3.time.StopWatch;

import com.zerocool.systemcontroller.event.AbstractEvent;
import com.zerocool.systemcontroller.event.Individual;
import com.zerocool.systemcontroller.eventlog.EventLog;
import com.zerocool.systemcontroller.participant.Participant;

public class Timer {
	
	private StopWatch stopwatch;
	private Date sysTime;
	private ArrayList<Participant> totalParticipants;
	private AbstractEvent currentEvent;
	private EventLog eventLogger;
	
	SimpleDateFormat f = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");

	
	/*
	 * Just a question, why are the constructors going in reverse?
	 * I think you've totally got the right idea but just did it in reverse accidentally! ^__^
	 * 
	 * Like there is the default Timer() constructor which creates a new stopwatch. But there also needs to be an AbstractEvent object created as well if there isn't one given and the D2L 
	 * document states the default event is of type IND (individual). Also it needs to create a new EventLog object since there isn't one given.
	 * 
	 * Then one level higher there is the Timer(AbstractEvent) constructor who is given an abstract event and this first calls the default
	 * no parameter constructor. But since the no parameter constructor must account for creating the default event if it's called it sets currentEvent = new Individual(), then jumps BACK 
	 * to the second parameter constructor and now sets currentEvent = event. The no parameter constructor is creating a new event, then the second constructor is immediately overwriting it.
	 * Also, since the second constructor isn't given an EventLog either, it must create a new one of those too. I mean, technically you could have the no parameter create it and be done with it 
	 * but there's still a problem in the third constructor.
	 * 
	 * Just like the second constructor, there is redundancy issues. The third constructor is given a time, event, and event log
	 * NOTHING is done with the time that is given. Its not set anywhere, nor passed in.
	 * The second parameter is called with the event given, which then calls the first constructor which sets the default EventLog and Time and AbstractEvent objects, 
	 * then jumps back to the second constructor and overwrites the Event object just created, then jumps back to the third constructor and overwrites the  EventLog
	 * that was created in the first constructor.
	 * 
	 * Also nowhere in here is there a constructor that is given a/many participants? The SystemController, which parses the data passed in will have a list of
	 * participants which can be given to the Timer class, or the timer can create an empty ArrayList of Participant objects. 
	 * Maybe the SystemController class will actually give the number of participants and instead of a taking in a ArrayList of Participant objects it will take
	 * in a number and based off this number create X amnount of participants, and store them in totalParticipants. IDK yet, as of right now lets just say we're given a list
	 * from the SystemController
	 * 
	 * I commented out your constructors and reworked them to go from the lowest default/no parameter constructor to the highest full parameter constructor
	 * */

	/*public Timer() {
		stopwatch = new StopWatch();
		//the D2L document states that the default event is of type IND (individual)
		currentEvent = new Individual();
		//If no EventLog is given, by default one needs to be created.
		eventLogger = new EventLog();
	}
	
	public Timer(AbstractEvent event) {
		this();
		//This overwrites the AbstractEvent object the no parameter constructor just created..
		this.currentEvent=event;
	}
	
	public Timer(long time, AbstractEvent event, EventLog eventLog) {
		this(event);
		//this overwrites the EventLog the no parameter constructor just created...
		this.eventLogger=eventLog;
	}*/
	
	//if there is nothing given the constructors cascade UP giving the appropriate data for each level
	//see this way no matter what data is given, they alwasy go from small to large so nothing is created twice and overwritten.
	public Timer() {
		this(new Date());
	}
	
	public Timer(Date time){
		this(time, new Individual());
	}
	
	public Timer(Date time, AbstractEvent event){
		this(time, event, new ArrayList<Participant>());
	}
	
	public Timer(Date time, AbstractEvent event, ArrayList<Participant> participants){
		this(time, event, participants, new EventLog());
	}
	
	public Timer(Date time, AbstractEvent event, ArrayList<Participant> participants, EventLog eventLog){
		this.stopwatch = new StopWatch();
		this.currentEvent = event;
		this.totalParticipants = participants;
		this.eventLogger = eventLog;
		try{
			time = f.parse( (""+(time.getYear()+1900)+"/"+(time.getMonth()+1)+"/"+time.getDate()+" "+time.getHours()+":"+time.getMinutes()+":"+time.getSeconds()+".000") );
		}catch(Exception e){
			System.out.println("ERROR!!!! " + e.getMessage());
		}
		this.sysTime = time;
	}
	
	
	
	// ----- functional methods ----- \\
	
	public void startEvent() { 
		stopwatch.start();
		//Must actually initialize the event
		currentEvent.initializeEvent(totalParticipants);
		//Then start the event (which also fills in the event name and eventId of each participants record)
		currentEvent.startParticipants();
	}
	
	public void endEvent() { 
		stopwatch.stop(); 
		//Finish all participants (for this sprint this will work)
		//Further on, when we're doing more that just one single participant we need functionality to start and finish specific participants
		currentEvent.finishParticipants();
	}
	
	public void resetTime() { stopwatch.reset(); }
	
	
	// ----- accessors ----- \\
	
	public long getEventTime() { return stopwatch.getStartTime(); }
	
	public Date getSysTime(){
		return this.sysTime;
	}
	
	public AbstractEvent getEvent(){
		return this.currentEvent;
	}
	
	public ArrayList<Participant> getTotalParticipants() { return totalParticipants; }
	
	public AbstractEvent getCurrentEvent() { return currentEvent; }
	
	
	// ----- mutators ----- \\
	
	public void setEventLog(EventLog eventLog) { eventLogger=eventLog; }
	
	public void setEvent(AbstractEvent event) { currentEvent=event;}
	
	public void setSysTime(Date time){
		this.sysTime = time;
	}
	
}
