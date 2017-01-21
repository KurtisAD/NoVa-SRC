package nova.modules;

import net.minecraft.client.Minecraft;
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
        this.description = ("Flies");
        this.defaultArg = "speed";

		this.speed = 0.05F;
	}


	@Override
	public void onEnable()
	{
		mc.player.capabilities.isFlying = true;
        StaticNova.Nova.getModule(ModuleSpeed.class).onDisable();
		StaticNova.Nova.getModule(ModuleElytraFly.class).onDisable();
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
		return "(" + (speed == 0.05F ? "Ready" : Float.toString(speed)) + ")";
	}
}
