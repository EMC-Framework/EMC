package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.math.position.ChunkBlockPosition;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.ChunkPos;

public class EventChunkDataReceive extends Event {

    private final ChunkPos rawPos;
    private final ChunkBlockPosition pos;

    public boolean isInitialFullChunk, updatedIsFullChunk;

    private final SPacketChunkData rootPacket;

    public SPacketChunkData getRootPacket() {
        return rootPacket;
    }

    public ChunkPos getRawPos() {
        return rawPos;
    }

    public ChunkBlockPosition getPos() {
        return pos;
    }

    public EventChunkDataReceive(SPacketChunkData rootPacket) {
        this.rootPacket = rootPacket;
        this.rawPos = new ChunkPos(rootPacket.getChunkX(), rootPacket.getChunkZ());
        this.pos = new ChunkBlockPosition(rawPos);
        isInitialFullChunk = rootPacket.isFullChunk();
        updateFullChunk(rootPacket);
    }

    private void updateFullChunk(SPacketChunkData rootPacket) {
        updatedIsFullChunk = rootPacket.isFullChunk();
    }

}
