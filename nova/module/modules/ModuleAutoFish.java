package nova.module.modules;

import net.minecraft.entity.projectile.EntityFishHook;
import nova.event.EventHandler;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;

/**
 * Created by Skeleton Man on 6/24/2016.
 */
public class ModuleAutoFish extends ModuleBase {
    // TODO: more research on tolerance
    // TODO: change the catching flag?

    private boolean catching;
    private double tolerance;


    public ModuleAutoFish() {
        super();
        this.description = ("Automatically catches fish, based off Wurst");
        catching = false;
        tolerance = -0.1D;
    }


    @EventHandler
    public void onPlayerTick(PlayerTickEvent e){
        if (this.isEnabled) {
            if (mc.player.fishEntity != null
                    && isHooked(mc.player.fishEntity)
                    && !catching) {
                catching = true;

                mc.rightClickMouse();

                new Thread("AutoFish") {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        mc.rightClickMouse();

                        catching = false;
                    }
                }.start();
            }
        }
    }

    private boolean isHooked(EntityFishHook hook)
    {
        return hook.motionX == 0.0D && hook.motionZ == 0.0D
                && hook.motionY <= tolerance;
    }
}
