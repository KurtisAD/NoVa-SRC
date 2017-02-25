package nova;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import nova.event.Events;
import nova.module.ModuleBase;
import nova.module.modules.*;
import nova.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kurt Dee
 * @since 6/18/2016
 */

public class Nova {
    // TODO: implement enabled event; adding an event type that checks if module is enabled? Similar to implementing pre/post events?
    // TODO: consider adding modules via reflection
    // TODO: Document more and more (For util and non-annotated methods in modules)
    // TODO: make annotations throw errors
    // TODO: explore removing module constructors if unneeded(including removing description from the constructor)
    // I swear to fuck I add more todo's than I solve

    private static Minecraft mc;
    private static Events events;
    private static ArrayList<ModuleBase> modules;
    private static Map<Class<? extends ModuleBase>, ModuleBase> moduleCache;
    private static Map<String, Class<? extends ModuleBase>> moduleAliasCache;

    public static final String delimiter = "-";

    public static final String Version = "NoVa 11.2.a16 Optifine B5";
    /**
     * NovaClient file directory ".minecraft/Nova/"
     */
    public static File novaDir = new File(Util.getAppDir("minecraft") + File.separator + "Nova");

    static {
        mc = Minecraft.getMinecraft();

        if (!novaDir.exists()) {
            novaDir.mkdirs();
        }

        modules = new ArrayList<>();
        moduleCache = new HashMap<>();
        moduleAliasCache = new HashMap<>();


        //Add Modules here
        //Format:            this.modules.add(new ModuleBase());


        modules.add(new ModuleAntiAfk());
        modules.add(new ModuleAntifall());
        modules.add(new ModuleAutoArmor());
        modules.add(new ModuleAutoEat());
        modules.add(new ModuleAutoFish());
        modules.add(new ModuleAutoMine());
        modules.add(new ModuleAutoRespawn());
        //this.modules.add(new ModuleAutotool());
        modules.add(new ModuleAutowalk());
        modules.add(new ModuleBindEditor());
        //this.modules.add(new ModuleBlink());
        modules.add(new ModuleBrightness());
        modules.add(new ModuleCameraClip());
        modules.add(new ModuleElytraFly());
        modules.add(new ModuleEncryption());
        modules.add(new ModuleEntityRide());
        modules.add(new ModuleESP());
        //this.modules.add(new ModuleExtraElytra());
        //this.modules.add(new ModuleFakeCoord());
        modules.add(new ModuleFastBreak());
        modules.add(new ModuleFly());
        modules.add(new ModuleFreecam());
        modules.add(new ModuleFriends());
        modules.add(new ModuleGlide());
        modules.add(new ModuleGreet());
        modules.add(new ModuleGui());
        modules.add(new ModuleHelp());
        modules.add(new ModuleInfo());
        modules.add(new ModuleIntervalThrow());
        modules.add(new ModuleJesus());
        modules.add(new ModuleMarkers());
        modules.add(new ModuleNames());
        modules.add(new ModuleNoclip());
        modules.add(new ModuleNoHurtCam());
        modules.add(new ModuleNofall());
        modules.add(new ModuleNoKnockback());
        modules.add(new ModuleNoRender());
        modules.add(new ModuleNoslow());
        modules.add(new ModuleNotifications());
        modules.add(new ModulePeek());
        modules.add(new ModuleSafewalk());
        modules.add(new ModuleSay());
        modules.add(new ModuleSpeed());
        modules.add(new ModuleSprint());
        modules.add(new ModuleTextwidth());
        modules.add(new ModuleTimer());
        modules.add(new ModuleTracers());
        modules.add(new ModuleTrajectories());
        modules.add(new ModuleWaifuESP());
        modules.add(new ModuleYaw());


        for(ModuleBase m : modules)
        {
            // maybe put names and aliases in module cache?
            moduleCache.put(m.getClass(), m);

            for (String alias : m.getAliases()) {
                moduleAliasCache.put(alias, m.getClass());
            }

            m.loadModule();
        }

        events = new Events();

    }

    /**
     * Returns the Nova module instance
     *
     * @param type The class of the module instance requested
     * @return The module instance of the class type
     */
    public static <T extends ModuleBase> T getModule(Class<T> type) {
        return type.cast(moduleCache.get(type));
    }

    public static Events getEvents() {
        return events;
    }

    public static ArrayList<ModuleBase> getModules() {
        return modules;
    }

    public static Map<Class<? extends ModuleBase>, ? extends ModuleBase> getModuleCache() {
        return moduleCache;
    }

    public static Map<String, Class<? extends ModuleBase>> getModuleAliasCache() {
        return moduleAliasCache;
    }

    public static boolean onEvent(Object o) {
        return events.onEvent(o);
    }

    public static void notificationMessage(String msg)
    {
        if ((getModule(ModuleGui.class)).isHidden)
            message("\247l>>\247r " + msg);
        else
            getModule(ModuleGui.class).addToQueue(msg);
    }

    public static void errorMessage(String s) {
        message("\247c[NV]\247r" + s);
    }

    public static void confirmMessage(String s) {
        message("\247b[NV]\247r " + s);
    }

    public static void message(String s) {
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(s));
    }


}
