package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/17/2016.
 */

public class ModuleAntiAfk extends ModuleBase{

    // TODO: implement own anti-afk

    private int timer;
    public ModuleAntiAfk(nova.Nova Nova, Minecraft mc) {
        // TODO: change code to implement yaw module
        super(Nova, mc);
        aliases.add("afk");
        aliases.add("aafk");
        this.command = new Command(Nova, this, aliases, "Walks randomly to prevent AFK detectors. From Wurst");

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
