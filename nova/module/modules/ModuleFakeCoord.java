package nova.module.modules;

import net.minecraft.client.Minecraft;
import nova.event.RegisterArgument;
import nova.module.ModuleBase;
import org.lwjgl.opengl.Display;

/**
 * Created by Skeleton Man on 7/18/2016.
 */
public class ModuleFakeCoord extends ModuleBase {

    // DO NOT USE THIS, DOES NOT PROTECT AGAINST COORDINATE EXPLOITS
    // NEEDS FIXING
    // useless due to bedrock bruteforcer/ other stuff?
    static boolean isSpoofing;

    static int xOffset;
    static int yOffset;
    static int zOffset;

    String lastTitle;
    final String spoofingTitle = " [FAKE COORDINATES ACTIVE]";

    public ModuleFakeCoord(nova.Nova Nova, Minecraft mc) {
        super();

        aliases.add("fakecoords");

        this.description = ("Offsets coordinates in debug overlay and ModuleGui, input is rounded down to multiples of 16");

        isSpoofing = false;
        xOffset = 0;
        yOffset = 0;
        zOffset = 0;

        lastTitle = "";
    }

    @RegisterArgument(name = "offset", description = "Offsets location by coordinate amount, rounded down to multiples of 16")
    public void doOffset(int xOffset, int yOffset, int zOffset){
        // Can probably use & here, would cut operations in half
        ModuleFakeCoord.xOffset = (xOffset >> 4) << 4;
        ModuleFakeCoord.yOffset = (yOffset >> 4) << 4;
        ModuleFakeCoord.zOffset = (zOffset >> 4) << 4;
    }

    @RegisterArgument(name = "set", description = "Sets location to coordinates, rounded down to multiples of 16")
    public void doSet(int xSet, int ySet, int zSet){
        // Can probably use & here, would cut operations in half
        xOffset = (xSet - (int) mc.player.posX >> 4) << 4;
        yOffset = (ySet - (int) mc.player.posY >> 4) << 4;
        zOffset = (zSet - (int) mc.player.posZ >> 4) << 4;


    }

    @Override
    public void toggleState(){
        if(isSpoofing)
            this.onDisable();
        else
            this.onEnable();
    }

    @Override
    public void onEnable(){
        lastTitle = Display.getTitle();
        Display.setTitle(lastTitle + spoofingTitle);
        isSpoofing = true;
    }

    @Override
    public void onDisable(){
        Display.setTitle(lastTitle);
        isSpoofing = false;
    }

    public static int getX(){
        return isSpoofing ? xOffset : 0;
    }

    public static int getY(){
        return isSpoofing ? yOffset : 0;
    }

    public static int getZ(){
        return isSpoofing ? zOffset : 0;
    }

    public static boolean getSpoofing(){
        return isSpoofing;
    }
}
