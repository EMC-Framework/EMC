package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.math.position.ChunkBlockPosition;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.world.ChunkCoordIntPair;

public class EventChunkDataReceive extends Event {

    private final ChunkCoordIntPair rawPos;
    private final ChunkBlockPosition pos;

    public boolean isInitialFullChunk, updatedIsFullChunk;

    private final S21PacketChunkData rootPacket;

    public S21PacketChunkData getRootPacket() {
        return rootPacket;
    }

    public ChunkCoordIntPair getRawPos() {
        return rawPos;
    }

    public ChunkBlockPosition getPos() {
        return pos;
    }

    public EventChunkDataReceive(S21PacketChunkData rootPacket) {
        this.rootPacket = rootPacket;
        this.rawPos = new ChunkCoordIntPair(rootPacket.getChunkX(), rootPacket.getChunkZ());
        this.pos = new ChunkBlockPosition(rawPos);
        isInitialFullChunk = rootPacket.func_149274_i();
        updateFullChunk(rootPacket);
    }

    private void updateFullChunk(S21PacketChunkData rootPacket) {
        updatedIsFullChunk = rootPacket.func_149274_i();
    }

}
