import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class ParIndividual extends Event{
	
	private EventType type;
	private String name;
	private long eventId;
	private Participant [] currentParticipants;
	//dateFormat.format(date) prints (example)   2014/08/06 15:59:48
	private DateFormat eventTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	//eventTime stored the entire date but the specific miliseconds, seconds, minutes, hours..etc can be accessed from such
	private Date eventTime;
	
	public ParIndividual(){
		super();
	}

	public ParIndividual(String name){
		super(name, EventType.GRP);
	}

	public ParIndividual(String name, EventType type){
		super(name, type, new Date());
	}
	
	public ParIndividual(String name, EventType type, Date eventTime){
		super(name, type, eventTime, -1);
	}
	
	public ParIndividual(String name, EventType type, Date eventTime, long eventId){
		super(name, type, eventTime, eventId);
	}
	
	void initializeEvent(Participant[] participants){
		currentParticipants = participants;
	}
	
	void startParticipant(){
		System.out.println("Starting ParIndividual Participants");
	}
	
	void startParticipants(){
		
	}
	
	void finishParticipant(){
		System.out.println("Finishing ParIndividual Participants");
	}

}