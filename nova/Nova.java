package nova;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import nova.modules.*;
import nova.core.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Skeleton Man on 6/18/2016.
 */
public class Nova {
    // TODO: implement commands using annotations
    // TODO: implement enabled event

    public Minecraft mc;
    public Events events;
    public ArrayList<ModuleBase> modules;
    public Map<String, ModuleBase> moduleCache;
    public Map<String, String> moduleNameCache;

    public final String delimeter;


    public static final String Version = "NoVa 10.2.a1 Optifine C2";
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


        try{
            this.modules.add(new ModuleAntiAfk(this, mc));
            this.modules.add(new ModuleAutoArmor(this, mc));
            this.modules.add(new ModuleAutoEat(this, mc));
            this.modules.add(new ModuleAutoFish(this, mc));
            this.modules.add(new ModuleAutoMine(this, mc));
            this.modules.add(new ModuleAutoRespawn(this, mc));
            this.modules.add(new ModuleAutotool(this, mc));
            this.modules.add(new ModuleAutowalk(this, mc));
            this.modules.add(new ModuleBindEditor(this, mc));
            this.modules.add(new ModuleBlink(this, mc));
            this.modules.add(new ModuleBrightness(this, mc));
            this.modules.add(new ModuleEncryption(this, mc));
            this.modules.add(new ModuleESP(this, mc));
            this.modules.add(new ModuleFakeCoord(this, mc));
            this.modules.add(new ModuleFly(this, mc));
            this.modules.add(new ModuleFreecam(this, mc));
            this.modules.add(new ModuleFriends(this, mc));
            this.modules.add(new ModuleGlide(this, mc));
            this.modules.add(new ModuleGui(this, mc));
            this.modules.add(new ModuleHelp(this, mc));
            this.modules.add(new ModuleHorseRide(this, mc));
            this.modules.add(new ModuleImpersonate(this, mc));
            this.modules.add(new ModuleInfo(this, mc));
            this.modules.add(new ModuleIntervalThrow(this, mc));
            //this.modules.add(new ModuleMarkers(this, mc));
            this.modules.add(new ModuleNofall(this, mc));
            this.modules.add(new ModuleSay(this, mc));
            this.modules.add(new ModuleSprint(this, mc));
            this.modules.add(new ModuleTextwidth(this, mc));
            this.modules.add(new ModuleYaw(this, mc));



        } catch (NoSuchMethodException e){
            e.printStackTrace();
        }
        //End of Modules


        for(ModuleBase m : modules)
        {
            moduleCache.put(m.getName().toLowerCase(), m);
            moduleNameCache.put(m.getClass().getSimpleName(), m.getName().toLowerCase());
        }

        events = new Events(this);

        new StaticNova(this);
    }

    public <T extends ModuleBase> T getModule(Class<T> type) {
        return type.cast( moduleCache.get(moduleNameCache.get(type.getSimpleName())));
    }

    public void notificationMessage(String msg)
    {
        if(((ModuleGui)getModule(ModuleGui.class)).isHidden)
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
