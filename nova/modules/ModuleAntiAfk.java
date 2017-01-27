package nova.modules;

import nova.core.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/17/2016.
 */

public class ModuleAntiAfk extends ModuleBase{

    // TODO: implement own anti-afk

    private int timer;

    public ModuleAntiAfk() {
        super();
        aliases.add("afk");
        aliases.add("aafk");
        this.description = ("Sneaks on interval to prevent AFK detectors.");

        timer = 0;
    }

    @EventHandler
    public void onUpdate(PlayerTickEvent e)
    {
        if (this.isEnabled) {
            timer++;
            if (timer >= 20)
            {
                mc.gameSettings.keyBindSneak.pressed =
                        !mc.gameSettings.keyBindSneak.pressed;
                timer = 0;
            }
        }
    }
}
