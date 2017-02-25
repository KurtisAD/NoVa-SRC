package nova.module.modules;

import nova.event.RegisterArgument;
import nova.module.ModuleBase;
import nova.saver.Saveable;

/**
 * Created by Skeleton Man on 12/11/2016.
 */
public class ModuleTimer extends ModuleBase { // TODO: fix the timer setting issues when disabled

    @Saveable
    public float speed;

    public ModuleTimer() {
        super();
        this.description = ("Changes client tick speed");

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
