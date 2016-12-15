package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.IModule;
import nova.Nova;
import nova.Saver;
import nova.core.Saveable;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Skeleton Man on 6/18/2016.
 */
public class ModuleBase implements IModule {

    Nova Nova;
    Minecraft mc;

    public ArrayList<String> aliases;
    public String name;
    public String defaultArg;
    public Command command;

    @Saveable
    public boolean isEnabled;

    public boolean showEnabled;

    // If false, this will essentially just act as a command.
    public boolean isToggleable;


    public String filepath;


    // TODO: fix module shit so it loads on declaration, maybe a check and set function for nulls?
    // TODO: a lot of modules have public fields, need to privatize them
    public ModuleBase(Nova Nova, Minecraft mc) {
        this.Nova = Nova;
        this.isEnabled = false;
        this.mc = mc;
        this.isToggleable = true;
        this.defaultArg = "";

        aliases = new ArrayList<String>();

        this.command = new Command(Nova, this, aliases);
        showEnabled = true;

        this.name = this.getClass().getSimpleName().replaceFirst("Module", "").toLowerCase();
        aliases.add(name);

        this.filepath = Nova.novaDir + File.separator + this.name + ".nova";
    }

    public void onEnable() {
        this.isEnabled = true;
    }


    public void onDisable() {
        this.isEnabled = false;
    }

    public void toggleState() {
        if (this.isToggleable) {
            if (this.isEnabled)
                this.onDisable();
            else
                this.onEnable();
        }
    }

    public String getName() {
        return this.name;
    }

    public String getMetadata() {
        return "";
    }


    public void saveModule() {
        Saver.saveModule(this);
    }

    public void loadModule() {
        Saver.loadModule(this);
    }


    // Work around for the Saver in saveModule()
    // m.getClass().getFields() will return only fields of ModuleBase, we need the subclass fields as well
    // TODO: maybe better implement it?
    public Field[] getFields() {
        return this.getClass().getFields();
    }
}
