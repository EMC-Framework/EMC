package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.network.PacketRegistry;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.packet.Packet;

/**
 * Triggered when packet is being sent to the server
 */
public class EventPacketSend extends Event {

    private Packet<?> packet;
    private final PacketWrapper wrapper;

    public EventPacketSend(Packet<?> packet) {
        this.packet = packet;
        this.wrapper = PacketRegistry.INSTANCE.translate(packet);
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public void setPacket(PacketWrapper packet) {
        this.packet = packet.getPacket();
    }

    public PacketWrapper getIPacket() {
        return wrapper;
    }

}
