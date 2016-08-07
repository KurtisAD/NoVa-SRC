package nova.modules;

import com.google.gson.annotations.Expose;
import nova.Nova;
import nova.core.Util;
import nova.events.EventHandler;
import nova.Command;
import nova.events.PlayerTickEvent;

import net.minecraft.client.Minecraft;

public class ModuleFly extends ModuleBase 
{
	@Expose
	float speed;

	/*
	public ModuleFly(V V) 
	{
		super(V);
		// TODO Auto-generated constructor stub

		this.speed = 0.05F;

		this.needsTick = true;

		this.command = new Command(this.V, this, aliases, "flies");
		this.command.registerArg("speed", new Class[] { Float.class }, "how fast, default 0.05F, highest speed on ice is 0.065, with speed II is 0.09");
		this.defaultArg = "speed";

	}
	*/

	public ModuleFly(Nova Nova, Minecraft mc) throws NoSuchMethodException {
		super(Nova, mc);
		this.command = new Command(Nova, this, aliases, "Flies");
		this.command.registerArg("speed", this.getClass().getMethod("changeSpeed", float.class), "how fast");
		this.defaultArg = "speed";

		this.speed = 0.05F;

		loadModule();
	}

	@Override
	public void saveModule(){
		json.add("speed", Util.getGson().toJsonTree(speed));
		super.saveModule();
	}

	@Override
	public void load(){
		super.load();
		speed = Util.getGson().fromJson(json.get("speed"), Float.class);
	}

	@Override
	public void onEnable()
	{
		mc.thePlayer.capabilities.isFlying = true;
		this.isEnabled = true;
	}

	@Override
	public void onDisable()
	{
		mc.thePlayer.capabilities.isFlying = false;
		if (!mc.thePlayer.capabilities.isCreativeMode){
			mc.thePlayer.capabilities.allowFlying = false;
		}
		this.isEnabled = false;
	}

	@EventHandler
	public void onTick(PlayerTickEvent e)
	{
		if(this.isEnabled) {
			mc.thePlayer.capabilities.isFlying = true;
			mc.thePlayer.capabilities.setFlySpeed(speed);
		}

	}

	public void changeSpeed(float speed){
		this.speed = speed;
	}

	@Override
	public String getMetadata()
	{
		return this.speed == 0.05F ? "" :  "(" + Float.toString(speed) + ")";
	}
}
