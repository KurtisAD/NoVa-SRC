package nova.modules;

import nova.core.EventHandler;
import nova.events.PlayerTickEvent;

public class ModuleAutowalk extends ModuleBase{


    public ModuleAutowalk() {
        super();
        this.description = ("as if you were holding \"W\"");
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
