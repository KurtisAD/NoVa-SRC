package nova.module.modules;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;

/**
 * Created by Skeleton Man on 7/22/2016.
 */
public class ModuleIntervalThrow extends ModuleBase {
    long intervalMs = 90000;
    long currentMs, lastMs;
    int changeToSlot;
    boolean offHand;
    int priorSlot;


    public ModuleIntervalThrow() {
        super();

        this.aliases.add("intt");
        this.description = ("Throws the item in your hotbar every nth millisecond.");

        this.defaultArg = "int";

        this.priorSlot = 0;

        this.offHand = false;

        this.changeToSlot = -1;
    }

    @RegisterArgument(
            name = "int",
            description = "period in milliseconds (ex. 100)")
    public void setInt(long inter){
        this.intervalMs = inter;
    }

    @RegisterArgument(
            name = "slot",
            description = "hotbar slot to change to before throwing; -1 means no change")
    public void setSlot(int slot){
        this.changeToSlot = slot;
    }

    @RegisterArgument(name = "offhand", description = "toggles offhand mode")
    public void offHand(){
        this.offHand = !this.offHand;
    }

    @EventHandler
    public void onPlayerTick(PlayerTickEvent e) {
        this.currentMs = System.nanoTime() / 1000000;

        if(this.isEnabled && (this.currentMs - this.lastMs) >= this.intervalMs) {
            this.lastMs = System.nanoTime() / 1000000;

            if(this.offHand){
                mc.player.connection.getNetworkManager().sendPacket(new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
                return;
            }

            if(this.changeToSlot != -1) {
                this.priorSlot = mc.player.inventory.currentItem;
                mc.player.inventory.currentItem = this.changeToSlot;
            }

            mc.player.connection.getNetworkManager().sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));


            if(this.changeToSlot != -1) {
                mc.player.inventory.currentItem = this.priorSlot;
            }
        }

    }

    @Override
    public String getMetadata()
    {
        return "(" + Long.toString(this.intervalMs) + "ms)" + (this.offHand ? "[OffHand]":(this.changeToSlot != -1 ? (" [" + Integer.toString(this.changeToSlot) + "]" ) : ""));
    }
}
