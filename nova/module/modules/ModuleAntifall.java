package nova.module.modules;

import nova.Nova;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;

/**
 * Created by Skeleton Man on 12/9/2016.
 */
public class ModuleAntifall extends ModuleBase {
    @Saveable
    public float tolerance;

    // TODO: maybe implement using NaN or spamming teleport packets?
    public ModuleAntifall() {
        super();

        this.description = "Catches you after you've fallen a certain distance by turning on fly, designed for NCP servers";
        this.defaultArg = "tolerance";
        this.tolerance = 5.0F;
    }

    @RegisterArgument(name = "tolerance", description = "how far you fall before fly is toggled")
    public void setTolerance(float tolerance) {
        this.tolerance = tolerance;
    }

    @EventHandler
    public void onTick(PlayerTickEvent e) {
        if (this.isEnabled) {
            if (mc.player.fallDistance > tolerance) {
                Nova.getModule(ModuleFly.class).onEnable();
            }
        }
    }

    @Override
    public String getMetadata() {
        return this.tolerance == 5.0F ? "" : ("(" + Float.toString(this.tolerance) + ")");
    }
}
