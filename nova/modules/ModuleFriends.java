package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.core.RegisterArgument;

import java.util.ArrayList;

/**
 * Created by Skeleton Man on 7/19/2016.
 */
public class ModuleFriends extends ModuleBase{
    ArrayList<String> friends;


    public ModuleFriends(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.aliases.add("friend");
        this.command = new Command(Nova, this, aliases, "friends list for various purposes");
        this.isEnabled = true;

        this.defaultArg = "add";

        this.friends = new ArrayList<String>();
        this.showEnabled = false;
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

    @RegisterArgument(name = "add", description = "add a friend (eg. friend add Pyrobyte)")
    public void addFriend(String friend){
        if (!this.friends.contains(friend)) {
            this.friends.add(friend);
            Nova.confirmMessage("Added " + friend + " to friends list");

        } else {
            Nova.errorMessage("That friend has already been added");
        }
    }

    @RegisterArgument(name = "del", description = "delete a friend (eg. friend del chrisleighton)")
    public void deleteFriend(String friend){
        if (this.friends.contains(friend)) {
            this.friends.remove(friend);
            Nova.confirmMessage("Removed " + friend + " from friends list");
        } else {
            Nova.errorMessage("That friend doesn't exist");
        }
    }

    public boolean isFriend(String name)
    {
        return this.isEnabled && this.friends.contains(name.toLowerCase());
    }
}
