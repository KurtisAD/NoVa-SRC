package nova.event.events;

import net.minecraft.entity.player.EntityPlayer;
import nova.event.Event;

/**
 * @author Kurt Dee
 * @since 1/8/2017
 */
public class PlayerLeaveVisualRangeEvent implements Event {

    EntityPlayer player;

    public PlayerLeaveVisualRangeEvent(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }
}
