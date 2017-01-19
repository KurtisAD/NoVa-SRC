package nova.modules;

import net.minecraft.client.Minecraft;

/**
 * Created by Skeleton Man on 12/20/2016.
 */
public class ModuleNoclip extends ModuleBase {
    // All logic done elsewhere
    public ModuleNoclip(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.description = ("Enables noclip");
    }
}
