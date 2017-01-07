package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.core.RegisterArgument;
import nova.core.Saveable;

/**
 * Created by Skeleton Man on 1/4/2017.
 */
public class ModuleSpeed extends ModuleBase {


    @Saveable
    public float speed;

    public ModuleSpeed(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Modifies your movement speed");

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

    @RegisterArgument(name = "set", description = "sets the speed")
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public String getMetadata() {
        return "(" + Float.toString(speed) + ")";
    }
}
