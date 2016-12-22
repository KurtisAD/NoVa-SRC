package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;

/**
 * Created by Skeleton Man on 12/20/2016.
 */
public class ModuleNoslow extends ModuleBase {

    public double ladderSpeed = 2.0D;
    public float iceSlipperiness = 0.35F;
    public boolean fastLadder = false;


    public ModuleNoslow(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        aliases.add("ns");
        this.command = new Command(Nova, this, aliases, "Makes you go fast");
    }

    private void fastLadder() {
        if (mc.player.isOnLadder()) {
            if (mc.gameSettings.keyBindForward.pressed) {
                if (!mc.player.capabilities.isFlying) {
                    mc.player.motionY = 0.4F;
                    mc.player.motionY = 0.1D * ladderSpeed;
                } else {
                    mc.player.motionZ = 0.15D * ladderSpeed;
                }
            }
        }
    }

}
