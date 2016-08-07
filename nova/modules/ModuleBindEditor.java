package nova.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import nova.Nova;
import nova.core.Util;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;
import nova.Command;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Skeleton Man on 6/24/2016.
 */
public class ModuleBindEditor extends ModuleBase{
    boolean keys[];
    int key;

    @Expose
    public Map<Integer, String> binds;

    public ModuleBindEditor(Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);

        this.isToggleable = false;
        aliases.add("bind");
        aliases.add("binds");

        this.command = new Command(Nova, this, aliases, "Binds command sets to keys. Separate commands with pipes, and surround the whole set with quotes if it includes spaces. (ex. markers; or fly|freecam; or \"fly 0.065|bright -0.025\"");
        this.command.registerArg("add", this.getClass().getMethod("putBind", String.class, String.class), "(key) (command) | adds or replaces a keybind (ex. binds add j brightness)");
        this.defaultArg = "add";

        this.command.registerArg("del", this.getClass().getMethod("deleteBind", String.class), "(key) | removes a keybind");

        keys = new boolean[256];

        binds = new HashMap<Integer, String>();

        loadModule();
    }


    @Override
    public void saveModule(){
        json.add("binds", Util.getGson().toJsonTree(binds));
        super.saveModule();
    }

    @Override
    public void load(){
        super.load();
        binds = Util.getGson().fromJson(json.get("binds"), new TypeToken<HashMap<Integer, String>>(){}.getType());
    }

    public int stringToKey(String key)
    {
        return Keyboard.getKeyIndex(key.substring(0, 1).toUpperCase());
    }



    public boolean keyDown(int k)
    {
        if(mc.currentScreen != null && !(mc.currentScreen instanceof GuiChest))
            return false;

        if(Keyboard.isKeyDown(k) != keys[k])
            return keys[k] = !keys[k];
        else
            return false;


    }

    @EventHandler
    public void onTick(PlayerTickEvent e){
        for(int i : binds.keySet())
        {
            if(this.keyDown(i))
            {
                for(String c : binds.get(i).split("\\|"))
                {
                    Nova.events.onCommand(c);
                }
            }
        }
    }

    public void putBind(String key, String command)
    {
        this.binds.put(this.stringToKey(key), command);
        Nova.confirmMessage("Put bind for key: " + this.stringToKey(key) + "; " + command);
    }

    public void deleteBind(String key)
    {
        this.binds.remove(this.stringToKey(key));
        Nova.confirmMessage("Deleted bind for key: " + this.stringToKey(key));

    }

}
