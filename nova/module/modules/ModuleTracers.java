package nova.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import nova.Nova;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.EntityRenderTickEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;
import nova.util.Util;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collection;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Kurt Dee
 * @since 12/9/2016
 */
public class ModuleTracers extends ModuleBase {
    // TODO: add color selection
    // TODO: add setting for friends

    @Saveable
    public Map<String, Color> traced;


    public ModuleTracers() {
        super();

        this.aliases.add("tracer");

        this.description = ("Draws a line to entities");
    }

    @RegisterArgument(name = "valid", description = "Lists valid entites")
    public void valid() {
        Nova.confirmMessage("You can select:");
        Nova.message(Util.join((Collection<String>) Util.getValidEntities().keySet(), ", "));
    }

    @RegisterArgument(name = "new", description = "Adds entity class to tracers with default color, white")
    public void add(String name) {
        if (Util.getValidEntities().containsKey(name.toLowerCase())) {
            this.traced.put(name.toLowerCase(), new Color(255, 255, 255));
            Nova.confirmMessage("Added " + name + " to tracers");
        } else {
            Nova.errorMessage("You cannot trace that entity; check -tracers valid");
        }
    }

    @RegisterArgument(name = "add", description = "Add entity to tracers with R G B colors")
    public void addrgb(String name, int r, int g, int b) {
        if (Util.getValidEntities().containsKey(name.toLowerCase())) {
            this.traced.put(name.toLowerCase(), new Color(r, g, b));
            Nova.confirmMessage("Added " + name + " to tracers");
        } else {
            Nova.errorMessage("You cannot trace that entity; check -tracers valid");
        }
    }

    @RegisterArgument(name = "del", description = "Removes entity from tracers")
    public void del(String name) {
        if (this.traced.containsKey(name.toLowerCase())) {
            this.traced.remove(name.toLowerCase());
            Nova.confirmMessage("Deleted " + name);
        } else {
            Nova.errorMessage("That entity has not been added");
        }
    }

    @RegisterArgument(name = "list", description = "Lists traced entities")
    public void list() {
        String color = "";
        for (String s : this.traced.keySet()) {

            Color rgb = this.traced.get(s);
            int r, g, b;
            r = rgb.getRed();
            g = rgb.getGreen();
            b = rgb.getBlue();

            color = "(" + r + ", " + b + ", " + g + ")";
            Nova.message(s + ": " + color);
        }
        Nova.message("End of tracers list.");

    }

    @RegisterArgument(name = "rgb", description = "Changes the color of a tracer, insert RGB from 0-255")
    public void rgb(String name, int r, int g, int b) {
        if (this.traced.containsKey(name.toLowerCase())) {
            this.traced.put(name.toLowerCase(), new Color(r, g, b));
            Nova.confirmMessage("Changed color of " + name);
        } else {
            Nova.errorMessage("That entity has not been added");
        }
    }

    @EventHandler
    public void onEntityRenderTick(EntityRenderTickEvent e) {
        if (this.isEnabled) {
            for (Entity entity : mc.world.getLoadedEntityList()) {
                if (this.traced.containsKey(Util.getEntityName(entity.getClass()))) {
                    drawTracers(entity, traced.get(Util.getEntityName(entity.getClass())));
                }
            }
        }
    }

    private void drawTracers(Entity e, Color c) {
        Vec3d eyes = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(mc.player.rotationPitch)).rotateYaw(-(float) Math.toRadians(mc.player.rotationYaw));

        double x = e.posX
                - Minecraft.getMinecraft().getRenderManager().renderPosX;
        double y = e.posY + e.height / 2
                - Minecraft.getMinecraft().getRenderManager().renderPosY;
        double z = e.posZ
                - Minecraft.getMinecraft().getRenderManager().renderPosZ;
        glBlendFunc(770, 771);
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_LINE_SMOOTH);
        glDepthMask(false);


        glLineWidth(3.0F);
        glColor3d(c.getRed() / 255D, c.getGreen() / 255D, c.getBlue() / 255D);
        glBegin(GL_LINES);
        {
            glVertex3d(eyes.xCoord, mc.player.getEyeHeight() + eyes.yCoord, eyes.zCoord);
            glVertex3d(x, y, z);
        }
        glEnd();

        glDisable(GL_LINE_SMOOTH);
        glEnable(GL11.GL_TEXTURE_2D);
        glEnable(GL11.GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

}
