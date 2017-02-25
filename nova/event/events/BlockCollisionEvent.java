package nova.event.events;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import nova.event.Event;

/**
 * @author Kurt Dee
 * @since 1/29/2017
 */
public class BlockCollisionEvent implements Event {
    public AxisAlignedBB aabb;
    public BlockPos pos;

    public BlockCollisionEvent(AxisAlignedBB aabb, BlockPos pos) {
        this.aabb = aabb;
        this.pos = pos;
    }
}
