package nova.events;

/**
 * Created by Skeleton Man on 6/22/2016.
 */
public class ChatSentEvent {
    String message;

    public ChatSentEvent(String message){
        this.message = message;
    }

    public String getMessage(){return this.message;}
}
