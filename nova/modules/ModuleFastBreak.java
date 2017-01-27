package nova.modules;

import nova.core.EventHandler;
import nova.events.PlayerTickEvent;

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
