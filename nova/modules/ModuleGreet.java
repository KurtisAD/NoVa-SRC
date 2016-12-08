package nova.modules;

/**
 * Created by Skeleton Man on 7/21/2016.
 */

// TODO: pretty much rewrite this entire class
public class ModuleGreet /*extends ModuleBase*/ {
    /*
    Random rand;

    boolean onJoin;
    boolean onLeave;
    ArrayList<String> ignoredPlayers;

    String defaultGreetings[] = {"Hey", "Welcome", "Hello", "Hi", "Greetings", "Salutations", "Good to see you", "{time}"};
    String defaultGoodbyes[] = {"Later", "So long", "See you later", "Good bye", "Bye", "Farewell"};

    ArrayList<String> greetings;
    ArrayList<String> goodbyes;

    String greetingFormat;
    String goodbyeFormat;



    public ModuleGreet(nova.Nova Nova, Minecraft mc)  {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Greets");
        this.command.registerArg("unignoreall", this.getClass().getMethod("unignoreall"), "Removes all ignored players");
        this.command.registerArg("greetadd", this.getClass().getMethod("greetadd", String.class), "Add a welcome");
        this.command.registerArg("greetdel", this.getClass().getMethod("greetdel", String.class), "Delete a welcome");
        this.command.registerArg("byeadd", this.getClass().getMethod("byeadd", String.class), "Add a good bye");
        this.command.registerArg("byedel", this.getClass().getMethod("byedel",String.class), "Delete a good bye");
        this.command.registerArg("greetlist", this.getClass().getMethod("greetlist"), "Lists greetings");
        this.command.registerArg("byelist", this.getClass().getMethod("byelist"), "Lists goodbyes");
        this.command.registerArg("byeformat", this.getClass().getMethod("byeformat", String.class), "Change greeting format; {player} {msg} {.}");
        this.command.registerArg("greetformat", this.getClass().getMethod("greetformat", String.class), "Change goodbye format; {player} {msg} {.}");

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

    public void unignoreall(){
        this.ignoredPlayers.clear();
        this.Nova.confirmMessage("Deleted all players from ignore list");
    }

    public void greetadd(String greet){
        this.greetings.add(greet);
        this.Nova.confirmMessage("Added greeting");
    }

    public void greetdel(String greet){
        if(this.greetings.contains(greet)) {
            this.greetings.remove(greet);
            this.Nova.confirmMessage("Removed greeting");
        } else {
            this.Nova.errorMessage("Could not remove greeting");
        }
    }

    public void byeadd(String bye){
        this.goodbyes.add(bye);
        this.Nova.confirmMessage("Added goodbye");
    }

    public void byedel(String bye){
        if(this.goodbyes.contains(bye)) {
            this.goodbyes.remove(bye);
            this.Nova.confirmMessage("Removed goodbye");
        } else {
            this.Nova.errorMessage("Could not remove goodbye");
        }
    }

    public void greetlist(){
        this.Nova.message(Util.join(this.greetings, ", "));
    }
    public void byelist(){
        this.Nova.message(Util.join(this.goodbyes, ", "));
    }
    public void byeformat(String format){
        this.Nova.confirmMessage("Changed goodbye format");
        this.goodbyeFormat = format;
    }
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

*/
}
