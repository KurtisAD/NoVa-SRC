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

    public ModuleAutoEat(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);
        this.command = new Command(Nova, this, aliases, "Automatically eats. From Wrust");

        loadModule();
    }

    @EventHandler
    public void onUpdate(PlayerTickEvent e){
        if(isEnabled) {
            if (isEating()) {
                ItemStack item = mc.thePlayer.inventory.getStackInSlot(bestSlot);

                if (mc.thePlayer.getFoodStats().getFoodLevel() >= 20 || item == null || !(item.getItem() instanceof ItemFood)) {
                    stopEating();
                } else {
                    mc.thePlayer.inventory.currentItem = bestSlot;
                    mc.playerController.processRightClick(mc.thePlayer, mc.theWorld, item, EnumHand.MAIN_HAND);
                    mc.gameSettings.keyBindUseItem.pressed = true;
                }
            } else {
                float bestSaturation = 0F;
                bestSlot = -1;

                for (int i = 0; i < 9; i++) {
                    ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);

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
                oldSlot = mc.thePlayer.inventory.currentItem;
            }
        }
    }

    public boolean isEating()
    {
        return oldSlot != -1;
    }

    public void stopEating(){
        mc.gameSettings.keyBindUseItem.pressed = false;
        mc.thePlayer.inventory.currentItem = oldSlot;
        oldSlot = -1;
    }
}
