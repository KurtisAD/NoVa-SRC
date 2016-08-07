package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.Nova;
import nova.core.Util;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleGlide extends ModuleBase {
    double speed;

    public ModuleGlide(Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);

        this.speed = -0.17D;

        this.command = new Command(Nova, this, aliases, "Slows fall speed; NCP patched (?)");
        this.command.registerArg("speed", this.getClass().getMethod("setSpeed", double.class), "Sets the vertical speed");
        this.defaultArg = "speed";

        loadModule();
    }


    public void onTick(PlayerTickEvent e)
    {
        if(this.isEnabled) {
            if(!mc.thePlayer.onGround && mc.thePlayer.motionY < 0.0D && mc.thePlayer.isAirBorne && !mc.thePlayer.isInWater())
            {
                mc.thePlayer.motionY = speed;
            }
        }

    }

    @Override
    public void saveModule(){
        json.add("speed", Util.getGson().toJsonTree(speed));
        super.saveModule();
    }

    @Override
    public void load(){
        super.load();
        speed = Util.getGson().fromJson(json.get("speed"),double.class);
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }

    @Override
    public String getMetadata()
    {
        return this.speed == 0.05F ? "" :  "(" + Double.toString(speed) + ")";
    }
}
