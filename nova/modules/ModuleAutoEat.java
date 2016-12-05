package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import nova.Command;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/17/2016.
 */
public class ModuleAutoEat extends ModuleBase {
    private int oldSlot;
    private int bestSlot;

    public ModuleAutoEat(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);
        this.command = new Command(Nova, this, aliases, "Automatically eats. From Wrust");
    }

    @EventHandler
    public void onUpdate(PlayerTickEvent e){
        if(isEnabled) {
            if (isEating()) {
                ItemStack item = mc.player.inventory.getStackInSlot(bestSlot);

                if (mc.player.getFoodStats().getFoodLevel() >= 20 || item == null || !(item.getItem() instanceof ItemFood)) {
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

                    if (item == null) {
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
