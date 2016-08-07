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
    public ModuleSprint(Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Sprints when moving");

        loadModule();
    }

    @EventHandler
    public void onPlayerTick(PlayerTickEvent e){
        if(isEnabled){
            if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking()){
                mc.thePlayer.setSprinting(true);
            }
        }
    }
}
