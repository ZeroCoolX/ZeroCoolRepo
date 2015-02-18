import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Individual extends Event{
	
	private EventType type;
	private String name;
	private long eventId;
	//only one Participant is needed since this is an Individual event
	private Participant currentParticipant;
	//dateFormat.format(date) prints (example)   2014/08/06 15:59:48
	private DateFormat eventTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	//eventTime stored the entire date but the specific miliseconds, seconds, minutes, hours..etc can be accessed from such
	private Date eventTime;
	
	public Individual(){
		super();
	}

	public Individual(String name){
		super(name, EventType.IND);
	}

	public Individual(String name, EventType type){
		super(name, type, new Date());
	}
	
	public Individual(String name, EventType type, Date eventTime){
		super(name, type, eventTime, -1);
	}
	
	public Individual(String name, EventType type, Date eventTime, long eventId){
		super(name, type, eventTime, eventId);
	}
	
	void initializeEvent(Participant[] participants){
		currentParticipant = participants[0];
		
	}
	
	void startParticipant(){
		System.out.println("Starting Individual Participants");
	}
	
	void startParticipants(){
		
	}
	
	void finishParticipant(){
		System.out.println("Finishing Individual Participants");
	}

}
