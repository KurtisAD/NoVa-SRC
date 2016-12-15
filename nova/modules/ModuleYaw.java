package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Command;
import nova.Nova;
import nova.core.RegisterArgument;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

public class ModuleYaw extends ModuleBase 
{
	public float yaw;

	public ModuleYaw(Nova Nova, Minecraft mc) {
		super(Nova, mc);
		this.command = new Command(Nova, this, aliases, "Locks your yaw to the closest 45 degrees.");

		this.defaultArg = "deg";

		this.yaw = 0F;
	}

	@RegisterArgument(name = "deg", description = "Optionally, you can lock your yaw to a specific degree")
	public void changeYaw(float deg) {
		this.yaw = deg;
	}


	@Override
	public void onEnable()
	{
		this.isEnabled = true;
		this.yaw = Math.round((mc.player.rotationYaw + 1F) / 45F) * 45F;
	}

	@EventHandler
	public void onTick(PlayerTickEvent e)
	{
		if(this.isEnabled) {
			mc.player.rotationYaw = this.yaw;
		}
		
	}



	@Override
	public String getMetadata()
	{
		return "(" + Float.toString(this.yaw) + ")";
	}

}
