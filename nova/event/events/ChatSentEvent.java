package nova.event.events;

import nova.event.Event;

/**
 * @author Kurt Dee
 * @since 6/22/2016
 */

// TODO: implement packet send event instead of chat send event, chat send triggers packet send afterwards
// Change Events class and Modules to use instance of packet send

@Deprecated
public class ChatSentEvent implements Event {
    String message;

    public ChatSentEvent(String message){
        this.message = message;
    }

    public String getMessage(){return this.message;}
}
