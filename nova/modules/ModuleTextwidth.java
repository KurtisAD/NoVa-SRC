package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;
import nova.Command;
import nova.core.Util;
import nova.events.ChatSentEvent;
import nova.events.EventHandler;

/**
 * Created by Skeleton Man on 7/19/2016.
 */
public class ModuleTextwidth extends ModuleBase{

    public ModuleTextwidth(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);

        aliases.add("width");
        this.name = "TextWidth";
        this.command = new Command(Nova, this, aliases, "Converts chats to full-width unicode");

        loadModule();
    }


    @EventHandler
    public boolean onPlayerChat(ChatSentEvent e)
    {

        if(this.isEnabled)
        {
            String message = "";
            if(e.getMessage().startsWith("%") ||
                    e.getMessage().startsWith(Util.hash(mc.thePlayer.getDisplayName().getUnformattedText()))){
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

            mc.thePlayer.connection.netManager.sendPacket(new CPacketChatMessage(message));
            return false;
        }
        else
            return true;
    }
}
