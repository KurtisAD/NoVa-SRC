package nova.modules;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import nova.Command;
import nova.events.EventHandler;
import nova.events.LeftClickEvent;
import nova.events.PlayerTickEvent;


public class ModuleAutotool extends ModuleBase{
	private int oldSlot;
	private boolean isActive = false;


    public ModuleAutotool(nova.Nova Nova, Minecraft mc) {

		super(Nova, mc);
		this.command = new Command(Nova, this, aliases, "Switches to better tool if possible");
	}

    // This code need revision

	@EventHandler
	public void onTick(PlayerTickEvent e) {
		if (this.isEnabled){
			if ((!mc.gameSettings.keyBindAttack.pressed) && (this.isActive))
			{
				this.isActive = false;
                mc.player.inventory.currentItem = this.oldSlot;
            } else if ((this.isActive) &&
					(mc.objectMouseOver != null) &&
					(mc.objectMouseOver.getBlockPos() != null) &&
                    (mc.world.getBlockState(mc.objectMouseOver.getBlockPos())
                            .getBlock().getMaterial(null) != Material.AIR)) {
				setSlot(mc.objectMouseOver.getBlockPos());
			}
		}
	}

	@EventHandler
	public void onLeftClick(LeftClickEvent e){
		if (this.isEnabled){
			if ((mc.objectMouseOver == null) ||
					(mc.objectMouseOver.getBlockPos() == null)) {
				return;
			}
            if (mc.world.getBlockState(mc.objectMouseOver.getBlockPos())
                    .getBlock().getMaterial(null) != Material.AIR) {
				this.isActive = true;
                this.oldSlot = mc.player.inventory.currentItem;
                setSlot(mc.objectMouseOver.getBlockPos());
            }
		}
	}

	public void setSlot(BlockPos blockPos)
	{
		float bestSpeed = 1.0F;
		int bestSlot = -1;
        IBlockState blockState = mc.world.getBlockState(blockPos);
        for (int i = 0; i < 9; i++) {
            ItemStack item = mc.player.inventory.getStackInSlot(i);
            if (item != null) {
				float speed = item.getStrVsBlock(blockState);
				if (speed > bestSpeed)
				{
					bestSpeed = speed;
					bestSlot = i;
				}
			}
		}
		if (bestSlot != -1) {
            mc.player.inventory.currentItem = bestSlot;
        }
    }
}
