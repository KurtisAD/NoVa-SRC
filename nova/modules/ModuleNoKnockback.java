package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import nova.core.Util;
import nova.events.EventHandler;
import nova.events.PacketReceivedEvent;

/**
 * Created by Skeleton Man on 12/9/2016.
 */
public class ModuleNoKnockback extends ModuleBase {
    public ModuleNoKnockback(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.aliases.add("nk");
        this.aliases.add("knock");
        this.aliases.add("antiknockback");

        this.description = ("Prevents knockback from swords, bows, explosions, etc.");
    }

    @EventHandler
    public boolean onPacketRecieve(PacketReceivedEvent e) {
        if (this.isEnabled) {
            if (e.getPacket() instanceof SPacketEntityVelocity) {
                if (((SPacketEntityVelocity) e.getPacket()).getEntityID() == mc.player.getEntityId()) {
                    return false;
                }
            } else if (e.getPacket() instanceof SPacketExplosion) {
                Util.setPrivateValue(SPacketExplosion.class, (SPacketExplosion) e.getPacket(), 0f, "motionX");
                Util.setPrivateValue(SPacketExplosion.class, (SPacketExplosion) e.getPacket(), 0f, "motionY");
                Util.setPrivateValue(SPacketExplosion.class, (SPacketExplosion) e.getPacket(), 0f, "motionZ");
            }
        }
        return true;
    }
}
