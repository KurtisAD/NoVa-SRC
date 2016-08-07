package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import nova.events.EventHandler;
import nova.Command;
import nova.events.PlayerTickEvent;

public class ModuleNofall extends ModuleBase{

    public ModuleNofall(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);
        this.command = new Command(Nova, this, aliases, "Standard no fall, NCP patched");

        loadModule();
    }

    @EventHandler
    public void onTick(PlayerTickEvent e){
        if(isEnabled){
            if(mc.thePlayer.fallDistance > 2)
                mc.thePlayer.connection.sendPacket(new CPacketPlayer(true));
        }
    }
}
