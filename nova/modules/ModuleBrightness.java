package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;

/**
 * Created by Skeleton Man on 7/19/2016.
 */
public class ModuleBrightness extends ModuleBase{
    float brightness;
    float defaultBrightness;

    public ModuleBrightness(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);

        this.brightness = 10024F;
        this.defaultBrightness = 0F;

        aliases.add("bright");
        aliases.add("fullbright");

        this.command = new Command(Nova, this, aliases, "Changes brightness");
        this.command.registerArg("set", this.getClass().getMethod("setBrightness", float.class), "how bright");
        this.defaultArg = "set";
    }

    @Override
    public void onEnable()
    {
        this.defaultBrightness = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = this.brightness;
        this.isEnabled = true;
    }

    @Override
    public void onDisable()
    {
        mc.gameSettings.gammaSetting = this.defaultBrightness > 1.0F ? 1.0F : this.defaultBrightness;
        this.isEnabled = false;
    }

    public void setBrightness(float howBright){
        this.brightness = howBright;
    }

    @Override
    public String getMetadata()
    {
        return this.brightness == 1024F ? "" :  "(" + Float.toString(brightness) + ")";
    }
}
