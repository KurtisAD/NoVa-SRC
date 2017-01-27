package nova.modules;

import nova.Nova;
import nova.core.EventHandler;
import nova.core.RegisterArgument;
import nova.core.Saveable;
import nova.events.PlayerMoveEvent;

/**
 * Created by Skeleton Man on 1/4/2017.
 */
public class ModuleSpeed extends ModuleBase {


    @Saveable
    public float speed;

    public ModuleSpeed() {
        super();

        this.description = ("Modifies your movement speed");

        this.speed = 1.3f;

        this.defaultArg = "set";
    }

    @Override
    public void onEnable() {
        if (Nova.getModule(ModuleFly.class).isEnabled) {
            Nova.errorMessage("Cannot enable speed while fly is enabled");
            return;
        }
        super.onEnable();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (this.isEnabled) {
            double movePointer[] = e.getMovePointer();

            movePointer[0] *= speed;
            movePointer[2] *= speed;
        }
    }

    @RegisterArgument(name = "set", description = "sets the speed")
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public String getMetadata() {
        return "(" + Float.toString(speed) + ")";
    }
}
