package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.core.RegisterArgument;
import nova.core.Saveable;
import nova.core.Util;
import nova.events.EventHandler;
import nova.events.PlayerLogOffEvent;
import nova.events.PlayerLogOnEvent;

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
    public ArrayList<String> ignoredPlayers;


    @Saveable
    public ArrayList<String> greetings;

    @Saveable
    public ArrayList<String> goodbyes;

    @Saveable
    public String greetingFormat;

    @Saveable
    public String goodbyeFormat;



    public ModuleGreet(nova.Nova Nova, Minecraft mc)  {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Greets");

        this.ignoredPlayers = new ArrayList<String>();
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

    @RegisterArgument(name = "ignore", description = "Adds a player to the ignore list")
    public void ignore(String name){
        String player = name.toLowerCase();
        if (!this.ignoredPlayers.contains(name)){
            ignoredPlayers.add(name);
            this.Nova.confirmMessage("Ignored " + name);
        } else {
            this.Nova.errorMessage("As much as you hate " + name + ", you can only ignore him once");
        }
    }

    @RegisterArgument(name = "unignore", description = "Removes a player from the ignore list")
    public void unignore(String name){
        String player = name.toLowerCase();
        if(this.ignoredPlayers.contains(player))
        {
            this.ignoredPlayers.remove(player);
            this.Nova.confirmMessage("Unignored " + player);
        }
        else
            this.Nova.errorMessage("You never ignored " + name);
    }

    @RegisterArgument(name = "ignored", description = "Lists ignored players")
    public void ignored(String name){
        if(this.ignoredPlayers.isEmpty())
        {
            this.Nova.errorMessage("Nobody ignored");
            return;
        }

        String ret = "";
        for(String s : this.ignoredPlayers)
        {
            ret += s + ", ";
        }

        ret = ret.substring(0, ret.length() - 2);
        this.Nova.confirmMessage(ret);
    }

    @RegisterArgument(name = "unignoreall", description = "Removes all ignored players")
    public void unignoreall(){
        this.ignoredPlayers.clear();
        this.Nova.confirmMessage("Deleted all players from ignore list");
    }

    @RegisterArgument(name = "greetadd", description = "Add a welcome")
    public void greetadd(String greet){
        this.greetings.add(greet);
        this.Nova.confirmMessage("Added greeting");
    }

    @RegisterArgument(name = "greetdel", description = "Delete a welcome")
    public void greetdel(String greet){
        if(this.greetings.contains(greet)) {
            this.greetings.remove(greet);
            this.Nova.confirmMessage("Removed greeting");
        } else {
            this.Nova.errorMessage("Could not remove greeting");
        }
    }

    @RegisterArgument(name = "byeadd", description = "Add a good bye")
    public void byeadd(String bye){
        this.goodbyes.add(bye);
        this.Nova.confirmMessage("Added goodbye");
    }

    @RegisterArgument(name = "byedel", description = "Delete a good bye")
    public void byedel(String bye){
        if(this.goodbyes.contains(bye)) {
            this.goodbyes.remove(bye);
            this.Nova.confirmMessage("Removed goodbye");
        } else {
            this.Nova.errorMessage("Could not remove goodbye");
        }
    }

    @RegisterArgument(name = "greetlist", description = "Lists greetings")
    public void greetlist(){
        this.Nova.message(Util.join(this.greetings, ", "));
    }

    @RegisterArgument(name = "byelist", description = "Lists goodbyes")
    public void byelist(){
        this.Nova.message(Util.join(this.goodbyes, ", "));
    }

    @RegisterArgument(name = "byeformat", description = "Change goodbye format; {player} {msg} {.}")
    public void byeformat(String format){
        this.Nova.confirmMessage("Changed goodbye format");
        this.goodbyeFormat = format;
    }

    @RegisterArgument(name = "greetformat", description = "Change greeting format; {player} {msg} {.}")
    public void greetformat(String format){
        this.Nova.confirmMessage("Changed greeting format");
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

    public void greeting(String user)
    {
        if(!(this.isEnabled && this.onJoin && !this.ignoredPlayers.contains(user.toLowerCase())))
            return;

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
    }

    public void farewell(String user)
    {

        if(!(this.isEnabled && this.onLeave && !this.ignoredPlayers.contains(user.toLowerCase())))
            return;

        int i = rand.nextInt(goodbyes.size());
        String period = rand.nextInt(2) > 1 ? "." : "!";

        String msg = this.goodbyeFormat.replaceAll("\\{player\\}", user)
                .replaceAll("\\{msg\\}", goodbyes.get(i))
                .replaceAll("\\{.\\}", period);

        mc.player.sendChatMessage(msg);
    }


}
