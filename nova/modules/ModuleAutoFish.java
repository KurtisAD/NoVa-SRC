package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntityFishHook;
import nova.Nova;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;
import nova.Command;

/**
 * Created by Skeleton Man on 6/24/2016.
 */
public class ModuleAutoFish extends ModuleBase {
    private boolean catching;

    public ModuleAutoFish(Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);
        this.command = new Command(Nova, this, aliases, "Automatically catches fish");
        catching = false;

        loadModule();
    }


    @EventHandler
    public void onPlayerTick(PlayerTickEvent e){
        if (this.isEnabled) {
            if (mc.thePlayer.fishEntity != null
                    && isHooked(mc.thePlayer.fishEntity)
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
                && hook.motionY != 0.0D;
    }
}
