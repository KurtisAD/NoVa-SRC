package nova.events;

import net.minecraft.network.Packet;


public class PacketReceivedEvent {

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
