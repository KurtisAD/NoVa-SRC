package nova.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import nova.Command;
import nova.events.EventHandler;
import nova.events.PacketSendEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class ModuleBlink extends ModuleBase
{

    private ArrayList delayedPackets;
    private EntityOtherPlayerMP blinkEntity;
    private double timer;

    public ModuleBlink(nova.Nova Nova, Minecraft mc) throws NoSuchMethodException {
        super(Nova, mc);

        this.command = new Command(Nova, this, aliases, "Synthetic lagswitch.");
        this.delayedPackets = new ArrayList();
    }

    @Override
    public void onDisable()
    {
        this.isEnabled = false;
        if (this.blinkEntity != null) {
            mc.theWorld.removeEntityFromWorld(-1);
            this.blinkEntity = null;
        }
        final Iterator<Packet> iterator = this.delayedPackets.iterator();
        while (iterator.hasNext()) {
            final Packet a;
            if ((a = iterator.next()) != null) {
                mc.thePlayer.connection.sendPacket(a);
            }
        }
        this.delayedPackets.clear();
    }

    @Override
    public void onEnable()
    {
        this.isEnabled = true;
        if (mc.theWorld != null) {
            final double timer = 0;
            this.blinkEntity = new EntityOtherPlayerMP(mc.theWorld, mc.session.getProfile());
            this.blinkEntity.inventory = mc.thePlayer.inventory;
            this.blinkEntity.setPosition(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ);
            mc.theWorld.addEntityToWorld(-1, this.blinkEntity);
            this.timer = timer;
        }
    }

    @EventHandler
    public boolean onPacketSend(PacketSendEvent e) {
        if(this.isEnabled)
        {
            ++timer;
            if ((mc.thePlayer.motionY >= -0.2D) && (mc.thePlayer.motionZ == 0.0D) && (mc.thePlayer.motionX == 0.0D)) {
                return false;
            }
            if (e.getPacket() instanceof CPacketPlayer || e.getPacket() instanceof CPacketPlayerTryUseItem || e.getPacket() instanceof CPacketPlayerDigging) {
                this.delayedPackets.add(e.getPacket());
                return false;
            }
        }

        return true;
    }

    private String getTimeElapsed()
    {
        final int n = (int)this.timer / 20;
        String displayName = "Time(s): " + n;
        return displayName;
    }

    @Override
    public String getMetadata()
    {
        return "(" + getTimeElapsed() + ")";
    }
}
