package nova.module.modules;

import net.minecraft.network.play.client.CPacketChatMessage;
import nova.event.RegisterArgument;
import nova.module.ModuleBase;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleSay extends ModuleBase {
    public ModuleSay() {
        super();
        this.isToggleable = false;

        this.description = ("Says whatever surrounded by quotes. Format: say \"Message\".");
        this.defaultArg = "say";

    }

    @RegisterArgument(name = "say", description = "The say command")
    public void say(String message){
        mc.player.connection.getNetworkManager().sendPacket(new CPacketChatMessage(message));
    }
}
