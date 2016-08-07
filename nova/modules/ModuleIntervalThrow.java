package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import nova.Command;
import nova.Nova;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/22/2016.
 */
public class ModuleIntervalThrow extends ModuleBase {
    long intervalMs = 90000;
    long currentMs, lastMs;
    int changeToSlot;
    boolean offHand;
    int priorSlot;


    public ModuleIntervalThrow(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);

        this.aliases.add("intt");
        this.name = "IntervalThrow";
        this.command = new Command(Nova, this, aliases, "Throws the item in your hotbar every nth millisecond.");
        this.command.registerArg("int", this.getClass().getMethod("setInt", long.class), "period in milliseconds (ex. 100)");
        this.command.registerArg("slot", this.getClass().getMethod("setSlot", int.class), "hotbar slot to change to before throwing; -1 means no change");
        this.command.registerArg("offhand", this.getClass().getMethod("offHand"), "toggles offhand mode");

        this.defaultArg = "int";

        this.priorSlot = 0;

        this.offHand = false;

        this.changeToSlot = -1;
    }

    public void setInt(long inter){
        this.intervalMs = inter;
    }

    public void setSlot(int slot){
        this.changeToSlot = slot;
    }

    public void offHand(){
        this.offHand = !this.offHand;
    }

    public void onPlayerTick(PlayerTickEvent e)
    {
        this.currentMs = System.nanoTime() / 1000000;

        if(this.isEnabled && (this.currentMs - this.lastMs) >= this.intervalMs)
        {
            this.lastMs = System.nanoTime() / 1000000;

            if(this.offHand){
                mc.thePlayer.connection.netManager.sendPacket(new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
                return;
            }

            if(this.changeToSlot != -1)
            {
                this.priorSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = this.changeToSlot;
            }

            mc.thePlayer.connection.netManager.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));


            if(this.changeToSlot != -1)
            {
                mc.thePlayer.inventory.currentItem = this.priorSlot;
            }
        }

    }

    @Override
    public String getMetadata()
    {
        return "(" + Long.toString(this.intervalMs) + "ms)" + (this.offHand ? "[OffHand]":(this.changeToSlot != -1 ? (" [" + Integer.toString(this.changeToSlot) + "]" ) : ""));
    }
}
