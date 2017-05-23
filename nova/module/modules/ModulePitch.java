package nova.module.modules;

import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;

/**
 * @author Kurt Dee
 * @since 5/19/2017
 */
public class ModulePitch extends ModuleBase {
    @Saveable
    float pitch;

    public ModulePitch() {
        super();
        this.description = ("Locks your pitch to the saved setting");

        this.defaultArg = "deg";

        this.pitch = 0F;
    }

    @RegisterArgument(name = "deg", description = "Sets the pitch degree to input")
    public void deg(float deg) {
        this.pitch = deg;
        mc.player.rotationPitch = this.pitch;
    }

    @RegisterArgument(name = "set", description = "Sets the pitch to current pitch")
    public void set() {
        this.pitch = mc.player.rotationPitch;
    }

    @EventHandler
    public void onTick(PlayerTickEvent e) {
        if (this.isEnabled) {
            mc.player.rotationPitch = this.pitch;
        }

    }


    @Override
    public String getMetadata() {
        return "(" + String.format("%.1f", this.pitch) + ")";
    }

}
