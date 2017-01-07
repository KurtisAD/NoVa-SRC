package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.Nova;
import nova.StaticNova;
import nova.core.RegisterArgument;
import nova.core.Saveable;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

public class ModuleFly extends ModuleBase 
{

	@Saveable
	public float speed;


	public ModuleFly(Nova Nova, Minecraft mc) {
		super(Nova, mc);
		this.command = new Command(Nova, this, aliases, "Flies");
		this.defaultArg = "speed";

		this.speed = 0.05F;
	}


	@Override
	public void onEnable()
	{
		mc.player.capabilities.isFlying = true;
        StaticNova.Nova.getModule(ModuleSpeed.class).onDisable();
        super.onEnable();
    }

	@Override
	public void onDisable()
	{
		mc.player.capabilities.isFlying = false;
		if (!mc.player.capabilities.isCreativeMode) {
			mc.player.capabilities.allowFlying = false;
		}

        super.onDisable();
    }

	@EventHandler
	public void onTick(PlayerTickEvent e)
	{
		if(this.isEnabled) {
			mc.player.capabilities.isFlying = true;
			mc.player.capabilities.setFlySpeed(speed);
		}

	}

	@RegisterArgument(name = "speed", description = "how fast")
	public void changeSpeed(float speed){
		this.speed = speed;
	}

	@Override
	public String getMetadata()
	{
        return "(" + (speed == 1.0f ? "Ready" : Float.toString(speed)) + ")";
    }
}
