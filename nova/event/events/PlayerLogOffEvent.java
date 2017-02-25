package nova.event.events;

import nova.event.Event;

public class PlayerLogOffEvent implements Event {
    String username;

    public PlayerLogOffEvent(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return this.username;
    }
}
