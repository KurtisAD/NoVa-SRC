package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.Nova;
import nova.core.Util;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleHorseRide extends ModuleBase{
    boolean horseJump;
    public ModuleHorseRide(Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);

        this.aliases.add("horse");
        this.command = new Command(Nova, this, aliases, "Can ride all horses; ALPHA MODULE");
        this.command.registerArg("jump",this.getClass().getMethod("toggleJump"), "Max jump everytime when jumping on horses.");

        loadModule();
    }
    // Information is in EntityHorse.isTame()
    @Override
    public void load(){
        super.load();
        horseJump = Util.getGson().fromJson(json.get("horseJump"), boolean.class);
    }

    @Override
    public void saveModule(){
        json.add("horseJump",Util.getGson().toJsonTree(horseJump));
        super.saveModule();
    }

    public void toggleJump(){
        this.horseJump = !this.horseJump;
    }

    public void onTick(PlayerTickEvent e)
    {
        if(this.isEnabled && horseJump) {
            mc.thePlayer.horseJumpPower = 1.0F;
        }
    }

    @Override
    public String getMetadata(){
        return horseJump ? "(Jump)" : "";
    }
}
