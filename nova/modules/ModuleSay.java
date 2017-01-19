package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;
import nova.Nova;
import nova.core.RegisterArgument;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleSay extends ModuleBase{
    public ModuleSay(Nova Nova, Minecraft mc) {
        super(Nova, mc);
        this.isToggleable = false;

        this.description = ("Says whatever surrounded by quotes. Format: say \"Message\".");
        this.defaultArg = "say";

    }

    @RegisterArgument(name = "say", description = "The say command")
    public void say(String message){
        mc.player.connection.getNetworkManager().sendPacket(new CPacketChatMessage(message));
    }
}
