package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.core.RegisterArgument;
import nova.core.Saveable;

/**
 * Created by Skeleton Man on 12/11/2016.
 */
public class ModuleTimer extends ModuleBase {

    @Saveable
    public float speed;

    public ModuleTimer(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);
        this.command = new Command(Nova, this, aliases, "Changes client tick speed");

        this.speed = 1.0f;
        this.defaultArg = "set";
    }

    @RegisterArgument(name = "set", description = "Changes tick speed")
    public void setSpeed(float speed) {
        this.speed = speed;
        mc.timer.timerSpeed = speed;
    }


    @Override
    public String getMetadata() {
        return this.speed == 1.0F ? "(Ready)" : "(" + Float.toString(this.speed) + ")";
    }
}
