package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.*;
import net.minecraft.util.math.Vec3d;
import nova.Command;
import nova.core.RegisterArgument;
import nova.core.Saveable;
import nova.events.EntityRenderTickEvent;
import nova.events.EventHandler;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Skeleton Man on 12/9/2016.
 */
public class ModuleTracers extends ModuleBase {
    // TODO: add color selection
    // TODO: test and debug
    // TODO: add setting for friends
    // TODO: FIX BECAUSE IT'S SUPER BROKEN

    Map<String, Class> validEntities;

    @Saveable
    public Map<String, Color> traced;


    public ModuleTracers(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Draws a line to players");
        validEntities = new HashMap<>();
        traced = new HashMap<>();
        addEntities();
    }

    @RegisterArgument(name = "valid", description = "Lists valid entites")
    public void valid() {
        String ret = "";
        for (String s : this.validEntities.keySet()) {
            ret += s + ", ";
        }

        this.Nova.confirmMessage("You can select:");
        this.Nova.message(ret.substring(0, ret.length() - 2));
    }

    @RegisterArgument(name = "add", description = "Adds entity class to tracers")
    public void add(String name) {
        if (validEntities.containsKey(name.toLowerCase())) {
            this.traced.put(name.toLowerCase(), new Color(255, 255, 255));
            this.Nova.confirmMessage("Added " + name + " to tracers");
        } else {
            this.Nova.errorMessage("You cannot trace that entity; check -tracers valid");
        }
    }

    @RegisterArgument(name = "del", description = "Removes entity from tracers")
    public void del(String name) {
        if (this.traced.containsKey(name.toLowerCase())) {
            this.traced.remove(name.toLowerCase());
            this.Nova.confirmMessage("Deleted " + name);
        } else {
            this.Nova.errorMessage("That entity has not been added");
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
            this.Nova.message(s + ": " + color);

        }

    }

    @RegisterArgument(name = "rgb", description = "Changes the color of a tracer, insert RGB from 0-255")
    public void rgb(String name, int r, int g, int b) {
        if (this.traced.containsKey(name.toLowerCase())) {
            this.traced.put(name.toLowerCase(), new Color(r, g, b));
            this.Nova.confirmMessage("Color of " + name + "Set to ");
        } else {
            this.Nova.errorMessage("That entity has not been added");
        }
    }

    @EventHandler
    public void onEntityRenderTick(EntityRenderTickEvent e) {
        if (this.isEnabled) {
            for (Entity entity : mc.world.getLoadedEntityList()) {
                if (this.traced.containsKey(getEntityName(entity.getClass()))) {
                    drawTracers(entity, traced.get(getEntityName(entity.getClass())));
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
        glLineWidth(2.0F);
        glDisable(GL11.GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);

        glColor3d(c.getRed() / 255D, c.getGreen() / 255D, c.getBlue() / 255D);

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

    private void addToValidEntities(Class entityClass) {
        validEntities.put(getEntityName(entityClass), entityClass);
    }

    private String getEntityName(Class entityClass) {
        return entityClass.getSimpleName().replaceFirst("Entity", "").toLowerCase();
    }

    private void addEntities() {
        addToValidEntities(EntityDragon.class);
        addToValidEntities(EntityWither.class);

        addToValidEntities(EntityArmorStand.class);
        addToValidEntities(EntityBoat.class);
        addToValidEntities(EntityEnderCrystal.class);
        addToValidEntities(EntityEnderEye.class);
        addToValidEntities(EntityEnderPearl.class);
        addToValidEntities(EntityExpBottle.class);
        addToValidEntities(EntityFallingBlock.class);
        addToValidEntities(EntityFireworkRocket.class);
        addToValidEntities(EntityItem.class);
        addToValidEntities(EntityItemFrame.class);
        addToValidEntities(EntityMinecartChest.class);
        addToValidEntities(EntityMinecartCommandBlock.class);
        addToValidEntities(EntityMinecartEmpty.class);
        addToValidEntities(EntityMinecartFurnace.class);
        addToValidEntities(EntityMinecartHopper.class);
        addToValidEntities(EntityMinecartMobSpawner.class);
        addToValidEntities(EntityMinecartTNT.class);
        addToValidEntities(EntityPainting.class);
        addToValidEntities(EntityTNTPrimed.class);
        addToValidEntities(EntityXPOrb.class);

        addToValidEntities(EntityBlaze.class);
        addToValidEntities(EntityCaveSpider.class);
        addToValidEntities(EntityCreeper.class);
        addToValidEntities(EntityElderGuardian.class);
        addToValidEntities(EntityEnderman.class);
        addToValidEntities(EntityEndermite.class);
        addToValidEntities(EntityEvoker.class);
        addToValidEntities(EntityGhast.class);
        addToValidEntities(EntityGiantZombie.class);
        addToValidEntities(EntityGuardian.class);
        addToValidEntities(EntityHusk.class);
        addToValidEntities(EntityIronGolem.class);
        addToValidEntities(EntityMagmaCube.class);
        addToValidEntities(EntityPigZombie.class);
        addToValidEntities(EntityPolarBear.class);
        addToValidEntities(EntityShulker.class);
        addToValidEntities(EntitySilverfish.class);
        addToValidEntities(EntitySkeleton.class);
        addToValidEntities(EntitySlime.class);
        addToValidEntities(EntitySnowman.class);
        addToValidEntities(EntitySpider.class);
        addToValidEntities(EntityStray.class);
        addToValidEntities(EntityVex.class);
        addToValidEntities(EntityVindicator.class);
        addToValidEntities(EntityWitch.class);
        addToValidEntities(EntityWitherSkeleton.class);
        addToValidEntities(EntityZombie.class);
        addToValidEntities(EntityZombieVillager.class);

        addToValidEntities(EntityBat.class);
        addToValidEntities(EntityChicken.class);
        addToValidEntities(EntityCow.class);
        addToValidEntities(EntityDonkey.class);
        addToValidEntities(EntityHorse.class);
        addToValidEntities(EntityLlama.class);
        addToValidEntities(EntityMooshroom.class);
        addToValidEntities(EntityMule.class);
        addToValidEntities(EntityOcelot.class);
        addToValidEntities(EntityPig.class);
        addToValidEntities(EntityRabbit.class);
        addToValidEntities(EntitySheep.class);
        addToValidEntities(EntitySkeletonHorse.class);
        addToValidEntities(EntitySquid.class);
        addToValidEntities(EntityVillager.class);
        addToValidEntities(EntityWolf.class);
        addToValidEntities(EntityZombieHorse.class);

        addToValidEntities(EntityPlayerMP.class);

        addToValidEntities(EntityDragonFireball.class);
        addToValidEntities(EntityEgg.class);
        addToValidEntities(EntityEvokerFangs.class);
        addToValidEntities(EntityFishHook.class);
        addToValidEntities(EntityLargeFireball.class);
        addToValidEntities(EntityLlamaSpit.class);
        addToValidEntities(EntityPotion.class);
        addToValidEntities(EntityShulkerBullet.class);
        addToValidEntities(EntitySmallFireball.class);
        addToValidEntities(EntitySnowball.class);
        addToValidEntities(EntitySpectralArrow.class);
        addToValidEntities(EntityTippedArrow.class);
        addToValidEntities(EntityWitherSkull.class);

    }
}
