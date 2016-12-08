package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;

/**
 * Created by Skeleton Man on 12/8/2016.
 */
public class ModuleCameraClip extends ModuleBase {

    public ModuleCameraClip(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Clips the third person camera");
    }
}
