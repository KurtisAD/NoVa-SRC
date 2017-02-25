package nova.module.modules;

import nova.module.ModuleBase;

/**
 * Created by Skeleton Man on 2/2/2017.
 */
public class ModuleNoHurtCam extends ModuleBase {
    public ModuleNoHurtCam() {
        this.description = "Disables hurt effects";
    }

    @Override
    public String getMetadata() {
        return mc.player.isBurning() ? "(\247cON FIRE\247r)" : "";
    }
}
