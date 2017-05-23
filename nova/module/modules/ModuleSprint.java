package nova.module.modules;

import net.minecraft.network.play.client.CPacketEntityAction;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PacketSendEvent;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleSprint extends ModuleBase {
    @Saveable
    boolean antiHunger;

    public ModuleSprint() {
        super();

        this.description = ("Sprints when moving");
        this.antiHunger = false;
    }

    @RegisterArgument(name = "antihunger", description = "Makes sprinting take no hunger")
    public void toggleAntiHunger() {
        this.antiHunger = !antiHunger;
    }

    @EventHandler
    public boolean onPacketSend(PacketSendEvent e) {
        if (antiHunger && e.getPacket() instanceof CPacketEntityAction) {
            CPacketEntityAction p = (CPacketEntityAction) e.getPacket();

            if (p.getAction() == CPacketEntityAction.Action.START_SPRINTING) {
                return false;
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerTick(PlayerTickEvent e){
        if(isEnabled){
            if (mc.player.moveForward > 0 && !mc.player.isSneaking()) {
                mc.player.setSprinting(true);
            }
        }
    }

    @Override
    public String getMetadata() {
        return antiHunger ? "(AntiHunger)" : "";
    }
}
