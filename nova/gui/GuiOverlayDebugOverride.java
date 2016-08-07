package nova.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import nova.modules.ModuleFakeCoord;

import java.util.List;

/**
 * Created by Skeleton Man on 7/18/2016.
 */
public class GuiOverlayDebugOverride extends GuiOverlayDebug {
    private final Minecraft mc;

    public GuiOverlayDebugOverride(Minecraft mc){
        super(mc);
        this.mc = mc;
    }

    @SuppressWarnings("incomplete-switch")
    protected List<String> call()
    {
        BlockPos blockpos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);

        if (this.mc.isReducedDebug())
        {
            return Lists.newArrayList(new String[] {"Minecraft 1.10.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "", String.format("Chunk-relative: %d %d %d", new Object[]{Integer.valueOf(blockpos.getX() & 15), Integer.valueOf(blockpos.getY() & 15), Integer.valueOf(blockpos.getZ() & 15)})});
        }
        else
        {
            Entity entity = this.mc.getRenderViewEntity();
            EnumFacing enumfacing = entity.getHorizontalFacing();
            String s = "Invalid";

            switch (enumfacing)
            {
                case NORTH:
                    s = "Towards negative Z";
                    break;

                case SOUTH:
                    s = "Towards positive Z";
                    break;

                case WEST:
                    s = "Towards negative X";
                    break;

                case EAST:
                    s = "Towards positive X";
            }

            List<String> list = Lists.newArrayList(new String[] {"Minecraft 1.10.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : "/" + this.mc.getVersionType()) + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "",
                    String.format("XYZ: %.3f / %.5f / %.3f", new Object[]{Double.valueOf(this.mc.getRenderViewEntity().posX) + ModuleFakeCoord.getX(),Double.valueOf(this.mc.getRenderViewEntity().getEntityBoundingBox().minY) + ModuleFakeCoord.getY(), Double.valueOf(this.mc.getRenderViewEntity().posZ) + ModuleFakeCoord.getZ()}),
                    String.format("Block: %d %d %d", new Object[]{Integer.valueOf(blockpos.getX()) + ModuleFakeCoord.getX(), Integer.valueOf(blockpos.getY()) + ModuleFakeCoord.getY(), Integer.valueOf(blockpos.getZ()) + ModuleFakeCoord.getZ()}),
                    String.format("Chunk: %d %d %d in %d %d %d", new Object[]{Integer.valueOf(blockpos.getX() & 15), Integer.valueOf(blockpos.getY() & 15), Integer.valueOf(blockpos.getZ() & 15), Integer.valueOf((blockpos.getX() + ModuleFakeCoord.getX())>> 4), Integer.valueOf((blockpos.getY() + ModuleFakeCoord.getY())>> 4), Integer.valueOf(blockpos.getZ() + ModuleFakeCoord.getX()>> 4)}),
                    String.format("Facing: %s (%s) (%.1f / %.1f)", new Object[]{enumfacing, s, Float.valueOf(MathHelper.wrapDegrees(entity.rotationYaw)), Float.valueOf(MathHelper.wrapDegrees(entity.rotationPitch))})});

            if (this.mc.theWorld != null)
            {
                Chunk chunk = this.mc.theWorld.getChunkFromBlockCoords(blockpos);

                if (this.mc.theWorld.isBlockLoaded(blockpos) && blockpos.getY() >= 0 && blockpos.getY() < 256)
                {
                    if (!chunk.isEmpty())
                    {
                        list.add("Biome: " + chunk.getBiome(blockpos, this.mc.theWorld.getBiomeProvider()).getBiomeName());
                        list.add("Light: " + chunk.getLightSubtracted(blockpos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos) + " block)");
                        DifficultyInstance difficultyinstance = this.mc.theWorld.getDifficultyForLocation(blockpos);

                        if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null)
                        {
                            EntityPlayerMP entityplayermp = this.mc.getIntegratedServer().getPlayerList().getPlayerByUUID(this.mc.thePlayer.getUniqueID());

                            if (entityplayermp != null)
                            {
                                difficultyinstance = entityplayermp.worldObj.getDifficultyForLocation(new BlockPos(entityplayermp));
                            }
                        }

                        list.add(String.format("Local Difficulty: %.2f // %.2f (Day %d)", new Object[] {Float.valueOf(difficultyinstance.getAdditionalDifficulty()), Float.valueOf(difficultyinstance.getClampedAdditionalDifficulty()), Long.valueOf(this.mc.theWorld.getWorldTime() / 24000L)}));
                    }
                    else
                    {
                        list.add("Waiting for chunk...");
                    }
                }
                else
                {
                    list.add("Outside of world...");
                }
            }

            if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive())
            {
                list.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
            }

            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null)
            {
                BlockPos blockpos1 = this.mc.objectMouseOver.getBlockPos();
                list.add(String.format("Looking at: %d %d %d", new Object[] {Integer.valueOf(blockpos1.getX()) + (ModuleFakeCoord.getSpoofing() ? ModuleFakeCoord.getX() : 0), Integer.valueOf(blockpos1.getY()) + (ModuleFakeCoord.getSpoofing() ? ModuleFakeCoord.getY() : 0), Integer.valueOf(blockpos1.getZ()) + (ModuleFakeCoord.getSpoofing() ? ModuleFakeCoord.getZ() : 0)}));
            }

            return list;
        }
    }

}
