package nova.modules;

import com.google.gson.annotations.Expose;
import nova.Nova;
import nova.core.Util;
import nova.events.EventHandler;
import nova.Command;
import net.minecraft.client.Minecraft;
import nova.events.PlayerTickEvent;

public class ModuleYaw extends ModuleBase 
{
	@Expose
	public float yaw;

	public ModuleYaw(Nova Nova, Minecraft mc) throws NoSuchMethodException {
		super(Nova, mc);
		this.command = new Command(Nova, this, aliases, "Locks your yaw to the closest 45 degrees.");
		this.command.registerArg("deg", this.getClass().getMethod("changeYaw", float.class), "Optionally, you can lock your yaw to a specific degree.");

		this.defaultArg = "deg";

		this.yaw = 0F;

		loadModule();
	}

	@Override
	public void saveModule(){
		json.add("yaw", Util.getGson().toJsonTree(yaw));
		super.saveModule();
	}

	@Override
	public void load(){
		super.load();
		yaw = Util.getGson().fromJson(json.get("yaw"), Float.class);
	}

	@Override
	public void onEnable()
	{
		this.isEnabled = true;
		this.yaw = Math.round((mc.thePlayer.rotationYaw + 1F) / 45F) * 45F;
	}

	@EventHandler
	public void onTick(PlayerTickEvent e)
	{
		if(this.isEnabled) {
			mc.thePlayer.rotationYaw = this.yaw;
		}
		
	}


	public void changeYaw(float deg){
		this.yaw = deg;
	}

	@Override
	public String getMetadata()
	{
		return "(" + Float.toString(this.yaw) + ")";
	}

}
