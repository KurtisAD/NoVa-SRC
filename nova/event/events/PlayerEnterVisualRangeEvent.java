package nova.event.events;

import net.minecraft.entity.player.EntityPlayer;
import nova.event.Event;

/**
 * @author Kurt Dee
 * @since 1/8/2017
 */
public class PlayerEnterVisualRangeEvent implements Event {

    EntityPlayer player;

    public PlayerEnterVisualRangeEvent(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }
}
