package nova.events;

import net.minecraft.util.math.BlockPos;
import nova.core.Location;
import nova.core.SimpleBlock;
import optfine.BlockPosM;

/**
 * Created by Skeleton Man on 7/26/2016.
 */
public class BlockRenderedEvent {
    public SimpleBlock block;
    public Location pos;

    public BlockRenderedEvent(SimpleBlock block, BlockPos pos){
        this.block = block;
        this.pos = new Location(pos);
    }}
