package nova.modules;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import nova.Command;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/19/2016.
 */
public class ModuleAutoMine extends ModuleBase {

    public ModuleAutoMine(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.aliases.add("mine");
        this.command = new Command(Nova, this, aliases, "Automatically mines");
    }

    @Override
    public void onDisable()
    {
        this.isEnabled = false;
        mc.gameSettings.keyBindAttack.pressed = false;
    }

    public void onPlayerTick(PlayerTickEvent e)
    {
        if (isEnabled){
            if ((mc.objectMouseOver == null) || (mc.objectMouseOver.getBlockPos() == null)) {
                return;
            }
            if (Block.getIdFromBlock(mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock()) != 0) {
                mc.gameSettings.keyBindAttack.pressed = true;
            } else {
                mc.gameSettings.keyBindAttack.pressed = false;
            }
        }
    }
}
