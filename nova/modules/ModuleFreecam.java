package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import nova.Command;
import nova.StaticNova;
import nova.events.EventHandler;
import nova.events.PacketSendEvent;

public class ModuleFreecam extends ModuleBase {
    // TODO: do some tests to see if encryption can be easily seeded

	double x, y, z;
	float yaw, pitch;
    private EntityOtherPlayerMP freecamEntity;

	public ModuleFreecam(nova.Nova Nova, Minecraft mc) {
		super(Nova, mc);
		this.command = new Command(Nova, this, aliases, "Frees your camera");
	}

	@Override
	public void onEnable()
	{
		x = mc.player.posX;
		y = mc.player.posY;
		z = mc.player.posZ;
		yaw = mc.player.rotationYaw;
		pitch = mc.player.rotationPitch;

		freecamEntity = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
		freecamEntity.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch);
		freecamEntity.rotationYawHead = mc.player.rotationYawHead;
		mc.world.addEntityToWorld(-2, freecamEntity);
		StaticNova.Nova.getModule(ModuleFly.class).onEnable();
        StaticNova.Nova.getModule(ModuleNoclip.class).onEnable();

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
			mc.player.setPositionAndRotation(freecamEntity.posX, freecamEntity.posY, freecamEntity.posZ, freecamEntity.rotationYaw, freecamEntity.rotationPitch);
		} catch (NullPointerException e) {
			mc.player.setPosition(x, y, z);
			mc.player.rotationPitch = pitch;
			mc.player.rotationYaw = yaw;
		}
		mc.player.noClip = false;
		mc.world.removeEntityFromWorld(-2);
		StaticNova.Nova.getModule(ModuleFly.class).onDisable();
        StaticNova.Nova.getModule(ModuleNoclip.class).onDisable();

		this.isEnabled = false;
	}
}
