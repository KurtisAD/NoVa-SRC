package nova.module.modules;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import nova.event.EventHandler;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;

/**
 * Created by Skeleton Man on 7/17/2016.
 */
public class ModuleAutoEat extends ModuleBase {
    // TODO: implement my own way instead of using wurst code
    private int oldSlot;
    private int bestSlot;

    public ModuleAutoEat() {
        super();
        this.description = ("Automatically eats. From Wurst");
    }

    @EventHandler
    public void onUpdate(PlayerTickEvent e){

        // TODO: also implement for spectator or add less jank code
        if (isEnabled && !mc.player.isCreative()) {
            if (isEating()) {
                ItemStack item = mc.player.inventory.getStackInSlot(bestSlot);

                if (mc.player.getFoodStats().getFoodLevel() >= 20 || item == ItemStack.EMPTY || !(item.getItem() instanceof ItemFood)) {
                    stopEating();
                } else {
                    mc.player.inventory.currentItem = bestSlot;
                    mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
                    mc.gameSettings.keyBindUseItem.pressed = true;
                }
            } else {
                float bestSaturation = 0F;
                bestSlot = -1;

                for (int i = 0; i < 9; i++) {
                    ItemStack item = mc.player.inventory.getStackInSlot(i);

                    if (item == ItemStack.EMPTY) {
                        continue;
                    }

                    float saturation = 0;

                    if (item.getItem() instanceof ItemFood) {
                        saturation = ((ItemFood) item.getItem()).getSaturationModifier(item);
                    }

                    if (saturation > bestSaturation) {
                        bestSaturation = saturation;
                        bestSlot = i;
                    }
                }
                if (bestSlot == -1)
                    return;
                oldSlot = mc.player.inventory.currentItem;
            }
        }
    }

    public boolean isEating()
    {
        return oldSlot != -1;
    }

    public void stopEating(){
        mc.gameSettings.keyBindUseItem.pressed = false;
        mc.player.inventory.currentItem = oldSlot;
        oldSlot = -1;
    }
}
