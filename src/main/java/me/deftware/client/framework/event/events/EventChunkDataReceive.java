package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.wrappers.world.IChunkPos;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.world.ChunkCoordIntPair;

public class EventChunkDataReceive extends Event {

    private ChunkCoordIntPair rawPos;
    private IChunkPos pos;

    public boolean isInitialFullChunk, updatedIsFullChunk;

    private S21PacketChunkData rootPacket;

    public S21PacketChunkData getRootPacket() {
        return rootPacket;
    }

    public ChunkCoordIntPair getRawPos() {
        return rawPos;
    }

    public IChunkPos getPos() {
        return pos;
    }

    public EventChunkDataReceive(S21PacketChunkData rootPacket) {
        this.rootPacket = rootPacket;
        this.rawPos = new ChunkCoordIntPair(rootPacket.getChunkX(), rootPacket.getChunkZ());
        this.pos = new IChunkPos(rawPos);
        isInitialFullChunk = rootPacket.func_149274_i();
        updateFullChunk(rootPacket);
    }

    private void updateFullChunk(S21PacketChunkData rootPacket) {
        updatedIsFullChunk = rootPacket.func_149274_i();
    }
}
