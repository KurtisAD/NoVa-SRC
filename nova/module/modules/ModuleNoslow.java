package nova.module.modules;

import net.minecraft.init.Blocks;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;

/**
 * Created by Skeleton Man on 12/20/2016.
 */
public class ModuleNoslow extends ModuleBase {

    @Saveable
    public double ladderSpeed = 2.0D;
    @Saveable
    public float iceSlipperiness = 0.98F;
    @Saveable
    public boolean useItemSlowdown; // Hook in EntityPlayerSP onLivingUpdate()


    public ModuleNoslow() {
        super();

        aliases.add("ns");
        this.description = ("Makes you go fast");
    }


    @RegisterArgument(name = "ladder", description = "Changes your upward speed on ladders")
    public void ladderSpeed(double speed) {
        this.ladderSpeed = speed;
    }

    @RegisterArgument(name = "slip", description = "Changes the slipperiness of ice")
    public void setIceSlipperiness(float slip) {
        this.iceSlipperiness = slip;
    }

    @RegisterArgument(name = "items", description = "Toggles slowdown when using items")
    public void items() {
        this.useItemSlowdown = !useItemSlowdown;
    }

    @EventHandler
    public void onTick(PlayerTickEvent e) {
        if (this.isEnabled) {
            fastLadder();
            fastIce();
        }
    }

    private void fastLadder() {
        if (mc.player.isOnLadder()) {
            if (mc.gameSettings.keyBindForward.pressed) {
                if (!mc.player.capabilities.isFlying) {
                    mc.player.motionY = 0.1D * ladderSpeed;
                }
            }
        }
    }

    private void fastIce() {
        Blocks.ICE.slipperiness = iceSlipperiness;
        Blocks.PACKED_ICE.slipperiness = iceSlipperiness;
        Blocks.FROSTED_ICE.slipperiness = iceSlipperiness;
    }

    private void normalIce() {
        Blocks.ICE.slipperiness = 0.98F;
        Blocks.PACKED_ICE.slipperiness = 0.98F;
        Blocks.FROSTED_ICE.slipperiness = 0.98F;

    }

    @Override
    public void onDisable() {
        super.onDisable();
        normalIce();
    }

    @Override
    public String getMetadata() {
        return "(Ladder: " + ladderSpeed + ", Ice: " + iceSlipperiness + (useItemSlowdown ? "| Items" : "") + ")";
    }

}
