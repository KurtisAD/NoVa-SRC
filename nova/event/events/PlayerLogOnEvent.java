package nova.event.events;


import nova.event.Event;

public class PlayerLogOnEvent implements Event {

	String username;
	
	public PlayerLogOnEvent(String username)
	{
		this.username = username;
	}
	
	public String getUsername()
	{
		return this.username;
	}
}
