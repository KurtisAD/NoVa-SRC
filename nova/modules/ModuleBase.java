package nova.modules;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.Minecraft;
import nova.Nova;
import nova.Command;
import nova.IModule;
import nova.core.Util;

import java.io.*;
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

    public boolean isEnabled;

    public boolean showEnabled;

    // If false, this will essentially just act as a command.
    public boolean isToggleable;


    JsonObject json;
    public String filepath;





    public ModuleBase(Nova Nova, Minecraft mc) throws NoSuchMethodException{
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
        json = new JsonObject();
    }

    public void onEnable()
    {
        this.isEnabled = true;
    }


    public void onDisable()
    {
        this.isEnabled = false;
    }

    public void toggleState() {
        if(this.isToggleable){
            if(this.isEnabled)
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


    public void saveModule(){
        json.add("isEnabled", Util.getGson().toJsonTree(isEnabled));
        saveJson();
    }
    public void loadModule(){
        try {
            load();
        } catch (NullPointerException e){
            saveModule();
        }
    }

    protected void load(){
        loadJson();
        isEnabled = Util.getGson().fromJson(json.get("isEnabled"), boolean.class);
    }

    private void saveJson(){
        FileWriter file;
        try {
            file = new FileWriter(filepath);
            file.write(Util.getGson().toJson(json));
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadJson(){
        try {
            json = Util.getGson().fromJson(new JsonReader(new FileReader(filepath)), JsonObject.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            saveModule();
        }
    }
}
