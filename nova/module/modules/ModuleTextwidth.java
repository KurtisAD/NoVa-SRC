package nova.module.modules;

import net.minecraft.network.play.client.CPacketChatMessage;
import nova.event.EventHandler;
import nova.event.events.ChatSentEvent;
import nova.module.ModuleBase;
import nova.util.Util;

/**
 * Created by Skeleton Man on 7/19/2016.
 */
public class ModuleTextwidth extends ModuleBase {

    public ModuleTextwidth() {
        super();

        aliases.add("width");
        this.description = ("Converts chats to full-width unicode");
    }


    @EventHandler
    public boolean onPlayerChat(ChatSentEvent e)
    {

        if(this.isEnabled)
        {
            String message = "";
            if(e.getMessage().startsWith("%") ||
                    e.getMessage().startsWith(Util.hash(mc.player.getDisplayName().getUnformattedText()))) {
                return true;
            }
            else if(e.getMessage().startsWith(">"))
            {
                message = ">" + Util.toFull(e.getMessage().substring(1));
            }
            else if(e.getMessage().startsWith("/r "))
            {
                message = "/r " + Util.toFull(e.getMessage().substring(3));
            }
            else if(e.getMessage().startsWith("/p "))
            {
                message = "/p " + Util.toFull(e.getMessage().substring(3));
            }
            else if(e.getMessage().startsWith("/w "))
            {
                message = e.getMessage().substring(3);
                int split = message.indexOf(" ");
                String user = message.substring(0, split);
                message = "/w " + user + " " + Util.toFull(message.substring(split));
            }
            else
                message = Util.toFull(e.getMessage());

            mc.player.connection.getNetworkManager().sendPacket(new CPacketChatMessage(message));
            return false;
        }
        else
            return true;
    }
}
