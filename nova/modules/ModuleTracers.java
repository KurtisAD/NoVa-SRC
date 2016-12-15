package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import nova.Command;
import nova.events.EntityRenderTickEvent;
import nova.events.EventHandler;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Skeleton Man on 12/9/2016.
 */
public class ModuleTracers extends ModuleBase {
    // TODO: implement the generic version
    // this is just a shitty version for the youtube video

    ArrayList<String> friends;

    public ModuleTracers(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Draws a line to players");

        friends = new ArrayList<>();
        friends.add("Viooltje");
        friends.add("Tokerbuds");
        friends.add("nukkuh");
        friends.add("kinorana");
        friends.add("jman08");

    }

    @EventHandler
    public void onEntityRenderTick(EntityRenderTickEvent e) {
        if (this.isEnabled) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityPlayer && !((Entity) entity).getName().equals(mc.getSession().getUsername())) {

                    Vec3d eyes = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(mc.player.rotationPitch)).rotateYaw(-(float) Math.toRadians(mc.player.rotationYaw));

                    double x = entity.posX
                            - Minecraft.getMinecraft().getRenderManager().renderPosX;
                    double y = entity.posY + entity.height / 2
                            - Minecraft.getMinecraft().getRenderManager().renderPosY;
                    double z = entity.posZ
                            - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                    glBlendFunc(770, 771);
                    glEnable(GL_BLEND);
                    glLineWidth(2.0F);
                    glDisable(GL11.GL_TEXTURE_2D);
                    glDisable(GL_DEPTH_TEST);
                    glDepthMask(false);

                    if (friends.contains(entity.getName())) {
                        glColor3d(0, 0.75, 0.75);
                    } else {
                        glColor3d(1, 0, 0);
                    }
                    glBegin(GL_LINES);
                    {
                        glVertex3d(eyes.xCoord, mc.player.getEyeHeight() + eyes.yCoord, eyes.zCoord);
                        glVertex3d(x, y, z);
                    }
                    glEnd();
                    glEnable(GL11.GL_TEXTURE_2D);
                    glEnable(GL_DEPTH_TEST);
                    glDepthMask(true);
                    glDisable(GL_BLEND);
                }
            }
        }
    }
}
