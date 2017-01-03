package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import nova.Command;
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

        this.command = new Command(Nova, this, aliases, "Prevents knockback from swords, bows, etc.");
    }

    @EventHandler
    public boolean onPacketRecieve(PacketReceivedEvent e) {
        if (this.isEnabled) {
            if (e.getPacket() instanceof SPacketEntityVelocity) {
                if (((SPacketEntityVelocity) e.getPacket()).getEntityID() == mc.player.getEntityId()) {
                    return false;
                }
            }
        }
        return true;
    }
}
