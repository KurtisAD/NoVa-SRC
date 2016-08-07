package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import nova.Command;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

import java.util.Random;

/**
 * Created by Skeleton Man on 7/17/2016.
 */

public class ModuleAntiAfk extends ModuleBase{

    private BlockPos block;
    private Random random;
    private BlockPos nextBlock;

    private Long currentMS;
    private Long lastMs;

    public ModuleAntiAfk(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);
        aliases.add("afk");
        aliases.add("aafk");
        this.command = new Command(Nova, this, aliases, "Walks randomly to prevent AFK detectors. From Wrust");

        currentMS = System.currentTimeMillis();
        lastMs = currentMS;

        loadModule();
    }

    @Override
    public void onEnable()
    {
        try
        {
            block = new BlockPos(mc.thePlayer);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        random = new Random();

        this.isEnabled = true;
    }

    @EventHandler
    public void onUpdate(PlayerTickEvent e)
    {
        if (this.isEnabled) {
            currentMS = System.currentTimeMillis();

            if(currentMS >= lastMs + 3000|| nextBlock == null)
            {
                if(block == null)
                    onEnable();
                nextBlock =
                        block.add(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
                lastMs = System.currentTimeMillis();
            }
            faceBlockClientHorizontally(nextBlock);
            mc.gameSettings.keyBindForward.pressed = getHorizontalPlayerBlockDistance(nextBlock) > 0.75;
        }
    }

    @Override
    public void onDisable()
    {
        mc.gameSettings.keyBindForward.pressed = false;
        this.isEnabled = false;
    }

    private static void faceBlockClientHorizontally(BlockPos blockPos)
    {
        double diffX =
                blockPos.getX() + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
        double diffZ =
                blockPos.getZ() + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
        float yaw =
                (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        Minecraft.getMinecraft().thePlayer.rotationYaw =
                Minecraft.getMinecraft().thePlayer.rotationYaw
                        + MathHelper.wrapDegrees(yaw
                        - Minecraft.getMinecraft().thePlayer.rotationYaw);
    }

    private static float getHorizontalPlayerBlockDistance(BlockPos blockPos)
    {
        float xDiff =
                (float)(Minecraft.getMinecraft().thePlayer.posX - blockPos.getX());
        float zDiff =
                (float)(Minecraft.getMinecraft().thePlayer.posZ - blockPos.getZ());
        return MathHelper.sqrt_float((xDiff - 0.5F) * (xDiff - 0.5F)
                + (zDiff - 0.5F) * (zDiff - 0.5F));
    }
}
