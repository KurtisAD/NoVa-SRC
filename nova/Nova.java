package nova;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import nova.core.Util;
import nova.modules.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Skeleton Man on 6/18/2016.
 */

// Implement in minecraft
public class Nova {
    // TODO: implement enabled event (What did I mean by this shit shit shit) (I think I meant adding an event type that checks if module is enabled? Similar to implementing pre/post events?)
    // TODO: organize core so that it has some consistency
    // TODO: consider adding modules via reflection
    // TODO: Document more and more
    // TODO: make annotations throw errors
    // I swear to fuck I add more todo's than I solve

    public Minecraft mc;
    public Events events;
    public ArrayList<ModuleBase> modules;
    public Map<String, ModuleBase> moduleCache;
    public Map<String, String> moduleNameCache;

    public final String delimeter;

    public static final String Version = "NoVa 11.2.a13 Optifine B5";
    /**
     * NovaClient file directory ".minecraft/Nova/"
     */
    public static File novaDir = new File(Util.getAppDir("minecraft") + File.separator + "Nova");

    public Nova(Minecraft mc){
        this.mc = mc;

        if (!novaDir.exists()) {
            novaDir.mkdirs();
        }

        this.delimeter = "-";

        modules = new ArrayList<ModuleBase>();
        moduleCache = new HashMap<String, ModuleBase>();
        moduleNameCache = new HashMap<String, String>();


        //Add Modules here
        //Format:            this.modules.add(new ModuleBase(this, mc));


        this.modules.add(new ModuleAntiAfk(this, mc));
        this.modules.add(new ModuleAntifall(this, mc));
        this.modules.add(new ModuleAutoArmor(this, mc));
        this.modules.add(new ModuleAutoEat(this, mc));
        this.modules.add(new ModuleAutoFish(this, mc));
        this.modules.add(new ModuleAutoMine(this, mc));
        this.modules.add(new ModuleAutoRespawn(this, mc));
        //this.modules.add(new ModuleAutotool(this, mc));
        this.modules.add(new ModuleAutowalk(this, mc));
        this.modules.add(new ModuleBindEditor(this, mc));
        //this.modules.add(new ModuleBlink(this, mc));
        this.modules.add(new ModuleBrightness(this, mc));
        this.modules.add(new ModuleCameraClip(this, mc));
        this.modules.add(new ModuleElytraFly(this, mc));
        this.modules.add(new ModuleEncryption(this, mc));
        this.modules.add(new ModuleESP(this, mc));
        //this.modules.add(new ModuleExtraElytra(this, mc));
        //this.modules.add(new ModuleFakeCoord(this, mc));
        this.modules.add(new ModuleFastBreak(this, mc));
        this.modules.add(new ModuleFly(this, mc));
        this.modules.add(new ModuleFreecam(this, mc));
        this.modules.add(new ModuleFriends(this, mc));
        this.modules.add(new ModuleGlide(this, mc));
        this.modules.add(new ModuleGreet(this, mc));
        this.modules.add(new ModuleGui(this, mc));
        this.modules.add(new ModuleHelp(this, mc));
        this.modules.add(new ModuleHorseRide(this, mc));
        this.modules.add(new ModuleInfo(this, mc));
        this.modules.add(new ModuleIntervalThrow(this, mc));
        //this.modules.add(new ModuleJesus(this, mc));
        this.modules.add(new ModuleMarkers(this, mc));
        this.modules.add(new ModuleNames(this, mc));
        this.modules.add(new ModuleNoclip(this, mc));
        this.modules.add(new ModuleNofall(this, mc));
        this.modules.add(new ModuleNoKnockback(this, mc));
        this.modules.add(new ModuleNoRender(this, mc));
        this.modules.add(new ModuleNoslow(this, mc));
        this.modules.add(new ModuleNotifications(this, mc));
        this.modules.add(new ModulePeek(this, mc));
        this.modules.add(new ModuleSafewalk(this, mc));
        this.modules.add(new ModuleSay(this, mc));
        this.modules.add(new ModuleSpeed(this, mc));
        this.modules.add(new ModuleSprint(this, mc));
        this.modules.add(new ModuleTextwidth(this, mc));
        this.modules.add(new ModuleTimer(this, mc));
        this.modules.add(new ModuleTracers(this, mc));
        this.modules.add(new ModuleTrajectories(this, mc));
        this.modules.add(new ModuleWaifuESP(this, mc));
        this.modules.add(new ModuleYaw(this, mc));





        for(ModuleBase m : modules)
        {
            // maybe put names and aliases in module cache?
            moduleCache.put(m.getName().toLowerCase(), m);
            moduleNameCache.put(m.getClass().getSimpleName(), m.getName().toLowerCase());

            m.loadModule();
        }

        events = new Events(this);

        new StaticNova(this);
    }

    public <T extends ModuleBase> T getModule(Class<T> type) {
        return type.cast( moduleCache.get(moduleNameCache.get(type.getSimpleName())));
    }

    public void notificationMessage(String msg)
    {
        if ((getModule(ModuleGui.class)).isHidden)
            this.message("\247l>>\247r " + msg);
        else
            getModule(ModuleGui.class).addToQueue(msg);
    }

    public void errorMessage(String s) {
        message("\247c[NV]\247r" + s);
    }

    public void confirmMessage(String s) { message("\247b[NV]\247r " + s); }

    public void message(String s){
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(s));
    }


}
