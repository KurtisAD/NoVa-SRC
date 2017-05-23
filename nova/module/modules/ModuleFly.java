package nova.module.modules;

import nova.Nova;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;

public class ModuleFly extends ModuleBase
{
	@Saveable
	public float speed;

	public ModuleFly() {
		super();
		this.description = "Flies";
		this.defaultArg = "speed";

		this.speed = 0.05F;
	}


	@Override
	public void onEnable()
	{
		mc.player.capabilities.isFlying = true;
		Nova.getModule(ModuleElytraFly.class).onDisable();
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
