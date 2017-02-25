package nova.module.modules;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import nova.Nova;
import nova.core.Location;
import nova.core.Marker;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.BlockRenderedEvent;
import nova.event.events.EntityRenderTickEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Skeleton Man on 7/22/2016.
 */
public class ModuleMarkers extends ModuleBase {

    @Saveable
    public Map<Integer, Marker> blockDescriptors;

    ConcurrentHashMap<Location, Integer> blocks;


    // TODO: rewrite everything, maybe add a GUI because this shit is super complicated

    public ModuleMarkers() {
        super();

        blocks = new ConcurrentHashMap<>();
        blockDescriptors = new HashMap<>();

        aliases.add("m");
        aliases.add("marker");

        this.description = ("Draws a marker of a given style over a given block. Replaces x-ray. Does not consider metadata. Comes preloaded.");

//		this.command.registerArg("chunk", new Class[] { }, "Hilight quartz chunks in the nether");

        // Default markers here
        blockDescriptors.put(90, new Marker(new Color(175, 140, 100), 1));
        blockDescriptors.put(130, new Marker(new Color(175, 40, 100), 0));
        blockDescriptors.put(54, new Marker(new Color(170, 120, 45), 0));
        blockDescriptors.put(146, new Marker(new Color(170, 120, 45), 0));
        blockDescriptors.put(61, new Marker(new Color(140, 140, 140), 1));
        blockDescriptors.put(62, new Marker(new Color(140, 140, 140), 1));
        blockDescriptors.put(23, new Marker(new Color(140, 140, 140), 1));
        blockDescriptors.put(158, new Marker(new Color(140, 140, 140), 1));
        blockDescriptors.put(154, new Marker(new Color(140, 140, 140), 1));
        blockDescriptors.put(117, new Marker(new Color(140, 140, 140), 1));


        for (int i = 219; i <= 234; i++) {
            blockDescriptors.put(i, new Marker(new Color(200, 200, 20), 0));

        }
    }


    @RegisterArgument(name = "new", description = "Adds a new marker of the given ID with the default style, a white star.")
    public void newMarker(String marker){
        int block = getBlockFromString(marker);
        if (block == -1 || block == 0) {
            return;
        }

        if(blockDescriptors.containsKey(block))
        {
            Nova.errorMessage("The block " + getIdName(block) + " has already been added");
            return;
        }

        blockDescriptors.put(block, new Marker(255, 255, 255, 1));

        Nova.confirmMessage("Added new block " + getIdName(block) + " with default settings");
        mc.renderGlobal.loadRenderers();

    }

    @RegisterArgument(name = "del", description = "Deletes a marker of the given ID")
    public void delMarker(String marker){
        int block = getBlockFromString(marker);
        if (block == -1 || block == 0) {
            return;
        }

        if (!blockDescriptors.containsKey(block)) {
            Nova.errorMessage("The block " + getIdName(block) + " does not exist, so you cannot delete it");
            return;
        }

        blockDescriptors.remove(block);

        Nova.confirmMessage("Removed block " + getIdName(block));
    }

    @RegisterArgument(name = "type", description = "Changes the type of a marker; 0 is box, 1 is star")
    public void changeType(String marker, int type){
        int block = getBlockFromString(marker);
        if (block == -1 || block == 0) {
            return;
        }

        if (!blockDescriptors.containsKey(block)) {
            Nova.errorMessage("The block " + getIdName(block) + " does not exist");
            return;
        }

        Marker m = blockDescriptors.get(block);
        blockDescriptors.put(block, new Marker(m.color, type));

        Nova.confirmMessage("Changed marker type of block " + getIdName(block) + " to " + type);

    }

    @RegisterArgument(name = "add", description = "Adds a new marker of the given ID with colors R, G, B (see color) and the type (see type); (ex. markers new 98:2 255 0 0 1; adds a red star for mossy stone brick)")
    public void addMarker(String marker, int r, int g, int b, int type){
        int block = getBlockFromString(marker);
        if (block == -1 || block == 0) {
            return;
        }

        if (blockDescriptors.containsKey(block)) {
            Nova.errorMessage("The block " + getIdName(block) + " has already been added");
            return;
        }

        blockDescriptors.put(block, new Marker(r,g,b,type));

        Nova.confirmMessage("Added marker for block " + getIdName(block));
        mc.renderGlobal.loadRenderers();
    }

    @RegisterArgument(name = "color", description = "Changes the color for a marker of the given ID in the format R G B, where integers R G and B are in the interval [0, 255]")
    public void colorMarker(String marker, int r, int g, int b){
        int block = getBlockFromString(marker);
        if (block == -1 || block == 0) {
            return;
        }

        if (!blockDescriptors.containsKey(block)) {
            Nova.errorMessage("The block " + getIdName(block) + " has not been added yet");
            return;
        }

        Marker m = blockDescriptors.get(block);

        blockDescriptors.put(block, new Marker(r, g, b, m.setting));
        Nova.confirmMessage("Changed color for block " + getIdName(block));

    }

    @RegisterArgument(name = "list", description = "Lists markers")
    public void listMarkers(){
        String itemName = "";

        for (int key : blockDescriptors.keySet()) {
            Marker marker = blockDescriptors.get(key);


            Nova.message(getIdName(key)
                    + " rgb(" + marker.color.getRed()
                    + ", " + marker.color.getGreen()
                    + ", " + marker.color.getBlue()
                    + ") Type: " + Integer.toString(marker.setting));
        }
    }

    private int getBlockFromString(String blocktext) {
        int id;


        try {
            id = Integer.parseInt(blocktext);
        } catch (NumberFormatException e) {
            id = Block.getIdFromBlock(Block.getBlockFromName(blocktext));
            if (id == -1 || id == 0) {
                Nova.errorMessage("Invalid input: " + blocktext);
            }
        }

        return id;
    }



    @EventHandler
    public void onBlockRendered(BlockRenderedEvent e){
        updateBlock(e.id, e.pos);
    }


    private void updateBlock(int id, Location pos) {
        if (blockDescriptors.containsKey(id)) {
            if (id == (Block.getStateId(Block.getStateById(7)))) {
                if (mc.player.dimension == -1 && (pos.y >= 5 && pos.y < 122))
                    this.blocks.put(pos, id);
                else if (mc.player.dimension != -1 && pos.y >= 5)
                    this.blocks.put(pos, id);
                else
                    this.blocks.remove(pos);
            } else {
                this.blocks.put(pos, id);
            }
        } else {
            this.blocks.remove(pos);
        }
    }

    @Override
    public void onEnable()
    {
        mc.renderGlobal.loadRenderers();
        this.isEnabled = true;
    }

    @EventHandler
    public void onRendererTick(EntityRenderTickEvent e)
    {
        if(this.isEnabled)
        {
            this.drawMarkers();
        }
    }


    private void drawMarkers()
    {
        int j2 = 225 % 0x10000;
        int k2 = 225 / 0x10000;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j2 / 1.0F, (float)k2 / 1.0F);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int id;
        Location pos;

        for(Iterator<Location> i = blocks.keySet().iterator(); i.hasNext();) {
            pos = i.next();

            id = blocks.get(pos);


            if (!blockDescriptors.containsKey(id) || Block.getIdFromBlock(mc.world.getBlockState(pos.getBlockPos()).getBlock()) != id) {
                i.remove();
            } else
                this.drawMarker(pos, blockDescriptors.get(id));
        }



//		if(this.chunk)
//		{
//			Location c;
//			for(Iterator<Location> i = chunks.iterator(); i.hasNext();)
//			{
//				c = i.next();
//
//				if(c.distanceFromSq(TileEntityRendererDispatcher.staticPlayerX / 16, TileEntityRendererDispatcher.staticPlayerZ / 16) > 65536.0D)
//					i.remove();
//				else
//					this.drawMarker(new AxisAlignedBB(c.blockX * 16, 128, c.blockZ * 16, (c.blockX * 16) + 16, 128, (c.blockZ * 16) + 16), -256);
//			}
//		}
    }

    private String getIdName(int id) {
        Block block = Block.getBlockById(id);
        return "[" + Block.REGISTRY.getNameForObject(block) + "] [" + Integer.toString(id) + "]";
    }

    // 0 is box, 1 is star, 2 is diagonal line
    private void drawMarker(Location pos, Marker marker)
    {

        // I'm going to try to move the GL11 stuff into the draw functions themselves
        // might be redundant code, probably may change it back for that reason


        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.5F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(1.0F);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);


        double minX = pos.x - TileEntityRendererDispatcher.staticPlayerX;
        double minZ = pos.z - TileEntityRendererDispatcher.staticPlayerZ;
        double minY = pos.y - TileEntityRendererDispatcher.staticPlayerY;

        double maxX = minX + 1.0D;
        double maxZ = minZ + 1.0D;
        double maxY = minY + 1.0D;


        if(marker.setting == 0)
        {
            drawOutlinedBoundingBox(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ), marker.color.getRed(), marker.color.getGreen(), marker.color.getBlue());
        }


        if(marker.setting == 1)
        {
            drawStar(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ), marker.color.getRed(), marker.color.getGreen(), marker.color.getBlue());
        }


        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();

    }


    private static void drawOutlinedBoundingBox(AxisAlignedBB boundingBox,
                                                int red, int green, int blue) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
                .color(red, green, blue, 255).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
                .color(red, green, blue, 255).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ)
                .color(red, green, blue, 255).endVertex();
        tessellator.draw();
    }

    private static void drawStar(AxisAlignedBB aabb,
                                 int red, int green, int blue) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);

        vertexbuffer.pos(aabb.minX, aabb.minY, aabb.minZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(aabb.maxX, aabb.maxY, aabb.maxZ)
                .color(red, green, blue, 255).endVertex();
        tessellator.draw();

        vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(aabb.maxX, aabb.minY, aabb.minZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(aabb.minX, aabb.maxY, aabb.maxZ)
                .color(red, green, blue, 255).endVertex();
        tessellator.draw();

        vertexbuffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(aabb.minX, aabb.minY, aabb.maxZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(aabb.maxX, aabb.maxY, aabb.minZ)
                .color(red, green, blue, 255).endVertex();
        tessellator.draw();

        vertexbuffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(aabb.maxX, aabb.minY, aabb.maxZ)
                .color(red, green, blue, 255).endVertex();
        vertexbuffer.pos(aabb.minX, aabb.maxY, aabb.minZ)
                .color(red, green, blue, 255).endVertex();
        tessellator.draw();
    }

}
