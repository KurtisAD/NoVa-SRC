package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import nova.StaticNova;
import nova.Command;
import nova.events.EventHandler;
import nova.events.PacketSendEvent;

public class ModuleFreecam extends ModuleBase {
	double x, y, z;
	float yaw, pitch;
    private EntityOtherPlayerMP freecamEntity;

	public ModuleFreecam(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
		super(Nova,mc);
		this.command = new Command(Nova, this, aliases, "Frees your camera");

		loadModule();
	}

	@Override
	public void onEnable()
	{
		x = mc.thePlayer.posX;
		y = mc.thePlayer.posY;
		z = mc.thePlayer.posZ;
		yaw = mc.thePlayer.rotationYaw;
		pitch = mc.thePlayer.rotationPitch;

		freecamEntity = new EntityOtherPlayerMP(mc.theWorld, mc.getSession().getProfile());
		freecamEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
		freecamEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
		mc.theWorld.addEntityToWorld(-2, freecamEntity);
		StaticNova.Nova.getModule(ModuleFly.class).onEnable();
		mc.thePlayer.noClip = true;


		this.isEnabled = true;
	}

	@EventHandler
	public boolean onPacket(PacketSendEvent e){
		if(isEnabled){
			if (e.getPacket() instanceof CPacketPlayer){
				return false;
			}
		}
		return true;
	}

	@Override
	public void onDisable()
	{
		try {
			mc.thePlayer.setPositionAndRotation(freecamEntity.posX, freecamEntity.posY, freecamEntity.posZ, freecamEntity.rotationYaw, freecamEntity.rotationPitch);
		} catch (NullPointerException e){
			mc.thePlayer.setPosition(x,  y,  z);
			mc.thePlayer.rotationPitch = pitch;
			mc.thePlayer.rotationYaw = yaw;
		}
		mc.thePlayer.noClip = false;
		mc.theWorld.removeEntityFromWorld(-2);
		StaticNova.Nova.getModule(ModuleFly.class).onDisable();

		this.isEnabled = false;
	}
}
