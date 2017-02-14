package nova.modules;

import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.AxisAlignedBB;
import nova.core.EventHandler;
import nova.events.BlockCollisionEvent;
import nova.events.LivingUpdateEvent;

/**
 * Created by Skeleton Man on 12/9/2016.
 */

public class ModuleJesus extends ModuleBase {
    public ModuleJesus() {
        super();

        this.description = ("Walks on water");
    }

    @EventHandler
    public void onBlockCollision(BlockCollisionEvent e) {
        if (this.isEnabled &&
                mc.world.getBlockState(e.pos).getBlock() instanceof BlockLiquid &&
                !(mc.player.isInWater() || mc.player.isSneaking() || mc.player.fallDistance > 3)) {

            e.aabb = new AxisAlignedBB(0, 0, 0, 1, 0.99, 1);

        }
    }

    @EventHandler
    public void onLivingUpdate(LivingUpdateEvent e) {

    }
}
