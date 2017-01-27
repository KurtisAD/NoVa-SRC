package nova.modules;

import nova.core.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/17/2016.
 */
public class ModuleAutoRespawn extends ModuleBase{

    public ModuleAutoRespawn() {
        super();
        aliases.add("respawn");
        this.description = ("Automatically respawn on death.");

    }

    @EventHandler
    public void onPlayerTick(PlayerTickEvent e){
        if (this.isEnabled && mc.player.getHealth() <= 0)
            mc.player.respawnPlayer();
    }
}
