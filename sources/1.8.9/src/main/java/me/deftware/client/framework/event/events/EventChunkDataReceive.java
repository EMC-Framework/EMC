package me.deftware.client.framework.event.events;

import me.deftware.client.framework.math.ChunkPosition;
import me.deftware.client.framework.event.Event;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.world.ChunkCoordIntPair;

public class EventChunkDataReceive extends Event {

    private final ChunkCoordIntPair rawPos;

    public boolean isInitialFullChunk, updatedIsFullChunk;

    private final S21PacketChunkData rootPacket;

    public S21PacketChunkData getRootPacket() {
        return rootPacket;
    }

    public ChunkCoordIntPair getRawPos() {
        return rawPos;
    }

    public ChunkPosition getPos() {
        return (ChunkPosition) rawPos;
    }

    public EventChunkDataReceive(S21PacketChunkData rootPacket) {
        this.rootPacket = rootPacket;
        this.rawPos = new ChunkCoordIntPair(rootPacket.getChunkX(), rootPacket.getChunkZ());
        isInitialFullChunk = rootPacket.func_149274_i();
        updateFullChunk(rootPacket);
    }

    private void updateFullChunk(S21PacketChunkData rootPacket) {
        updatedIsFullChunk = rootPacket.func_149274_i();
    }

}
