package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;
import nova.Command;
import nova.Nova;
import nova.core.RegisterArgument;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleSay extends ModuleBase{
    public ModuleSay(Nova Nova, Minecraft mc) {
        super(Nova, mc);
        this.isToggleable = false;

        this.command = new Command(Nova, this, aliases, "Says whatever surrounded by quotes. Format: say \"Message\".");
        this.defaultArg = "say";

    }

    @RegisterArgument(name = "say", description = "The say command")
    public void say(String message){
        mc.player.connection.netManager.sendPacket(new CPacketChatMessage(message));
    }
}
