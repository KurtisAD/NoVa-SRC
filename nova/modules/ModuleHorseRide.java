package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Nova;
import nova.core.RegisterArgument;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleHorseRide extends ModuleBase{
    boolean horseJump;

    // TODO: fix horse jump

    public ModuleHorseRide(Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.aliases.add("horse");
        this.description = ("Can ride all horses; IN DEVELOPMENT AND TESTING");
    }
    // Information is in EntityHorse.isTame()

    @RegisterArgument(name = "jump", description = "Max jump everytime when jumping on horses")
    public void toggleJump(){
        this.horseJump = !this.horseJump;
    }

    public void onTick(PlayerTickEvent e)
    {
        if(this.isEnabled && horseJump) {
            mc.player.horseJumpPower = 1.0F;
        }
    }

    @Override
    public String getMetadata(){
        return horseJump ? "(Jump)" : "";
    }
}
