package nova.events;

/**
 * Created by Skeleton Man on 6/22/2016.
 */
public class ChatRecievedEvent {
    String message;

    public ChatRecievedEvent(String message){
        this.message = message;
    }

    public String getMessage(){return this.message;}
}
