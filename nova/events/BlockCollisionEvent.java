package nova.events;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Skeleton Man on 1/29/2017.
 */
public class BlockCollisionEvent {
    public AxisAlignedBB aabb;
    public BlockPos pos;

    public BlockCollisionEvent(AxisAlignedBB aabb, BlockPos pos) {
        this.aabb = aabb;
        this.pos = pos;
    }
}
