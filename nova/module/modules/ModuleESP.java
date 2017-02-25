package nova.module.modules;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import nova.Nova;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.EntityLabelRenderedEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;
import nova.util.Util;
import org.lwjgl.opengl.GL11;

/**
 * Created by Skeleton Man on 7/19/2016.
 */
public class ModuleESP extends ModuleBase {
    @Saveable
    public boolean healthEsp;
    @Saveable
    public boolean itemEsp;
    @Saveable
    public boolean armorEsp;

    // TODO: Change how labels are rendered, code is prehistoric and needs rewriting
    // TODO: Add more cool features that can be compressed (key factor here, a lot of the rendering is super big)

    public ModuleESP() {
        super();

        this.description = ("Hilights a player's name and shows their health, held item, and distance; friend's names are green.");

        this.healthEsp = true;
        this.itemEsp = true;
    }


    @EventHandler
    public boolean onEntityLabelRenderedEvent(EntityLabelRenderedEvent event)
    {
        // TODO: consider if instanceof EntityOtherPlayerMP is needed
        if(this.isEnabled && event.entity instanceof EntityOtherPlayerMP)
        {
            Entity e = event.entity;

            String name = e.getDisplayName().getUnformattedText();
            double distance = Math.round(mc.player.getDistanceSqToEntity(e));

            FontRenderer fr = mc.fontRendererObj;

            float var14;

            double i = event.interpolationX;
            double j = event.interpolationY;
            double k = event.interpolationZ;


            double dx = (e.posX * 1 - mc.getRenderManager().viewerPosX + 0.5D);
            double dy = (e.posY - mc.getRenderManager().viewerPosY + 0.5D);
            double dz = (e.posZ * 1 - mc.getRenderManager().viewerPosZ + 0.5D);

            double dl = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
            var14 = (float)(dl * 0.1F + 1.0F) * 0.02666666666666667F;

            int j2 = 225 % 0x10000;
            int k2 = 225 / 0x10000;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j2 / 1.0F, (float)k2 / 1.0F);


            GL11.glPushMatrix();
            GL11.glTranslatef((float)i + 0.0F, (float)j + e.height + 0.5F, (float)k);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-event.rm.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(event.rm.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-var14, -var14, var14);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);

            // ||| - Disable fog
            GL11.glDisable(GL11.GL_FOG);


            // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();

            byte var16 = 0;

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);


            name = name + " (" + Integer.toString((int)Math.floor(Math.sqrt(distance))) + ")";
            if (this.healthEsp){
                name = "\u00A74[" + String.format("%.1f",((EntityOtherPlayerMP) e).getHealth()) + "]\u00A7r " + name;
            }

            String item = "";
            String leftItem = "";
            if(e instanceof EntityOtherPlayerMP) {
                item = Util.getItemNameAndEnchantments(((EntityOtherPlayerMP)e).inventory.getCurrentItem());
                leftItem = Util.getItemNameAndEnchantments(((EntityOtherPlayerMP) e).inventory.offHandInventory.get(0));
            }

            int var17 = fr.getStringWidth(name) / 2;


            int color;
            // ||| - Colors
            if(e instanceof EntityOtherPlayerMP && Nova.getModule(ModuleFriends.class).isFriend(e.getDisplayName().getUnformattedText())){
                vertexbuffer.color(51, 178, 51, 255);
                color = 0xfffdfe02;

            }
            else{
                vertexbuffer.color(0, 0, 0, 255);
                color = -1;
            }

            vertexbuffer.pos((double)(-var17 - 1), (double)(-1 + var16), 0.0D);
            vertexbuffer.pos((double)(-var17 - 1), (double)(8 + var16), 0.0D);
            vertexbuffer.pos((double)(var17 + 1), (double)(8 + var16), 0.0D);
            vertexbuffer.pos((double)(var17 + 1), (double)(-1 + var16), 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);


            // |||
            if(e instanceof  EntityOtherPlayerMP){
                if(this.itemEsp){
                    fr.drawString("\247l" + item, -fr.getStringWidth(item) / 2, -20, 553648127);

                    fr.drawString("\247l" + item, -fr.getStringWidth(item) / 2, -20, color);

                    fr.drawString("\247o" + leftItem, -fr.getStringWidth(leftItem) / 2, -10, 553648127);

                    fr.drawString("\247o" + leftItem, -fr.getStringWidth(leftItem) / 2, -10, color);
                }

                if(this.armorEsp){
                    String armor = Nova.getModule(ModuleInfo.class).getArmorDurability(e.getDisplayName().getUnformattedText());
                    int position = this.itemEsp ? -30 : -10;

                    fr.drawString(armor, -fr.getStringWidth(armor) / 2, position, 553648127);

                    fr.drawString(armor, -fr.getStringWidth(armor) / 2, position, color);
                }

            }



            fr.drawString(name, -fr.getStringWidth(name) / 2, 0, 553648127);
            fr.drawString(name, -fr.getStringWidth(name) / 2, 0, color);

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);


            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);


            // |||
            GL11.glEnable(GL11.GL_FOG);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();


            return false;
        }

        return true;
    }

    @RegisterArgument(name = "health", description = "Show current health?")
    public void toggleHealth(){
        this.healthEsp = !this.healthEsp;
    }

    @RegisterArgument(name = "items", description = "Show current items?")
    public void toggleItems(){
        this.itemEsp = !this.itemEsp;
    }

    @RegisterArgument(name = "armor", description = "Show armor durability?")
    public void toggleArmor(){
        this.armorEsp = !this.armorEsp;
    }

    @Override
    public String getMetadata()
    {
        String ret = "";
        if(this.itemEsp)
        {
            ret += "Items, ";
        }

        if(this.armorEsp)
        {
            ret += "Armor, ";
        }

        if(this.healthEsp){
            ret += "Health";
        }

        if(!this.armorEsp && !this.itemEsp && !this.healthEsp)
            return "";

        if(ret.endsWith(", "))
        {
            ret = ret.substring(0, ret.length() - 2);
        }

        return "(" + ret + ")";
    }

}
