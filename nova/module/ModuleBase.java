package nova.module;

import net.minecraft.client.Minecraft;
import nova.Nova;
import nova.event.Command;
import nova.saver.Saveable;
import nova.saver.Saver;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Kurt Dee
 * @since 6/18/2016
 */
public class ModuleBase implements IModule {
    // TODO: make more fields protected for encapsulation
    // TODO: create another base class for modules with similar functionality (e.g. entity selection)

    protected static final Minecraft mc = Minecraft.getMinecraft();

    protected ArrayList<String> aliases;
    public final String name;
    public String defaultArg;
    public Command command;
    protected String description = "No description available.";

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

}
