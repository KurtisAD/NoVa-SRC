package nova.module.modules;

import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityShulkerBox;
import nova.event.EventHandler;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;
import nova.util.Util;

/**
 * Created by Skeleton Man on 1/14/2017.
 */
public class ModulePeek extends ModuleBase {
    // TODO: make it not modify ItemShulkerBox
    // TODO: make more generic so it works with all containers (currently only shulker boxes)
    // Maybe implement with middle click, or edit inventory GUI
    TileEntityShulkerBox sb;

    public ModulePeek() {
        super();
        this.description = "Peek inside shulker boxes";
        this.sb = null;
    }

    @Override
    public void toggleState() {
        ItemStack is = mc.player.inventory.getCurrentItem();

        if (is.getItem() instanceof ItemShulkerBox) {
            TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            Util.setPrivateValue(TileEntityShulkerBox.class, entityBox, ((ItemShulkerBox) is.getItem()).getBlock(), "blockType");
            entityBox.setWorldObj(mc.world);
            entityBox.readFromNBT(is.getTagCompound().getCompoundTag("BlockEntityTag"));
            sb = entityBox;
        }
    }

    @EventHandler
    public void onTick(PlayerTickEvent e) {
        if (sb != null) {
            mc.player.displayGUIChest(sb);
            sb = null;
        }
    }
}
