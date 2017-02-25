package nova.module.modules;

import nova.event.EventHandler;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;

/**
 * Created by Skeleton Man on 1/6/2017.
 */
public class ModuleFastBreak extends ModuleBase {
    public ModuleFastBreak() {
        super();

        this.description = ("Breaks blocks faster");
    }

    @EventHandler
    public void onTick(PlayerTickEvent e) {
        if (this.isEnabled) {
            if (mc.playerController != null) {
                mc.playerController.blockHitDelay = 0;
            }
        }
    }
}
