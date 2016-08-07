package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Nova;
import nova.Command;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

public class ModuleAutowalk extends ModuleBase{


	public ModuleAutowalk(Nova Nova, Minecraft mc) throws NoSuchMethodException {
		super(Nova, mc);
		this.command = new Command(Nova, this, aliases, "as if you were holding \"W\"");

		loadModule();
	}


	@Override
	public void onDisable()
	{
		this.isEnabled = false;
		mc.gameSettings.keyBindForward.pressed = false;

	}

	@EventHandler
	public void onTick(PlayerTickEvent e)
	{
		if(this.isEnabled) {
			mc.gameSettings.keyBindForward.pressed = true;
		}
		
	}

}
