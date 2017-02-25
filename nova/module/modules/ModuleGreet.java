package nova.module.modules;

import net.minecraft.client.Minecraft;
import nova.Nova;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PlayerLogOffEvent;
import nova.event.events.PlayerLogOnEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;
import nova.util.Util;

import java.util.*;

/**
 * Created by Skeleton Man on 7/21/2016.
 */

// TODO: pretty much rewrite this entire class
public class ModuleGreet extends ModuleBase {

    Random rand;
    String defaultGreetings[] = {"Hey", "Welcome", "Hello", "Hi", "Greetings", "Salutations", "Good to see you", "{time}"};
    String defaultGoodbyes[] = {"Later", "So long", "See you later", "Good bye", "Bye", "Farewell"};


    @Saveable
    public boolean onJoin;

    @Saveable
    public boolean onLeave;

    @Saveable
    public ArrayList<String> greetings;

    @Saveable
    public ArrayList<String> goodbyes;

    @Saveable
    public String greetingFormat;

    @Saveable
    public String goodbyeFormat;

    private long lastMessageTime;


    public ModuleGreet() {
        super();

        this.description = ("Greets");

        this.lastMessageTime = Minecraft.getSystemTime();

        this.onJoin = true;
        this.onLeave = true;
        this.greetings = new ArrayList<String>(Arrays.asList(this.defaultGreetings));
        this.goodbyes = new ArrayList<String>(Arrays.asList(this.defaultGoodbyes));
        this.goodbyeFormat = "> ... {msg}, {player}{.}";
        this.greetingFormat = "> !!! {msg}, {player}{.}";

        rand = new Random();


    }


    @RegisterArgument(name = "join", description = "Welcome")
    public void join(){
        this.onJoin = !onJoin;
    }

    @RegisterArgument(name = "leave", description = "Goodbye")
    public void leave(){
        this.onLeave = !onLeave;
    }

    @RegisterArgument(name = "greetadd", description = "Add a welcome")
    public void greetadd(String greet){
        this.greetings.add(greet);
        Nova.confirmMessage("Added greeting");
    }

    @RegisterArgument(name = "greetdel", description = "Delete a welcome")
    public void greetdel(String greet){
        if(this.greetings.contains(greet)) {
            this.greetings.remove(greet);
            Nova.confirmMessage("Removed greeting");
        } else {
            Nova.errorMessage("Could not remove greeting");
        }
    }

    @RegisterArgument(name = "byeadd", description = "Add a good bye")
    public void byeadd(String bye){
        this.goodbyes.add(bye);
        Nova.confirmMessage("Added goodbye");
    }

    @RegisterArgument(name = "byedel", description = "Delete a good bye")
    public void byedel(String bye){
        if(this.goodbyes.contains(bye)) {
            this.goodbyes.remove(bye);
            Nova.confirmMessage("Removed goodbye");
        } else {
            Nova.errorMessage("Could not remove goodbye");
        }
    }

    @RegisterArgument(name = "greetlist", description = "Lists greetings")
    public void greetlist(){
        Nova.message(Util.join(this.greetings, ", "));
    }

    @RegisterArgument(name = "byelist", description = "Lists goodbyes")
    public void byelist(){
        Nova.message(Util.join(this.goodbyes, ", "));
    }

    @RegisterArgument(name = "byeformat", description = "Change goodbye format; {player} {msg} {.}")
    public void byeformat(String format){
        Nova.confirmMessage("Changed goodbye format");
        this.goodbyeFormat = format;
    }

    @RegisterArgument(name = "greetformat", description = "Change greeting format; {player} {msg} {.}")
    public void greetformat(String format){
        Nova.confirmMessage("Changed greeting format");
        this.greetingFormat = format;
    }

    @EventHandler
    public void onPlayerLogIn(PlayerLogOnEvent e)
    {
        this.greeting(e.getUsername());
    }

    @EventHandler
    public void onPlayerLogOff(PlayerLogOffEvent e)
    {
        this.farewell(e.getUsername());
    }

    private void greeting(String user)
    {
        if (Minecraft.getSystemTime() - lastMessageTime < 100 || !(this.isEnabled && this.onJoin)) {
            return;
        }

        int i = rand.nextInt(greetings.size());
        String period = rand.nextInt(2) > 1 ? "." : "!";
        String greet = greetings.get(i);

        if(greet.equals("{time}"))
        {
            Calendar calendar = GregorianCalendar.getInstance();
            int t = calendar.get(Calendar.HOUR_OF_DAY);

            if(t < 11 && t > 2)
            {
                greet = "Morning";
            }
            else if(t > 11 && t < 18)
            {
                greet = "Afternoon";
            }
            else
            {
                greet = "Evening";
            }
        }

        String msg = this.greetingFormat.replaceAll("\\{player\\}", user)
                .replaceAll("\\{msg\\}", greet)
                .replaceAll("\\{.\\}", period);

        mc.player.sendChatMessage(msg);
        lastMessageTime = Minecraft.getSystemTime();
    }

    private void farewell(String user)
    {
        if (Minecraft.getSystemTime() - lastMessageTime < 100 || !(this.isEnabled && this.onLeave)) {
            return;
        }

        int i = rand.nextInt(goodbyes.size());
        String period = rand.nextInt(2) > 1 ? "." : "!";

        String msg = this.goodbyeFormat.replaceAll("\\{player\\}", user)
                .replaceAll("\\{msg\\}", goodbyes.get(i))
                .replaceAll("\\{.\\}", period);

        mc.player.sendChatMessage(msg);
        lastMessageTime = Minecraft.getSystemTime();
    }


}
