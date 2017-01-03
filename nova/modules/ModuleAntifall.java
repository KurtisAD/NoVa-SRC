package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.core.RegisterArgument;
import nova.core.Saveable;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 12/9/2016.
 */
public class ModuleAntifall extends ModuleBase {
    @Saveable
    public float tolerance;


    public ModuleAntifall(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Catches you after you've fallen a certain distance by turning on fly, designed for NCP servers");
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
