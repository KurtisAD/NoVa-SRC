package nova.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import nova.Nova;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.GuiOpenEvent;
import nova.event.events.RenderOverlayEvent;
import nova.gui.GuiDisconnectedOverride;
import nova.gui.GuiMultiplayerOverride;
import nova.gui.GuiScreenServerListOverride;
import nova.module.ModuleBase;
import nova.saver.Saveable;
import nova.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Skeleton Man on 6/22/2016.
 */
public class ModuleGui extends ModuleBase {
    // TODO: Add tickrate to render; implement settings and help
    // TODO: fix potion timer, poses incorrect time

    private int width, height;
    private ArrayList<HashMap<String, Integer>> note;
    private int maxDisplayedNotifications;
    private int potionShift;

    @Saveable
    public int guiColor;
    @Saveable
    public String infoFormat;
    @Saveable
    public boolean showArmor;
    @Saveable
    public boolean potionInSeconds;
    @Saveable
    public boolean isHidden;


    public static final String[] directions = { "SOUTH", "WEST", "NORTH", "EAST" };


    public ModuleGui() {
        super();

        this.isHidden = false;
        this.note = new ArrayList<HashMap<String, Integer>>();
        this.maxDisplayedNotifications = 5;
        this.potionShift = 0;

        this.description = ("Toggles the GUI");

        this.guiColor = 0xFFFFFF;
        this.infoFormat = "[{x}, {z}] {v}km/h";
        this.potionInSeconds = true;
        this.showArmor = true;
    }


    @Override
    public void toggleState()
    {
        this.isHidden = !this.isHidden;
    }

    @EventHandler
    public void onRenderOverlay(RenderOverlayEvent e)
    {

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        width = scaledResolution.getScaledWidth();
        height = scaledResolution.getScaledHeight();

        if(!mc.gameSettings.showDebugInfo && !isHidden) {

            String x = Integer.toString((int) Math.floor(mc.player.posX) + ModuleFakeCoord.getX());
            String y = Integer.toString((int) Math.floor(mc.player.posY) + ModuleFakeCoord.getY());
            String z = Integer.toString((int) Math.floor(mc.player.posZ) + ModuleFakeCoord.getZ());
            String v = Double.toString(this.getPlayerVelocity());
            String d = directions[MathHelper.floor((double) (mc.player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3];
            String D = Character.toString(d.charAt(0));


            String fps = Integer.toString(Minecraft.getDebugFPS());

            String info = this.infoFormat.replaceAll("&", "\247").replaceAll("\\{x\\}", x).replaceAll("\\{z\\}", z).replaceAll("\\{y\\}", y).replaceAll("\\{v\\}", v).replaceAll("\\{d\\}", d).replaceAll("\\{D\\}", D).replaceAll("\\{fps\\}", fps);


            mc.fontRendererObj.drawStringWithShadow(info, 2, 2, this.guiColor);

            this.drawModuleData();
            this.drawTime();
            this.drawStats();


            this.drawNotifications();

            this.decrementNotes();

            this.drawHorseStats();

        }
    }

    private void drawModuleData()
    {
        int position = 12;
        for (ModuleBase m : Nova.getModules()) {

            if(m.isEnabled && m.showEnabled) {
                mc.fontRendererObj.drawStringWithShadow("> " + Util.capitalize(m.name) + " " + m.getMetadata(), 2, position, this.guiColor);
                position += 10;
            }
        }
    }

    private void drawTime()
    {
        String date = new SimpleDateFormat("hh:mm a").format(new Date());
        mc.fontRendererObj.drawStringWithShadow(date, width - mc.fontRendererObj.getStringWidth(date) - 4, height - 12, this.guiColor);
    }


    private void drawStats()
    {
        // This should be fine to access by pointer since we're not editing anything
        NonNullList<ItemStack> armor = mc.player.inventory.armorInventory;
        String helmet, chestplate, leggings, boots, saturation = "";

        // Maybe have to change to .equals() ?
        // TODO: maybe make this a method?
        helmet = "h: " + (armor.get(3) != ItemStack.EMPTY ? Util.formatArmorDurability((((double) armor.get(3).getMaxDamage() - (double) armor.get(3).getItemDamage()) / (double) armor.get(3).getMaxDamage()) * 100.0D) + "%" : "none");
        chestplate = "c: " + (armor.get(2) != ItemStack.EMPTY ? Util.formatArmorDurability((((double) armor.get(2).getMaxDamage() - (double) armor.get(2).getItemDamage()) / (double) armor.get(2).getMaxDamage()) * 100.0D) + "%" : "none");
        leggings = "l: " + (armor.get(1) != ItemStack.EMPTY ? Util.formatArmorDurability((((double) armor.get(1).getMaxDamage() - (double) armor.get(1).getItemDamage()) / (double) armor.get(1).getMaxDamage()) * 100.0D) + "%" : "none");
        boots = "b: " + (armor.get(0) != ItemStack.EMPTY ? Util.formatArmorDurability((((double) armor.get(0).getMaxDamage() - (double) armor.get(0).getItemDamage()) / (double) armor.get(0).getMaxDamage()) * 100.0D) + "%" : "none");
        saturation = "sat: " + (mc.player.getFoodStats().getSaturationLevel() <= 0f ? "\247c" : "") + String.format("%.1f", mc.player.getFoodStats().getSaturationLevel());

        Iterator potions = mc.player.getActivePotionEffects().iterator();
        PotionEffect potionHolder;

        int i = 0;
        int j = 0;
        String duration;

        potionShift = 0;

        // This does the potion timer stuff and the armor placement shift
        while(potions.hasNext()) {
            potionHolder = ((PotionEffect) potions.next());
            int k = width;
            int l = 1;


            if (potionHolder.getPotion().isBeneficial()){
                i++;
                k = k - 25*i;
                potionShift = potionShift > 25 ? potionShift : 25;
            } else {
                ++j;
                k = k - 25 * j;
                l += 26;
                potionShift = 51;
            }

            // formatting the potion time output
            if(potionInSeconds){
                duration = Integer.toString(potionHolder.getDuration() / 20);
            } else {
                if (potionHolder.getDuration() / 20 > 599){
                    duration = Integer.toString(potionHolder.getDuration() / 1200) + "m";
                } else {
                    duration = Potion.getPotionDurationString(potionHolder, 1.0F);
                }
            }
            if( (potionHolder.getDuration() / 20) <= 5){
                duration = "\2474" + duration;
            } else if( (potionHolder.getDuration() / 20) <= 10) {
                duration = "\247c" + duration;
            } else {
                duration = "\2477" + duration;
            }

            if (potionInSeconds){
                mc.fontRendererObj.drawStringWithShadow(duration,k + 22 - mc.fontRendererObj.getStringWidth(duration),l + 15, 0xFFFFFF);
            } else {
                mc.fontRendererObj.drawStringWithShadow(duration,k + 3,l + 15, 0xFFFFFF);
            }
        }

        if(this.showArmor)
        {
            mc.fontRendererObj.drawStringWithShadow(helmet, width - mc.fontRendererObj.getStringWidth(helmet) - 2, 2 + potionShift, this.guiColor);
            mc.fontRendererObj.drawStringWithShadow(chestplate, width - mc.fontRendererObj.getStringWidth(chestplate) - 2, 12 + potionShift, this.guiColor);
            mc.fontRendererObj.drawStringWithShadow(leggings, width - mc.fontRendererObj.getStringWidth(leggings) - 2, 22 + potionShift, this.guiColor);
            mc.fontRendererObj.drawStringWithShadow(boots, width - mc.fontRendererObj.getStringWidth(boots) - 2, 32 + potionShift, this.guiColor);
            mc.fontRendererObj.drawStringWithShadow(saturation, width - mc.fontRendererObj.getStringWidth(saturation) - 2, 42 + potionShift, this.guiColor);
        }

    }

    private double getPlayerVelocity()
    {

        double velocity = Math.floor(Math.sqrt(Math.pow(mc.player.posX - mc.player.lastTickPosX, 2) + Math.pow(mc.player.posZ - mc.player.lastTickPosZ, 2)) * 20 * 60 * 60);
        velocity /= 100;
        velocity = Math.round(velocity);
        velocity /= 10;
        return velocity;
    }

    private void drawNotifications()
    {
        int x = 0, y = 0, pos = 0;
        String note = "";

        for(int i = this.note.size() - 1; i >= 0; i--)
        {
            if(i == this.maxDisplayedNotifications)
                break;

            note = (String) this.note.get(i).keySet().toArray()[0];


            x = (width - mc.fontRendererObj.getStringWidth(note)) / 2;
            y = (height - 8) / 2;

            mc.fontRendererObj.drawStringWithShadow(note, x, y - pos, this.guiColor);


            pos += 10;
        }

    }

    public void addToQueue(String msg)
    {
        this.note.add(new HashMap<String, Integer>());
        this.note.get(this.note.size() - 1).put(msg, 60);
    }

    public void decrementNotes()
    {
        String key = "";
        for(int i = 0; i < this.note.size(); i++)
        {
            key = (String) this.note.get(i).keySet().toArray()[0];

            if(this.note.get(i).get(key) <= 0)
            {
                this.note.remove(i);
                continue;
            }

            this.note.get(i).put(key, this.note.get(i).get(key) - 1);

        }
    }

    private void drawHorseStats() {
        if (mc.player != null && mc.player.getRidingEntity() instanceof AbstractHorse) {
            double speed = ((AbstractHorse) mc.player.getRidingEntity()).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            double jump = ((AbstractHorse) mc.player.getRidingEntity()).getHorseJumpStrength();
            double health = ((AbstractHorse) mc.player.getRidingEntity()).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();

            double healthPercent = (health - 15) / 0.15D;
            double jumpPercent = (jump - 0.4D) / 0.006D;
            double speedPercent = (speed - 0.1125) / 0.00225D;

            String healthStr = "Health: " + percentageModifier(healthPercent);
            String jumpStr = "Jump: " + percentageModifier(jumpPercent);
            String speedStr = "Speed: " + percentageModifier(speedPercent) + "\247r | " + String.format("%.3f", speed);

            mc.fontRendererObj.drawStringWithShadow(healthStr, width / 2 + 92, height - 45, 0xFFFFFF);
            mc.fontRendererObj.drawStringWithShadow(jumpStr, width / 2 + 92, height - 35, 0xFFFFFF);
            mc.fontRendererObj.drawStringWithShadow(speedStr, width / 2 + 92, height - 25, 0xFFFFFF);
        }
    }

    private String percentageModifier(double percent) {
        String suffix = String.format("%.1f", percent) + "%";
        String prefix = "\247";
        if (percent < 20D) {
            prefix += "c";
        } else if (percent < 40D) {
            prefix += "4";
        } else if (percent < 60D) {
            prefix += "3";
        } else if (percent < 80D) {
            prefix += "a";
        } else if (percent < 100D) {
            prefix += "6";
        } else {
            prefix += "d";
        }
        return prefix + suffix;
    }

    @EventHandler
    public void onGuiOpenEvent(GuiOpenEvent e){
        if (e.getGui() instanceof GuiDisconnected){
            GuiDisconnected screen = (GuiDisconnected) e.getGui();
            e.setGui(new GuiDisconnectedOverride(screen.parentScreen, screen.reason, screen.message));
        } else if (e.getGui() instanceof GuiMultiplayer){
            GuiMultiplayer screen = (GuiMultiplayer) e.getGui();
            e.setGui(new GuiMultiplayerOverride(screen.parentScreen));
        } else if (e.getGui() instanceof GuiScreenServerList){
            GuiScreenServerList screen = (GuiScreenServerList) e.getGui();
            e.setGui(new GuiScreenServerListOverride(screen.lastScreen, screen.serverData));
        }
    }

    @RegisterArgument(name = "format",
            description = "Wrap your argument in quotes! Changes format of the coord/info text; {x} parses to the x-coord, {z}, {y} do the same; {d} is your direction, {D} is it's single character representation; {v} is your velocity in km/h, {fps}, & is the formatting char")
    public void setInfoFormat(String infoFormat){
        this.infoFormat = infoFormat;
    }

    @RegisterArgument(name = "armor", description = "Toggles armor display")
    public void toggleArmor(){
        this.showArmor = !this.showArmor;
    }

    @RegisterArgument(name = "potion", description = "Toggles potion timer, either in seconds or minutes")
    public void togglePotionInSeconds(){
        this.potionInSeconds = !potionInSeconds;
    }
}
