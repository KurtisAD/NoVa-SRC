package nova.events;


public class PlayerLogOnEvent {

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
