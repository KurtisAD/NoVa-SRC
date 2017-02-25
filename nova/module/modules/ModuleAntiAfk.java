package nova.module.modules;

import net.minecraft.client.settings.KeyBinding;
import nova.event.EventHandler;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;

/**
 * Created by Skeleton Man on 7/17/2016.
 */

public class ModuleAntiAfk extends ModuleBase {
    // TODO: implement using packets

    private int timer;
    private int tickCounter;

    public ModuleAntiAfk() {
        super();
        aliases.add("afk");
        aliases.add("aafk");
        this.description = ("Sneaks on interval to prevent AFK detectors.");

        timer = 0;
        tickCounter = 0;
    }

    @EventHandler
    public void onUpdate(PlayerTickEvent e) {
        if (this.isEnabled && !mc.player.isCreative() && !mc.player.isSpectator()) {
            tickCounter++;

            if (mc.mouseHelper.deltaX != 0 || mc.mouseHelper.deltaY != 0) {
                tickCounter = 0;
            }

            for (KeyBinding kb : mc.gameSettings.keyBindings) {
                if (kb.equals(mc.gameSettings.keyBindSneak)) {
                    continue;
                }
                if (kb.pressed) {
                    tickCounter = 0;
                    return;
                }
            }

            if (tickCounter >= 1200) {
                timer++;
                if (timer >= 20) {
                    mc.gameSettings.keyBindSneak.pressed =
                            !mc.gameSettings.keyBindSneak.pressed;
                    timer = 0;
                }
            }
        }
    }

    @Override
    public String getMetadata() {
        return tickCounter >= 1200 ? "(Active)" : "(Ready: " + (60 - tickCounter / 20) + ")";
    }
}
