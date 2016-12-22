package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import nova.Command;
import nova.events.EventHandler;
import nova.events.LivingUpdateEvent;
import nova.events.PacketReceivedEvent;

import java.lang.reflect.Field;

/**
 * Created by Skeleton Man on 12/17/2016.
 */
public class ModuleFlyBypass extends ModuleBase {


    public ModuleFlyBypass(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Makes you fly, maybe can do more with this?");

    }

    @EventHandler
    public void updateMovement(LivingUpdateEvent e) {
        if (this.isEnabled) {
            double oldMotionX = mc.player.motionX;
            double oldMotionY = mc.player.motionY;
            double oldMotionZ = mc.player.motionZ;

            double[] dir = breakNCP(0);
            double xDir = dir[0];
            double zDir = dir[1];

            if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()) && !mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionX = xDir * 0.26;
                mc.player.motionZ = zDir * 0.26;
            }

            for (int hause = 0; hause <= 2; hause++) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY + (mc.gameSettings.keyBindJump.isKeyDown() ? (0.0625) : 0.00000001) - (mc.gameSettings.keyBindSneak.isKeyDown() ? (0.0625) : 0.00000002), mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, false));
            }
            mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, 1337 + mc.player.posY, mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, true));

            mc.player.motionX = oldMotionX;
            mc.player.motionY = oldMotionY;
            mc.player.motionZ = oldMotionZ;
        }
    }

    private double[] breakNCP(float yawOffset) {
        Minecraft mc = Minecraft.getMinecraft();
        float dir = mc.player.rotationYaw + yawOffset;
        dir += ((mc.player.moveStrafing < 0.0F) ? (90.0F * ((mc.player.moveForward > 0.0F) ? 0.5F : ((mc.player.moveForward < 0.0F) ? -0.5F : 1.0F))) : ((mc.player.moveStrafing > 0.0F) ? -(90.0F * (mc.player.moveForward > 0.0F ? 0.5F : mc.player.moveForward < 0.0F ? -0.5F : 1.0F)) : ((mc.player.moveForward < 0.0F) ? 180.0F : 0.0F)));

        float xD = (float) Math.cos((dir + 90.0F) * Math.PI / 180.0D);
        float zD = (float) Math.sin((dir + 90.0F) * Math.PI / 180.0D);

        return new double[]{xD, zD};
    }

    @EventHandler
    public void onPacketRecieve(PacketReceivedEvent event) {
        if (this.isEnabled) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
                setPrivateValue(SPacketPlayerPosLook.class, packet, mc.player.rotationYaw, "yaw");
                setPrivateValue(SPacketPlayerPosLook.class, packet, mc.player.rotationPitch, "pitch");
            }
        }
    }

    public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, String... fieldNames) {
        try {
            findField(classToAccess, fieldNames).set(instance, value);
        } catch (Exception e) {
            //throw new UnableToAccessFieldException(fieldNames, e);
        }
    }

    public static Field findField(Class<?> clazz, String... fieldNames) {
        Exception failed = null;
        for (String fieldName : fieldNames) {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (Exception e) {
                failed = e;
            }
        }
        return null;
        //throw new UnableToFindFieldException(fieldNames, failed);
    }
}
