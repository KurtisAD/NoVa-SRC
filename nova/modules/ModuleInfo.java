package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import nova.Command;
import nova.core.RegisterArgument;
import nova.core.Util;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by Skeleton Man on 7/19/2016.
 */
public class ModuleInfo extends ModuleBase{
    public ModuleInfo(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Gives info about a player; currently only their armor and it's enchantments; unless a player is designated, you will get info for the closest player to you");
        this.defaultArg = "player";

    }

    @Override
    public void onEnable()
    {

    }

    @Override
    public void onDisable()
    {

    }

    @Override
    public void toggleState()
    {
        double closestD = Double.MAX_VALUE;
        double d = 0.0D;
        EntityPlayer closest = null;

        for (int i = 0; i < mc.world.playerEntities.size(); ++i)
        {
            if (mc.world.playerEntities.get(i).getDisplayName().getUnformattedText().equals(mc.player.getDisplayName().getUnformattedText()))
            {
                continue;
            }

            d = mc.player.getDistanceSqToEntity(mc.world.playerEntities.get(i));

            if(d < closestD)
            {
                closestD = d;
                closest = mc.world.playerEntities.get(i);
            }
        }

        if(closest != null)
            this.getInfo(closest.getDisplayName().getUnformattedText());
        else
            this.Nova.errorMessage("No players found!");
    }

    public EntityPlayer getPlayerByName(String player)
    {
        for (int var2 = 0; var2 < mc.world.playerEntities.size(); ++var2)
        {
            if (player.equalsIgnoreCase(mc.world.playerEntities.get(var2).getDisplayName().getUnformattedText()))
            {
                return mc.world.playerEntities.get(var2);
            }
        }

        return null;
    }

    public String getArmorDurability(String player)
    {
        EntityPlayer p = this.getPlayerByName(player);

        if(this.getPlayerByName(player) != null)
        {
            String durability = "";

            ItemStack[] armor = (ItemStack[]) p.inventory.armorInventory.toArray();
            ArrayUtils.reverse(armor);
            boolean hasArmor = false;

            for(ItemStack i : armor)
            {
                durability += (i != null ? Util.formatArmorDurability( ( ((double)i.getMaxDamage() - (double)i.getItemDamage() ) / (double)i.getMaxDamage()) * 100.0D ): "---") + "/";

                if(i != null)
                    hasArmor = true;
            }

            return hasArmor ? durability.substring(0, durability.length() - 1) : "no armor";
        }
        else
        {
            return null;
        }
    }

    @RegisterArgument(name = "player", description = "Player to get info on; not case sensitive")
    public void getInfo(String player) {
        EntityPlayer p = this.getPlayerByName(player);

        if (this.getPlayerByName(player) != null) {
            this.Nova.message("\247l" + p.getDisplayName().getUnformattedText() + "\'s Armor");
            String durability = "";

            ItemStack[] armor = (ItemStack[]) p.inventory.armorInventory.toArray();
            ArrayUtils.reverse(armor);

            for (ItemStack i : armor) {
                this.Nova.message(Util.getItemNameAndEnchantments(i));
                durability += (i != null ? Util.formatArmorDurability( ( ((double)i.getMaxDamage() - (double)i.getItemDamage() ) / (double)i.getMaxDamage()) * 100.0D ) : "---") + "/";

            }
            this.Nova.message("\247l" + durability.substring(0, durability.length() - 1));

            String item = Util.getItemNameAndEnchantments(((EntityOtherPlayerMP)p).inventory.getCurrentItem());
            String leftItem = Util.getItemNameAndEnchantments(((EntityOtherPlayerMP) p).inventory.offHandInventory.get(0));
            Nova.message("Main Hand: " + item);
            Nova.message("Off Hand: " + leftItem);


        } else {
            this.Nova.errorMessage("Player not found!");
        }
    }
}
