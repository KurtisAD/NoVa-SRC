package nova.events;

import net.minecraft.util.math.BlockPos;
import nova.core.Location;

/**
 * Created by Skeleton Man on 7/26/2016.
 */
public class BlockRenderedEvent {
    public int id;
    public Location pos;

    public BlockRenderedEvent(int id, BlockPos pos) {
        this.id = id;
        this.pos = new Location(pos);
    }
}
