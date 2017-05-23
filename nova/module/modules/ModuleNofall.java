package nova.module.modules;

import net.minecraft.network.play.client.CPacketPlayer;
import nova.event.EventHandler;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;

public class ModuleNofall extends ModuleBase {

    public ModuleNofall() {
        super();
        this.description = ("Standard no fall, NCP patched");
    }

    @EventHandler
    public void onTick(PlayerTickEvent e){
        if(isEnabled){
            if (mc.player.fallDistance > 2)
                mc.player.connection.sendPacket(new CPacketPlayer(true));
        }
    }

}
