package nova.module.modules;

import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PlayerTickEvent;
import nova.module.ModuleBase;

public class ModuleYaw extends ModuleBase
{
	public float yaw;

    public ModuleYaw() {
        super();
        this.description = ("Locks your yaw to the closest 45 degrees.");

		this.defaultArg = "deg";

		this.yaw = 0F;
	}

	@RegisterArgument(name = "deg", description = "Optionally, you can lock your yaw to a specific degree")
	public void changeYaw(float deg) {
		this.yaw = deg;
	}

    @RegisterArgument(name = "to", description = "Takes an X and Z coordinate to change your angle to")
    public void lookTowards(int x, int z) {
        double diffX = x + 0.5 - mc.player.posX;
        double diffZ = z + 0.5 - mc.player.posZ;
        yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;

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
