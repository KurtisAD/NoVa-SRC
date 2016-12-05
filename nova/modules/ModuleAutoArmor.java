package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import nova.Command;
import nova.core.RegisterArgument;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/17/2016.
 */
public class ModuleAutoArmor extends ModuleBase{
    private boolean shields;
    private final String[] armorName = {"MAINHAND", "BOOTS", "LEGGINGS", "CHESTPLATE", "HELMET", "OFFHAND"};

    public ModuleAutoArmor(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        aliases.add("aa");
        this.command = new Command(Nova, this, aliases, "Equips armor if an armor slot is available and armor is found. From hotbar to top left to bottom right. Tells you when a piece is replaced.");

        shields = false;
    }


    @RegisterArgument(name = "shield", description = "Equips shield if offhand slot is available and is found.")
    public void toggleShield(){
        this.shields = !this.shields;
    }

    @EventHandler
    public void onPlayerTick(PlayerTickEvent e){
        if(this.isEnabled)
        {
            // 3 is head, 2 is chest, 1 is leg, 0 is boot
            // TODO: iterate through the List instead of pass to array
            ItemStack inv[];
            ItemStack armor[] = (ItemStack[]) mc.player.inventory.armorInventory.toArray();
            ItemStack offhand[] = (ItemStack[]) mc.player.inventory.offHandInventory.toArray();

            int armorIndex, inventoryIndex;

            for(armorIndex = 0; armorIndex < armor.length + 1; armorIndex++) {

                // if slot is missing an item
                if(armorIndex < armor.length ? armor[armorIndex] == null : offhand[0] == null) {

                    inv = (ItemStack[]) mc.player.inventory.mainInventory.toArray();

                    // looping through inventory
                    for(inventoryIndex = 0; inventoryIndex < inv.length; inventoryIndex++) {


                        EntityEquipmentSlot type;

                        // if an item exists
                        if(inv[inventoryIndex] != null) {

                            // and if the item is armor
                            if (inv[inventoryIndex].getItem() instanceof ItemArmor){
                                type = ((ItemArmor)inv[inventoryIndex].getItem()).armorType;

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
                            else if (inv[inventoryIndex].getItem() instanceof ItemShield && armorIndex == 4){
                                // and we're replacing offhand items
                                if (shields){
                                    replace(EntityEquipmentSlot.OFFHAND, inventoryIndex);
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
        Nova.notificationMessage("REPLACED " + this.getArmorName(type.getSlotIndex()));
        mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.QUICK_MOVE, mc.player);
    }

    @Override
    public String getMetadata(){
        return this.shields ? "(Shields)" : "";
    }
}
