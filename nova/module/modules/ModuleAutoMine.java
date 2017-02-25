package nova.module.modules;

import net.minecraft.block.Block;
import nova.event.EventHandler;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;

/**
 * Created by Skeleton Man on 7/19/2016.
 */
public class ModuleAutoMine extends ModuleBase {

    public ModuleAutoMine() {
        super();

        this.aliases.add("mine");
        this.description = ("Automatically mines");
    }

    @Override
    public void onDisable()
    {
        this.isEnabled = false;
        mc.gameSettings.keyBindAttack.pressed = false;
    }

    @EventHandler
    public void onPlayerTick(PlayerTickEvent e) {
        if (isEnabled){
            if ((mc.objectMouseOver == null)) {
                return;
            }
            mc.gameSettings.keyBindAttack.pressed = Block.getIdFromBlock(mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock()) != 0;
        }
    }
}
