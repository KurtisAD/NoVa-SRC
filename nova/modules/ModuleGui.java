package nova.modules;

import com.google.gson.annotations.Expose;
import net.minecraft.client.gui.*;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import nova.core.Util;
import nova.events.GuiOpenEvent;
import nova.events.RenderOverlayEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import nova.Command;
import nova.Nova;
import nova.events.EventHandler;
import nova.gui.GuiDisconnectedOverride;
import nova.gui.GuiMultiplayerOverride;
import nova.gui.GuiScreenServerListOverride;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Skeleton Man on 6/22/2016.
 */
public class ModuleGui extends ModuleBase{

    int width, height;
    ScaledResolution scaledResolution;
    private ArrayList<HashMap<String, Integer>> note;
    int maxDisplayedNotifications;
    int notificationTimeout;
    int ticks;
    int potionShift;
    String fps = "";

    @Expose
    int guiColor;
    @Expose
    String infoFormat;
    @Expose
    boolean showArmor;
    @Expose
    public boolean potionInSeconds;
    @Expose
    public boolean isHidden;


    public static final String[] directions = { "SOUTH", "WEST", "NORTH", "EAST" };


    public ModuleGui(Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);

        this.isHidden = false;
        this.note = new ArrayList<HashMap<String, Integer>>();
        this.maxDisplayedNotifications = 5;
        this.notificationTimeout = 256;
        this.ticks = 0;
        this.potionShift = 0;

        this.command = new Command(Nova, this, aliases, "Toggles the GUI");
        this.command.registerArg("format", this.getClass().getMethod("setInfoFormat", String.class), "Wrap your argument in quotes! Changes format of the coord/info text; {x} parses to the x-coord, {z}, {y} do the same; {d} is your direction, {D} is it's single character representation; {v} is your velocity in km/h, {fps}, & is the formatting char");
        this.command.registerArg("armor", this.getClass().getMethod("toggleArmor") , "Toggles armor display");
        this.command.registerArg("potion", this.getClass().getMethod("togglePotionInSeconds"), "Toggles potion timer, either in seconds or minutes.");

        this.guiColor = 0xFFFFFF;
        this.infoFormat = "[{x}, {z}] {v}km/h";
        this.potionInSeconds = true;
        this.showArmor = true;

        loadModule();
    }

    @Override
    public void saveModule(){
        json.add("guiColor",Util.getGson().toJsonTree(guiColor));
        json.add("infoFormat", Util.getGson().toJsonTree(infoFormat));
        json.add("potionInSeconds", Util.getGson().toJsonTree(potionInSeconds));
        json.add("showArmor", Util.getGson().toJsonTree(showArmor));
        super.saveModule();
    }

    @Override
    public void load(){
        super.load();
        this.guiColor = Util.getGson().fromJson(json.get("guiColor"), Integer.class);
        this.infoFormat = Util.getGson().fromJson(json.get("infoFormat"), String.class);
        this.potionInSeconds = Util.getGson().fromJson(json.get("potionInSeconds"), Boolean.class);
        this.showArmor = Util.getGson().fromJson(json.get("showArmor"), Boolean.class);

    }

    @Override
    public void toggleState()
    {
        this.isHidden = !this.isHidden;
    }

    @EventHandler
    public void onRenderOverlay(RenderOverlayEvent e)
    {

        scaledResolution = new ScaledResolution(mc);
        width = scaledResolution.getScaledWidth();
        height = scaledResolution.getScaledHeight();

        if(!mc.gameSettings.showDebugInfo && !isHidden) {

            String x = Integer.toString((int)Math.floor(mc.thePlayer.posX) + ModuleFakeCoord.getX());
            String y = Integer.toString((int)Math.floor(mc.thePlayer.posY) + ModuleFakeCoord.getY());
            String z = Integer.toString((int)Math.floor(mc.thePlayer.posZ) + ModuleFakeCoord.getZ());
            String v = Double.toString(this.getPlayerVelocity());
            String d = directions[MathHelper.floor_double((double)(mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3];
            String D = Character.toString(d.charAt(0));

            /*
            if(Sys.getTime() * 1000L / Sys.getTimerResolution() >= mc.debugUpdateTime + 1000L)
                fps = Integer.toString(mc.getDebugFPS());
            */
            fps = Integer.toString(Minecraft.getDebugFPS());

            String info = this.infoFormat.replaceAll("&", "\247").replaceAll("\\{x\\}", x).replaceAll("\\{z\\}", z).replaceAll("\\{y\\}", y).replaceAll("\\{v\\}", v).replaceAll("\\{d\\}", d).replaceAll("\\{D\\}", D).replaceAll("\\{fps\\}", fps);


            mc.fontRendererObj.drawStringWithShadow(info, 2, 2, this.guiColor);

            this.drawModuleData();
            this.drawTime();
            this.drawStats();


            this.drawNotifications();

            this.decrementNotes();

        }
    }

    private void drawModuleData()
    {
        int position = 12;
        for(ModuleBase m : Nova.modules) {

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
        ItemStack armor[] = mc.thePlayer.inventory.armorInventory;
        String helmet, chestplate, leggings, boots = "";

        helmet = "h: " + (armor[3] != null ? Util.formatArmorDurability( ( ((double)armor[3].getMaxDamage() - (double)armor[3].getItemDamage() ) / (double)armor[3].getMaxDamage()) * 100.0D ) + "%" : "none");
        chestplate = "c: " + (armor[2] != null ? Util.formatArmorDurability((((double)armor[2].getMaxDamage() - (double)armor[2].getItemDamage()) / (double)armor[2].getMaxDamage()) * 100.0D) + "%" : "none");
        leggings = "l: " + (armor[1] != null ? Util.formatArmorDurability((((double)armor[1].getMaxDamage() - (double)armor[1].getItemDamage()) / (double)armor[1].getMaxDamage()) * 100.0D) + "%" : "none");
        boots = "b: " + (armor[0] != null ? Util.formatArmorDurability( (((double)armor[0].getMaxDamage() - (double)armor[0].getItemDamage()) / (double)armor[0].getMaxDamage()) * 100.0D)  + "%" : "none");

        Iterator potions = mc.thePlayer.getActivePotionEffects().iterator();
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
        }

    }

    private double getPlayerVelocity()
    {

        double velocity = Math.floor(Math.sqrt(Math.pow(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, 2) + Math.pow(mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ, 2)) * 20 * 60 * 60);
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

    public void setInfoFormat(String infoFormat){
        this.infoFormat = infoFormat;
    }

    public void toggleArmor(){
        this.showArmor = !this.showArmor;
    }

    public void togglePotionInSeconds(){
        this.potionInSeconds = !potionInSeconds;
    }
}
