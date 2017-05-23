package nova.module.modules;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3d;
import nova.Nova;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.LivingUpdateEvent;
import nova.event.events.PlayerMoveEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;

/**
 * Created by Skeleton Man on 1/19/2017.
 */
public class ModuleElytraFly extends ModuleBase {
    // TODO: implement with normal fly acceleration maybe? a mode
    // TODO: implement flat move mode
    // TODO: make starting fly module seperate

    @Saveable
    public float speed;

    public ModuleElytraFly() {
        super();

        this.defaultArg = "speed";
        this.speed = 1F;

    }

    @RegisterArgument(name = "speed", description = "changes speed")
    public void setSpeed(float speed) {
        this.speed = speed;
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (this.isEnabled && mc.player.isElytraFlying()) {
            e.getMovePointer()[1] = 0D;

            float strafe = mc.gameSettings.keyBindLeft.pressed ? speed : 0;
            strafe -= mc.gameSettings.keyBindRight.pressed ? speed : 0;
            float forward = mc.gameSettings.keyBindForward.pressed ? speed : 0;
            forward -= mc.gameSettings.keyBindBack.pressed ? speed : 0;

            Vec3d playerVector = new Vec3d(strafe, 0, forward);
            playerVector = playerVector.rotatePitch(-mc.player.rotationPitch * 0.017453292F);
            playerVector = playerVector.rotateYaw(-mc.player.rotationYaw * 0.017453292F);

            e.getMovePointer()[0] = playerVector.xCoord;
            e.getMovePointer()[1] = playerVector.yCoord;
            e.getMovePointer()[2] = playerVector.zCoord;
        }


    }

    @EventHandler
    public void onPlayerUpdate(LivingUpdateEvent e) {
        if (this.isEnabled && !mc.player.isElytraFlying()) {
            mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }

    @Override
    public void onEnable() {
        Nova.getModule(ModuleFly.class).onDisable();

        super.onEnable();
    }

    @Override
    public String getMetadata() {
        return (mc.player.isElytraFlying() ? "(Flying " : "(Ready ") + "| Speed: " + Float.toString(speed) + ")";
    }
}
