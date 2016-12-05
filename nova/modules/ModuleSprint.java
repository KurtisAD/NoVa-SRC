package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.Nova;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleSprint extends ModuleBase{
    public ModuleSprint(Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Sprints when moving");
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
