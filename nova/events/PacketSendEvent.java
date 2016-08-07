package nova.events;

import net.minecraft.network.Packet;

public class PacketSendEvent{
    private Packet packet;
    
    public PacketSendEvent(final Packet a) {
        this.packet = a;
    }
    
    public void setPacket(final Packet a) {
        this.packet = a;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
}
