package nova.module.modules;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.ItemEmptyMap;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import nova.event.EventHandler;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;

/**
 * @author Kurt Dee
 * @since 2/26/2017
 */
public class ModuleRewriteMaps extends ModuleBase {

    public ModuleRewriteMaps() {
        super();
        this.description = "Uses a bunch of maps then drops them";
    }

    @EventHandler
    public void onTick(PlayerTickEvent e) {
        if (isEnabled && mc.player.openContainer instanceof ContainerPlayer) {
            dropCurrentMaps(swapCurrentSlotToEmptyMap());
            if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemEmptyMap) {
                mc.player.connection.getNetworkManager().sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mc.player.connection.getNetworkManager().sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mc.player.connection.getNetworkManager().sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
        }
    }

    private void dropCurrentMaps(boolean emptyMapsExist) {
        if (emptyMapsExist) {
            return;
        }
        NonNullList<ItemStack> inv = mc.player.inventory.mainInventory;
        int inventoryIndex;

        for (inventoryIndex = 0; inventoryIndex < inv.size(); inventoryIndex++) {
            if (inv.get(inventoryIndex).getItem() instanceof ItemMap) {
                mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.THROW, mc.player);
                return;
            }
        }
    }


    private boolean swapCurrentSlotToEmptyMap() {
        NonNullList<ItemStack> inv = mc.player.inventory.mainInventory;
        int inventoryIndex;

        if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemEmptyMap) {
            return true;
        }
        for (inventoryIndex = 0; inventoryIndex < inv.size(); inventoryIndex++) {
            if (inv.get(inventoryIndex).getItem() instanceof ItemEmptyMap) {
                mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                return true;
            }
        }
        return false;
    }
}
