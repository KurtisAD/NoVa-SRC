package nova.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import nova.event.EventHandler;
import nova.event.events.EntityRenderTickEvent;
import nova.module.ModuleBase;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;


/**
 * Created by Skeleton Man on 1/2/2017.
 */
public class ModuleTrajectories extends ModuleBase {
    public ModuleTrajectories() {
        super();

        this.description = ("Draws trajectories for projectiles. From Wurst");
    }

    @EventHandler
    public void onRenderTick(EntityRenderTickEvent e) {
        if (this.isEnabled) {
            // check if player is holding item
            ItemStack stack = mc.player.inventory.getCurrentItem();
            if (stack == null)
                return;

            // check if item is throwable
            Item item = stack.getItem();
            if (!(item instanceof ItemBow || item instanceof ItemSnowball
                    || item instanceof ItemEgg || item instanceof ItemEnderPearl
                    || item instanceof ItemSplashPotion
                    || item instanceof ItemLingeringPotion
                    || item instanceof ItemFishingRod))
                return;

            boolean usingBow =
                    mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow;

            // calculate starting position
            double arrowPosX = mc.player.lastTickPosX
                    + (mc.player.posX - mc.player.lastTickPosX) * mc.timer.renderPartialTicks
                    - MathHelper.cos((float) Math.toRadians(mc.player.rotationYaw)) * 0.16F;
            double arrowPosY = mc.player.lastTickPosY
                    + (mc.player.posY - mc.player.lastTickPosY)
                    * Minecraft.getMinecraft().timer.renderPartialTicks
                    + mc.player.getEyeHeight() - 0.1;
            double arrowPosZ = mc.player.lastTickPosZ
                    + (mc.player.posZ - mc.player.lastTickPosZ)
                    * Minecraft.getMinecraft().timer.renderPartialTicks
                    - MathHelper.sin((float) Math.toRadians(mc.player.rotationYaw)) * 0.16F;

            // calculate starting motion
            float arrowMotionFactor = usingBow ? 1F : 0.4F;
            float yaw = (float) Math.toRadians(mc.player.rotationYaw);
            float pitch = (float) Math.toRadians(mc.player.rotationPitch);
            float arrowMotionX =
                    -MathHelper.sin(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
            float arrowMotionY = -MathHelper.sin(pitch) * arrowMotionFactor;
            float arrowMotionZ =
                    MathHelper.cos(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
            double arrowMotion = Math.sqrt(arrowMotionX * arrowMotionX
                    + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
            arrowMotionX /= arrowMotion;
            arrowMotionY /= arrowMotion;
            arrowMotionZ /= arrowMotion;
            if (usingBow) {
                float bowPower = (72000 - mc.player.getItemInUseCount()) / 20F;
                bowPower = (bowPower * bowPower + bowPower * 2F) / 3F;

                if (bowPower > 1F)
                    bowPower = 1F;

                if (bowPower <= 0.1F)
                    bowPower = 1F;

                bowPower *= 3F;
                arrowMotionX *= bowPower;
                arrowMotionY *= bowPower;
                arrowMotionZ *= bowPower;
            } else {
                arrowMotionX *= 1.5D;
                arrowMotionY *= 1.5D;
                arrowMotionZ *= 1.5D;
            }

            // GL settings
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glEnable(GL13.GL_MULTISAMPLE);
            GL11.glDepthMask(false);
            GL11.glLineWidth(1.8F);

            RenderManager renderManager = mc.getRenderManager();

            // draw trajectory line
            double gravity = usingBow ? 0.05D : item instanceof ItemPotion ? 0.4D
                    : item instanceof ItemFishingRod ? 0.15D : 0.03D;
            Vec3d playerVector = new Vec3d(mc.player.posX,
                    mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
            GL11.glColor3d(0, 1, 0);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (int i = 0; i < 1000; i++) {
                GL11.glVertex3d(arrowPosX - renderManager.renderPosX,
                        arrowPosY - renderManager.renderPosY,
                        arrowPosZ - renderManager.renderPosZ);

                arrowPosX += arrowMotionX * 0.1;
                arrowPosY += arrowMotionY * 0.1;
                arrowPosZ += arrowMotionZ * 0.1;
                arrowMotionX *= 0.999D;
                arrowMotionY *= 0.999D;
                arrowMotionZ *= 0.999D;
                arrowMotionY -= gravity * 0.1;

                if (mc.world.rayTraceBlocks(playerVector,
                        new Vec3d(arrowPosX, arrowPosY, arrowPosZ)) != null)
                    break;
            }
            GL11.glEnd();

            // draw end of trajectory line
            double renderX = arrowPosX - renderManager.renderPosX;
            double renderY = arrowPosY - renderManager.renderPosY;
            double renderZ = arrowPosZ - renderManager.renderPosZ;
            AxisAlignedBB bb = new AxisAlignedBB(renderX - 0.5, renderY - 0.5,
                    renderZ - 0.5, renderX + 0.5, renderY + 0.5, renderZ + 0.5);
            GL11.glColor4f(0F, 1F, 0F, 0.15F);
            drawColorBox(bb, 0F, 1F, 0F, 0.15F);
            GL11.glColor4d(0, 0, 0, 0.5F);
            drawSelectionBoundingBox(bb);

            // GL resets
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDisable(GL13.GL_MULTISAMPLE);
            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glPopMatrix();
        }
    }

    public static void drawColorBox(AxisAlignedBB axisalignedbb, float red,
                                    float green, float blue, float alpha) {
        Tessellator ts = Tessellator.getInstance();
        VertexBuffer vb = ts.getBuffer();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts X.
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        ts.draw();// Ends X.
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts Y.
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        ts.draw();// Ends Y.
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts Z.
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        ts.draw();// Ends Z.
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
                .endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
                .endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ)
                .endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ)
                .endVertex();
        tessellator.draw();
    }
}
