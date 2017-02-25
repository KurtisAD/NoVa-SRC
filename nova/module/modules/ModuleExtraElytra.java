package nova.module.modules;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import nova.event.EventHandler;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;

/**
 * Created by Skeleton Man on 12/23/2016.
 */
public class ModuleExtraElytra extends ModuleBase {
    public ModuleExtraElytra() {
        super();

        this.description = ("Changes your speed mid-flight. From Wurst");
    }

    @EventHandler
    public void onTick(PlayerTickEvent e) {
        if (this.isEnabled) {
            ItemStack chest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (chest == null || chest.getItem() != Items.ELYTRA || ItemElytra.isBroken(chest))
                return;

            if (mc.gameSettings.keyBindJump.pressed)
                mc.player.motionY += 0.08;
            else if (mc.gameSettings.keyBindSneak.pressed)
                mc.player.motionY -= 0.04;

            if (mc.gameSettings.keyBindForward.pressed
                    && mc.player.getPosition().getY() < 256) {
                float yaw = (float) Math.toRadians(mc.player.rotationYaw);
                mc.player.motionX -= MathHelper.sin(yaw) * 0.05F;
                mc.player.motionZ += MathHelper.cos(yaw) * 0.05F;
            } else if (mc.gameSettings.keyBindBack.pressed
                    && mc.player.getPosition().getY() < 256) {
                float yaw = (float) Math.toRadians(mc.player.rotationYaw);
                mc.player.motionX += MathHelper.sin(yaw) * 0.05F;
                mc.player.motionZ -= MathHelper.cos(yaw) * 0.05F;
            }
        }
    }
}
