package nova.module.modules;

import net.minecraft.entity.Entity;
import nova.event.RegisterArgument;
import nova.module.ModuleBase;

/**
 * @author Kurt Dee
 * @since 3/22/2017
 */
public class ModuleTeleport extends ModuleBase {
    public ModuleTeleport() {
        this.description = "Teleports you to coordinates specified";
        this.isToggleable = false;

        aliases.add("tp");

        this.defaultArg = "to";
    }

    @RegisterArgument(name = "to", description = "Teleports to exact coordinates")
    public void tpTo(float x, float y, float z) {
        Entity toTeleport = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

        toTeleport.setPosition(x, y, z);

    }

    @RegisterArgument(name = "rto", description = "Teleportes to relative coordinates")
    public void rtpTo(float x, float y, float z) {
        Entity toTeleport = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

        toTeleport.setPosition(toTeleport.posX + x, toTeleport.posY + y, toTeleport.posZ + z);

    }
}
