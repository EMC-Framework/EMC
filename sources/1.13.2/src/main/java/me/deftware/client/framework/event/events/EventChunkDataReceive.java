package me.deftware.client.framework.event.events;

import me.deftware.client.framework.math.ChunkPosition;
import me.deftware.client.framework.event.Event;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.ChunkPos;

public class EventChunkDataReceive extends Event {

    private final ChunkPos rawPos;

    public boolean isInitialFullChunk, updatedIsFullChunk;

    private final SPacketChunkData rootPacket;

    public SPacketChunkData getRootPacket() {
        return rootPacket;
    }

    public ChunkPosition getPos() {
        return (ChunkPosition) rawPos;
    }

    public EventChunkDataReceive(SPacketChunkData rootPacket) {
        this.rootPacket = rootPacket;
        this.rawPos = new ChunkPos(rootPacket.getChunkX(), rootPacket.getChunkZ());
        isInitialFullChunk = rootPacket.isFullChunk();
        updateFullChunk(rootPacket);
    }

    private void updateFullChunk(SPacketChunkData rootPacket) {
        updatedIsFullChunk = rootPacket.isFullChunk();
    }

}
