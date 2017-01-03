package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;

/**
 * Created by Skeleton Man on 1/2/2017.
 */

// Hook in move() in Entity
public class ModuleSafewalk extends ModuleBase {
    public ModuleSafewalk(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Prevents you from falling, as if you were sneaking");
    }
}
