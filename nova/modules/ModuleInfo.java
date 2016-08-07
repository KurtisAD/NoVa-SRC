package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import nova.Command;
import nova.core.Util;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by Skeleton Man on 7/19/2016.
 */
public class ModuleInfo extends ModuleBase{
    public ModuleInfo(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Gives info about a player; currently only their armor and it's enchantments; unless a player is designated, you will get info for the closest player to you");
        this.command.registerArg("player", this.getClass().getMethod("getInfo", String.class), "Player to get info on; not case sensitive");
        this.defaultArg = "player";

        loadModule();
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

        for (int i = 0; i < mc.theWorld.playerEntities.size(); ++i)
        {
            if(mc.theWorld.playerEntities.get(i).getDisplayName().getUnformattedText().equals(mc.thePlayer.getDisplayName().getUnformattedText()))
            {
                continue;
            }

            d = mc.thePlayer.getDistanceSqToEntity(mc.theWorld.playerEntities.get(i));

            if(d < closestD)
            {
                closestD = d;
                closest = mc.theWorld.playerEntities.get(i);
            }
        }

        if(closest != null)
            this.getInfo(closest.getDisplayName().getUnformattedText());
        else
            this.Nova.errorMessage("No players found!");
    }

    public EntityPlayer getPlayerByName(String player)
    {
        for (int var2 = 0; var2 < mc.theWorld.playerEntities.size(); ++var2)
        {
            if (player.equalsIgnoreCase(mc.theWorld.playerEntities.get(var2).getDisplayName().getUnformattedText()))
            {
                return mc.theWorld.playerEntities.get(var2);
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

            ItemStack[] armor = p.inventory.armorInventory.clone();
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

    public void getInfo(String player)
    {
        EntityPlayer p = this.getPlayerByName(player);

        if(this.getPlayerByName(player) != null)
        {
            this.Nova.message("\247l" + p.getDisplayName().getUnformattedText() + "\'s Armor");
            String durability = "";

            ItemStack[] armor = p.inventory.armorInventory.clone();
            ArrayUtils.reverse(armor);

            for(ItemStack i : armor)
            {
                this.Nova.message(Util.getItemNameAndEnchantments(i));
                durability += (i != null ? Util.formatArmorDurability( ( ((double)i.getMaxDamage() - (double)i.getItemDamage() ) / (double)i.getMaxDamage()) * 100.0D ) : "---") + "/";

            }
            this.Nova.message("\247l" + durability.substring(0, durability.length() - 1));

            String item = Util.getItemNameAndEnchantments(((EntityOtherPlayerMP)p).inventory.getCurrentItem());
            String leftItem = Util.getItemNameAndEnchantments(((EntityOtherPlayerMP)p).inventory.offHandInventory[0]);
            Nova.message("Main Hand: " + item);
            Nova.message("Off Hand: " + leftItem);



        }
        else
        {
            this.Nova.errorMessage("Player not found!");
        }
    }
}
