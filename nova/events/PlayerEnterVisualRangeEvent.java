package nova.events;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Skeleton Man on 1/8/2017.
 */
public class PlayerEnterVisualRangeEvent {

    EntityPlayer player;

    public PlayerEnterVisualRangeEvent(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }
}
