package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 12/9/2016.
 */

// TODO: make this shit work
public class ModuleJesus extends ModuleBase {
    public ModuleJesus(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Walks on water");
    }

    @EventHandler
    public void onTick(PlayerTickEvent e) {
        if (this.isEnabled) {
            if (mc.player.isInWater()) {
                mc.player.motionY += 0.1;
            }
        }
    }
}
