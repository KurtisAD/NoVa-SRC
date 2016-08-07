package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;
import nova.Command;
import nova.Nova;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleSay extends ModuleBase{
    public ModuleSay(Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);
        this.isToggleable = false;

        this.command = new Command(Nova, this, aliases, "Says whatever surrounded by quotes. Format: say \"Message\".");
        this.command.registerArg("say", this.getClass().getMethod("say", String.class), "The say command");
        this.defaultArg = "say";

        loadModule();
    }

    public void say(String message){
        mc.thePlayer.connection.netManager.sendPacket(new CPacketChatMessage(message));
    }
}
