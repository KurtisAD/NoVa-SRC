package nova.modules;

import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import nova.Command;
import nova.core.Util;

import java.util.ArrayList;

/**
 * Created by Skeleton Man on 7/19/2016.
 */
public class ModuleFriends extends ModuleBase{
    ArrayList<String> friends;


    public ModuleFriends(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);

        this.aliases.add("friend");
        this.command = new Command(Nova, this, aliases, "friends list for various purposes");
        this.command.registerArg("add", this.getClass().getMethod("addFriend", String.class), "add a friend (ex. friend add Pyrobyte)");
        this.command.registerArg("del", this.getClass().getMethod("deleteFriend", String.class), "delete a friend (ex. friend del chrisleighton)");
        this.isEnabled = true;

        this.defaultArg = "add";

        this.friends = new ArrayList<String>();
        this.showEnabled = false;

        loadModule();
    }

    @Override
    public void load(){
        super.load();
        friends = Util.getGson().fromJson(json.get("friends"), new TypeToken<ArrayList<String>>(){}.getType());
    }

    @Override
    public void saveModule(){
        json.add("friends", Util.getGson().toJsonTree(friends));
        super.saveModule();
    }


    @Override
    public void onEnable()
    {
        this.isEnabled = true;
        this.Nova.confirmMessage("Friends enabled.");
    }

    @Override
    public void onDisable()
    {
        this.isEnabled = false;
        this.Nova.notificationMessage("FRIENDS DISABLED. DON'T ACCIDENTALLY KILL ANYONE!");
    }

    public void addFriend(String friend){
        if(!this.friends.contains(friend))
        {
            this.friends.add(friend);
            Nova.confirmMessage("Added " + friend + " to friends list");

        }
        else
        {
            Nova.errorMessage("That friend has already been added");
        }
    }

    public void deleteFriend(String friend){
        if(this.friends.contains(friend))
        {
            this.friends.remove(friend);
            Nova.confirmMessage("Removed " + friend + " from friends list");
        }
        else
        {
            Nova.errorMessage("That friend doesn't exist");
        }
    }

    public boolean isFriend(String name)
    {
        return this.isEnabled && this.friends.contains(name.toLowerCase());
    }
}
