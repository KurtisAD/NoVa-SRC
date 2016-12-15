package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.Nova;
import nova.core.RegisterArgument;
import nova.core.Saveable;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleGlide extends ModuleBase {
    @Saveable
    double speed;

    public ModuleGlide(Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.speed = -0.17D;

        this.command = new Command(Nova, this, aliases, "Slows fall speed; NCP patched (?)");
        this.defaultArg = "speed";

    }

    @EventHandler
    public void onTick(PlayerTickEvent e)
    {
        if(this.isEnabled) {
            if (!mc.player.onGround && mc.player.motionY < 0.0D && mc.player.isAirBorne && !mc.player.isInWater())
            {
                mc.player.motionY = speed;
            }
        }

    }


    @RegisterArgument(name = "speed", description = "Sets the vertical speed")
    public void setSpeed(double speed){
        this.speed = speed;
    }

    @Override
    public String getMetadata()
    {
        return this.speed == 0.05F ? "" :  "(" + Double.toString(speed) + ")";
    }
}
