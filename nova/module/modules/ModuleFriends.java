package nova.module.modules;

import nova.Nova;
import nova.event.RegisterArgument;
import nova.module.ModuleBase;
import nova.saver.Saveable;
import nova.util.Util;

import java.util.ArrayList;

/**
 * Created by Skeleton Man on 7/19/2016.
 */
public class ModuleFriends extends ModuleBase {
    @Saveable
    public ArrayList<String> friends;


    public ModuleFriends() {
        super();

        this.aliases.add("friend");
        this.description = ("friends list for various purposes");
        this.isEnabled = true;

        this.defaultArg = "add";

        this.friends = new ArrayList<String>();
        this.showEnabled = false;
    }

    @Override
    public void onEnable()
    {
        this.isEnabled = true;
        Nova.confirmMessage("Friends enabled.");
    }

    @Override
    public void onDisable()
    {
        this.isEnabled = false;
        Nova.notificationMessage("FRIENDS DISABLED. DON'T ACCIDENTALLY KILL ANYONE!");
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

    @RegisterArgument(name = "list", description = "Lists added friends")
    public void listFriends() {
        String list = "Friends: ";

        list += Util.join(friends, ", ");
        /*
        for (String friend : friends) {
            list += friend + ", ";
        }

        if (list.endsWith(", ")) {
            list = list.substring(0, list.length() - 2);
        }
        */

        Nova.message(list);
    }

    public boolean isFriend(String name)
    {
        return this.isEnabled && this.friends.contains(name.toLowerCase());
    }
}
