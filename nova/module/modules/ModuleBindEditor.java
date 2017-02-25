package nova.module.modules;

import net.minecraft.client.gui.inventory.GuiChest;
import nova.Nova;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Skeleton Man on 6/24/2016.
 */
public class ModuleBindEditor extends ModuleBase {
    // TODO: Add numpad keys

    public boolean keys[];

    @Saveable
    public Map<Integer, String> binds;

    public ModuleBindEditor() {
        super();

        this.isToggleable = false;
        aliases.add("bind");
        aliases.add("binds");

        this.description = ("Binds command sets to keys. Separate commands with pipes, and surround the whole set with quotes if it includes spaces. (ex. markers; or fly|freecam; or \"fly 0.065|bright -0.025\"");
        this.defaultArg = "add";


        keys = new boolean[256];

        binds = new HashMap<>();
    }


    private int stringToKey(String key)
    {
        return Keyboard.getKeyIndex(key.substring(0, 1).toUpperCase());
    }


    private boolean keyDown(int k) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChest))
            return false;

        return Keyboard.isKeyDown(k) != keys[k] && (keys[k] = !keys[k]);

    }

    @EventHandler
    public void onTick(PlayerTickEvent e){
        for(int i : binds.keySet())
        {
            if(this.keyDown(i))
            {
                for(String c : binds.get(i).split("\\|"))
                {
                    Nova.getEvents().onCommand(c);
                }
            }
        }
    }

    @RegisterArgument(name = "add", description = "(key) (command) | adds or replaces a keybind (ex. binds add j brightness)")
    public void putBind(String key, String command) {
        this.binds.put(this.stringToKey(key), command);
        Nova.confirmMessage("Put bind for key: " + this.stringToKey(key) + "; " + command);
    }

    @RegisterArgument(name = "del", description = "(key) | removes a keybind")
    public void deleteBind(String key) {
        this.binds.remove(this.stringToKey(key));
        Nova.confirmMessage("Deleted bind for key: " + this.stringToKey(key));

    }

}
