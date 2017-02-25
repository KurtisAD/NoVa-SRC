package nova.event.events;

import net.minecraft.network.Packet;
import nova.event.Event;


public class PacketReceivedEvent implements Event {

	Packet packet;
	
	public PacketReceivedEvent(Packet packet)
	{
		this.packet = packet;
	}
	
	public Packet getPacket()
	{
		return this.packet;
	}
}
