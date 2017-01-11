package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

public class ModuleNofall extends ModuleBase{

    public ModuleNofall(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);
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
