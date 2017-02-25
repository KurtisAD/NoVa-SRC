package nova.event.events;

import nova.event.Event;

/**
 * @author Kurt Dee
 * @since 6/22/2016
 */
public class ChatRecievedEvent implements Event {
    String message;

    public ChatRecievedEvent(String message){
        this.message = message;
    }

    public String getMessage(){return this.message;}
}
