package nova.module.modules;

import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleGlide extends ModuleBase {
    @Saveable
    double speed;

    public ModuleGlide() {
        super();

        this.speed = -0.17D;

        this.description = ("Slows fall speed; NCP patched (?)");
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
