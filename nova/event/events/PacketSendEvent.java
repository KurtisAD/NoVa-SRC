package nova.event.events;

import net.minecraft.network.Packet;
import nova.event.Event;

public class PacketSendEvent implements Event {
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
