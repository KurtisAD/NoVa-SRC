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
    // TODO: make more fields protected for encapsulation

    // mc is a pointer, which is just easier to use than Minecraft.getMinecraft();
    protected static final Minecraft mc = Minecraft.getMinecraft();

    protected ArrayList<String> aliases;
    public final String name;
    public String defaultArg;
    public Command command;
    protected String description;

    @Saveable
    public boolean isEnabled;

    public boolean showEnabled;

    // If false, this will essentially just act as a command.
    public boolean isToggleable;

    public String filepath;


    public ModuleBase() {
        this.isEnabled = false;
        this.isToggleable = true;
        this.defaultArg = "";

        this.description = "No description available.";
        this.aliases = new ArrayList<>();

        this.command = new Command(this);
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

    @Override
    public ArrayList<String> getAliases() {
        return this.aliases;
    }

    @Override
    public String getDescription() {
        return this.description;
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
