package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.wrappers.world.IChunkPos;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.ChunkPos;

public class EventChunkDataReceive extends Event {

    private ChunkPos rawPos;
    private IChunkPos pos;

    public boolean isInitialFullChunk, updatedIsFullChunk;

    private SPacketChunkData rootPacket;

    public SPacketChunkData getRootPacket() {
        return rootPacket;
    }

    public ChunkPos getRawPos() {
        return rawPos;
    }

    public IChunkPos getPos() {
        return pos;
    }

    public EventChunkDataReceive(SPacketChunkData rootPacket) {
        this.rootPacket = rootPacket;
        this.rawPos = new ChunkPos(rootPacket.getChunkX(), rootPacket.getChunkZ());
        this.pos = new IChunkPos(rawPos);
        isInitialFullChunk = rootPacket.doChunkLoad();
        updateFullChunk(rootPacket);
    }

    private void updateFullChunk(SPacketChunkData rootPacket) {
        updatedIsFullChunk = rootPacket.doChunkLoad();
    }
}
