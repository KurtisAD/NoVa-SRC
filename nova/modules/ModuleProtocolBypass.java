package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.handshake.client.C00Handshake;
import nova.Command;
import nova.core.Util;
import nova.events.EventHandler;
import nova.events.PacketSendEvent;

/**
 * Created by Skeleton Man on 12/23/2016.
 */
public class ModuleProtocolBypass extends ModuleBase {
    public ModuleProtocolBypass(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);
        this.isToggleable = false;

        this.command = new Command(Nova, this, aliases, "Sets the protocol version to 1.11.2");
    }

    @EventHandler
    public boolean onPacketSend(PacketSendEvent e) {
        if (e.getPacket() instanceof C00Handshake) {
            C00Handshake packet = (C00Handshake) e.getPacket();
            Util.setPrivateValue(C00Handshake.class, packet, 316, "protocolVersion");
            mc.getConnection().netManager.sendPacket(packet);
            return false;
        }
        return true;
    }

}
