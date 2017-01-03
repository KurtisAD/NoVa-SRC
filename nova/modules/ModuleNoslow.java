package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import nova.Command;
import nova.core.RegisterArgument;
import nova.core.Saveable;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 12/20/2016.
 */
public class ModuleNoslow extends ModuleBase {

    @Saveable
    public double ladderSpeed = 2.0D;
    @Saveable
    public float iceSlipperiness = 0.98F;


    public ModuleNoslow(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        aliases.add("ns");
        this.command = new Command(Nova, this, aliases, "Makes you go fast");
    }


    @RegisterArgument(name = "ladder", description = "Changes your upward speed on ladders")
    public void ladderSpeed(double speed) {
        this.ladderSpeed = speed;
    }

    @RegisterArgument(name = "slip", description = "Changes the slipperiness of ice")
    public void setIceSlipperiness(float slip) {
        this.iceSlipperiness = slip;
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
        return "( Ladders: " + ladderSpeed + ", Ice: " + iceSlipperiness + ")";
    }

}
