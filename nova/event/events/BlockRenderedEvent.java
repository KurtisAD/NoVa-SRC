package nova.event.events;

import net.minecraft.util.math.BlockPos;
import nova.core.Location;
import nova.event.Event;

/**
 * @author Kurt Dee
 * @since 7/26/2016
 */
public class BlockRenderedEvent implements Event {
    public int id;
    public Location pos;

    public BlockRenderedEvent(int id, BlockPos pos) {
        this.id = id;
        this.pos = new Location(pos);
    }
}
