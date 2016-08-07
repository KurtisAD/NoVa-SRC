package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/17/2016.
 */
public class ModuleAutoRespawn extends ModuleBase{

    public ModuleAutoRespawn(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);
        aliases.add("respawn");
        this.command = new Command(Nova, this, aliases, "Automatically respawns on death.");

    }

    @EventHandler
    public void onPlayerTick(PlayerTickEvent e){
        if(this.isEnabled && mc.thePlayer.getHealth() <= 0)
            mc.thePlayer.respawnPlayer();

    }
}
