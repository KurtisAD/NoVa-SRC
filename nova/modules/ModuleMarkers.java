package nova.modules;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import nova.Command;
import nova.core.*;
import nova.events.BlockRenderedEvent;
import nova.events.EventHandler;
import nova.events.RenderOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Skeleton Man on 7/22/2016.
 */
public class ModuleMarkers extends ModuleBase {
    Map<SimpleBlock, Marker> blockDescriptors;
    ConcurrentHashMap<Location, SimpleBlock> blocks;
    ArrayList<ChunkLocation> chunks;

    boolean iterating;

    // As a design choice, I decided to keep ints as the input and parse to double when drawing marker to match old versions
    // This is supposed to fix a problem, I'm not sure if it does

    public ModuleMarkers(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        chunks = new ArrayList<ChunkLocation>();

        blocks = new ConcurrentHashMap<Location, SimpleBlock>();
        blockDescriptors = new HashMap<SimpleBlock, Marker>();

        aliases.add("m");

        this.command = new Command(Nova, this, aliases, "Draws a marker of a given style over a given block. Replaces x-ray. All IDs are in the format id:metadata. If you do not provide metadata, it is assumed to be 0. (ex. markers new 98:1; or markers new 98) Comes preloaded with a marker for chests.");

//		this.command.registerArg("chunk", new Class[] { }, "Hilight quartz chunks in the nether");

        this.iterating = false;

    }


    @RegisterArgument(name = "new", description = "Adds a new marker of the given ID and metadata with the default style, a white star.")
    public void newMarker(String marker){
        SimpleBlock block = getBlockFromString(marker);

        if(blockDescriptors.containsKey(block))
        {
            this.Nova.errorMessage("The block " + block.toString() + " has already been added");
            return;
        }

        blockDescriptors.put(block, new Marker(255, 255, 255, 1));

        Nova.confirmMessage("Added new block " + block.toString() + " with default settings");
        mc.renderGlobal.loadRenderers();

    }

    @RegisterArgument(name = "del", description = "Deletes a marker of the given ID and metadata")
    public void delMarker(String marker){
        SimpleBlock block = getBlockFromString(marker);

        if (!blockDescriptors.containsKey(block)) {
            this.Nova.errorMessage("The block " + block.toString() + " does not exist, so you cannot delete it");
            return;
        }

        blockDescriptors.remove(block);

        Nova.confirmMessage("Removed block " + block.toString());
    }

    @RegisterArgument(name = "type", description = "Changes the type of a marker; 0 is box, 1 is star")
    public void changeType(String marker, int type){
        SimpleBlock block = getBlockFromString(marker);

        if (!blockDescriptors.containsKey(block)) {
            this.Nova.errorMessage("The block " + block.toString() + " does not exist");
            return;
        }

        Marker m = blockDescriptors.get(block);
        blockDescriptors.put(block, new Marker(m.color, type));

        Nova.confirmMessage("Changed marker type of block " + block.toString() + " to " + type);

    }

    @RegisterArgument(name = "add", description = "Adds a new marker of the given ID and metadata with colors R, G, B (see color) and the type (see type); (ex. markers new 98:2 255 0 0 1; adds a red star for mossy stone brick)")
    public void addMarker(String marker, int r, int g, int b, int type){
        SimpleBlock block = getBlockFromString(marker);

        if (blockDescriptors.containsKey(block)) {
            this.Nova.errorMessage("The block " + block.toString() + " has already been added");
            return;
        }

        blockDescriptors.put(block, new Marker(r,g,b,type));

        Nova.confirmMessage("Added marker for block " + block.toString());
        mc.renderGlobal.loadRenderers();
    }

    @RegisterArgument(name = "color", description = "Changes the color for a marker of the given ID and metadata in the format R G B, where integers R G and B are in the interval [0, 255]")
    public void colorMarker(String marker, int r, int g, int b){
        SimpleBlock block = getBlockFromString(marker);

        if (!blockDescriptors.containsKey(block)) {
            this.Nova.errorMessage("The block " + block.toString() + " has not been added yet");
            return;
        }

        Marker m = blockDescriptors.get(block);

        blockDescriptors.put(block, new Marker(r, g, b, m.setting));
        Nova.confirmMessage("Changed color for block " + block.toString());

    }

    @RegisterArgument(name = "list", description = "Lists markers")
    public void listMarkers(){
        String itemName = "";

        for (SimpleBlock key : blockDescriptors.keySet()) {
            Marker marker = blockDescriptors.get(key);

            try {
                itemName = (new ItemStack(Item.getItemById(key.id), 1, key.metadata)).getDisplayName();
            } catch(Exception e) {
                itemName = "Unknown";
            }


            // TODO: set so it only outputs a certain length of double maybe? needs testing
            this.Nova.message(Integer.toString(key.id) + ":" + Integer.toString(key.metadata)
                    + " " + itemName
                    + " rgb(" + marker.color.getRed()
                    + ", " + marker.color.getGreen()
                    + ", " + marker.color.getBlue()
                    + ") Type: " + Integer.toString(marker.setting));
        }
    }

    public SimpleBlock getBlockFromString(String blocktext) {
        int id, metadata;
        SimpleBlock block;
        block = new SimpleBlock(0, 0);
        String[] tmp;


        tmp = blocktext.split(":");

        metadata = tmp.length > 1 ? Integer.parseInt(tmp[1]) : 0;

        try {
            id = Integer.parseInt(tmp[0]);
        } catch (NumberFormatException e) {
            id = Block.getIdFromBlock(Block.getBlockFromName(tmp[0]));
            if (id == -1) {
                try {
                    Nova.errorMessage("Invalid input: " + tmp[0] + ":" + tmp[1]);
                } catch (ArrayIndexOutOfBoundsException e1) {
                    Nova.errorMessage("Invalid input: " + tmp[0]);
                }
                id = 0;
            }
        }

        return new SimpleBlock(id, metadata);
    }


    @EventHandler
    public void onBlockRendered(BlockRenderedEvent e){
        if (e.block.id == 12){
            if (e.block.id == 12){

            }
        }

        if(blockDescriptors.containsKey(e.block)){
            synchronized(blocks) {
                if(e.block.equals(Block.getStateId(Block.getStateById(7)))){
                    if (mc.player.dimension == -1 && (e.pos.y >= 5 && e.pos.y < 122))
                        this.blocks.put(e.pos, e.block);
                    else if (mc.player.dimension != -1 && e.pos.y >= 5)
                        this.blocks.put(e.pos, e.block);

                } else {
                    this.blocks.put(e.pos, e.block);
                }
            }
        } else {
            this.blocks.remove(e.pos);
        }
    }

    @Override
    public void onEnable()
    {
        mc.renderGlobal.loadRenderers();
        this.isEnabled = true;
    }

    @EventHandler
    public void onRendererTick(RenderOverlayEvent e)
    {

        if(this.isEnabled)
        {
            this.drawMarkers();
        }

    }

    public void drawMarkers() //Something terribly wrong with the iterators
    {
        int j2 = 225 % 0x10000;
        int k2 = 225 / 0x10000;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j2 / 1.0F, (float)k2 / 1.0F);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        SimpleBlock block;

        for(Iterator<Location> i = blocks.keySet().iterator(); i.hasNext();) {
            Location pos = i.next();

            block = blocks.get(pos);

            if (block.id == 1){
                int asdf= 0;
            }

            if(Util.distance(pos,TileEntityRendererDispatcher.staticPlayerX, TileEntityRendererDispatcher.staticPlayerZ) > 65536.0D || !blockDescriptors.containsKey(block))
                i.remove();
            else
                this.drawMarker(pos, blockDescriptors.get(block));
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

    // 0 is box, 1 is star, 2 is diagonal line
    public void drawMarker(Location pos, Marker marker)
    {

        // I'm going to try to move the GL11 stuff into the draw functions themselves
        // might be redundant code, probably may change it back for that reason

        /*
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
        */

        double minX = pos.x - TileEntityRendererDispatcher.staticPlayerX;
        double minZ = pos.z - TileEntityRendererDispatcher.staticPlayerZ;
        double minY = pos.y - TileEntityRendererDispatcher.staticPlayerY;

        double maxX = minX + 1.0D;
        double maxZ = minZ + 1.0D;
        double maxY = minY + 1.0D;


        if(marker.setting == 0)
        {
            drawOutlinedBoundingBox(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ), marker.color.getRed(), marker.color.getGreen(), marker.color.getBlue(), 255);
        }


        if(marker.setting == 1)
        {
            drawStar(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ), marker.color.getRed(), marker.color.getGreen(), marker.color.getBlue(), 255);
        }


        /*
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        */
    }

    public int parseBlock(int id, int metadata)
    {
        return id + metadata << 12;
    }

    public int parseDescriptor(int r, int g, int b, int type)
    {
        return ( r << 24) | ( g << 16) | (b << 8) | type;
    }

    private int getBlockId(int key) {
        return key & 0xFFF;
    }

    private int getBlockMetadata(int key) {
        return key >> 12;
    }


    public static void drawOutlinedBoundingBox(AxisAlignedBB boundingBox,
                                               int red, int green, int blue, int alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
                .color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
                .color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ)
                .color(red, green, blue, alpha).endVertex();
        tessellator.draw();
    }

    public static void drawStar(AxisAlignedBB aabb,
                                int red, int green, int blue, int alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);

        vertexbuffer.pos(aabb.minX, aabb.minY, aabb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(aabb.maxX, aabb.maxY, aabb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        tessellator.draw();

        vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(aabb.maxX, aabb.minY, aabb.minZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(aabb.minX, aabb.maxY, aabb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        tessellator.draw();

        vertexbuffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(aabb.minX, aabb.minY, aabb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(aabb.maxX, aabb.maxY, aabb.minZ)
                .color(red, green, blue, alpha).endVertex();
        tessellator.draw();

        vertexbuffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(aabb.maxX, aabb.minY, aabb.maxZ)
                .color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos(aabb.minX, aabb.maxY, aabb.minZ)
                .color(red, green, blue, alpha).endVertex();
        tessellator.draw();
    }


}
