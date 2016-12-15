package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;
import nova.Command;
import nova.core.RegisterArgument;
import nova.core.Util;

/**
 * Created by Skeleton Man on 7/22/2016.
 */
public class ModuleImpersonate extends ModuleBase {
    // Pretty sure this is fully broken, needs a new workaround


    public ModuleImpersonate(nova.Nova Nova, Minecraft mc) {
        super(Nova,mc);

        this.isToggleable = false;
        this.aliases.add("imp");

        this.command = new Command(Nova, this, aliases, "Impersonates a player");
        this.defaultArg = "do";
    }

    @RegisterArgument(name = "do", description = "Takes in [name] and \\\"[message]\\\"")
    public void imp(String user, String message){
        String username = user;
        String msg = message;

        String p = "";

        p += Util.toFull("(((((((mmmmmmmmmmmmmmmmmmmmmmmmmmmm");
        p += "<" + username + "> " + msg;

        mc.player.connection.netManager.sendPacket(new CPacketChatMessage(p));

    }
}
