package nova.module.modules;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import nova.Nova;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;

/**
 * Created by Skeleton Man on 7/17/2016.
 */
public class ModuleAutoArmor extends ModuleBase {
    // TODO: fix everything using NonNullList, implement auto-totem, decide what to do with shields
    // TODO: rewrite all this shit because I just fixed it without knowing what I was doing


    @Saveable
    public boolean totem;

    private final String[] armorName = {"MAINHAND", "BOOTS", "LEGGINGS", "CHESTPLATE", "HELMET", "OFFHAND"};

    public ModuleAutoArmor() {
        super();

        aliases.add("aa");
        this.description = ("Equips armor if an armor slot is available and armor is found. From hotbar to top left to bottom right. Tells you when a piece is replaced.");

        totem = false;
    }


    @RegisterArgument(name = "totem", description = "Equips totem if offhand slot is available and is found.")
    public void toggleTotem() {
        this.totem = !this.totem;
    }

    @EventHandler
    public void onPlayerTick(PlayerTickEvent e){
        if(this.isEnabled)
        {
            // 3 is head, 2 is chest, 1 is leg, 0 is boot
            NonNullList<ItemStack> inv;
            NonNullList<ItemStack> armor = mc.player.inventory.armorInventory;
            NonNullList<ItemStack> offhand = mc.player.inventory.offHandInventory;

            int armorIndex, inventoryIndex;

            for (armorIndex = 0; armorIndex < armor.size() + 1; armorIndex++) {

                // if slot is missing an item
                if (armorIndex < armor.size() ? armor.get(armorIndex) == ItemStack.field_190927_a : offhand.get(0) == ItemStack.field_190927_a) {

                    inv = mc.player.inventory.mainInventory;

                    // looping through inventory
                    for (inventoryIndex = 0; inventoryIndex < inv.size(); inventoryIndex++) {


                        EntityEquipmentSlot type;

                        // if an item exists
                        if (inv.get(inventoryIndex) != ItemStack.field_190927_a) {

                            // and if the item is armor
                            if (inv.get(inventoryIndex).getItem() instanceof ItemArmor) {
                                type = ((ItemArmor) inv.get(inventoryIndex).getItem()).armorType;

                                // and it's the type of armor needed
                                if(     (type == EntityEquipmentSlot.HEAD && armorIndex == 3) ||
                                        (type == EntityEquipmentSlot.CHEST && armorIndex == 2) ||
                                        (type == EntityEquipmentSlot.LEGS && armorIndex == 1) ||
                                        (type == EntityEquipmentSlot.FEET && armorIndex == 0)){

                                    replace(type, inventoryIndex);
                                    break;
                                }
                            }
                            // else if the item is a shield

                            else if (inv.get(inventoryIndex).getItem().getUnlocalizedName().equals("item.totem") && armorIndex == 4) {
                                // and we're replacing offhand items
                                if (totem) {
                                    replaceTotem(inventoryIndex);
                                    break;

                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public String getArmorName(int armorType)
    {
        if(armorType < armorName.length){
            return armorName[armorType];
        }
        return "UNKNOWN";
    }

    public void replace(EntityEquipmentSlot type, int inventoryIndex){
        if (mc.player.openContainer instanceof ContainerPlayer) {
            Nova.notificationMessage("REPLACED " + this.getArmorName(type.getSlotIndex()));
            mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.QUICK_MOVE, mc.player);

        }
    }

    public void replaceTotem(int inventoryIndex) {
        if (mc.player.openContainer instanceof ContainerPlayer) {
            Nova.notificationMessage("REPLACED TOTEM");
            mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
        }
    }

    @Override
    public String getMetadata(){
        return this.totem ? "(Totems)" : "";
    }
}
