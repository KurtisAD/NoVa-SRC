package nova.modules;

import nova.core.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleSprint extends ModuleBase{
    public ModuleSprint() {
        super();

        this.description = ("Sprints when moving");
    }

    @EventHandler
    public void onPlayerTick(PlayerTickEvent e){
        if(isEnabled){
            if (mc.player.moveForward > 0 && !mc.player.isSneaking()) {
                mc.player.setSprinting(true);
            }
        }
    }
}
