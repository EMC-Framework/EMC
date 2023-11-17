package me.deftware.client.framework.event.events;

import me.deftware.client.framework.math.ChunkPosition;
import me.deftware.client.framework.event.Event;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.util.math.ChunkPos;

public class EventChunkDataReceive extends Event {

    private final ChunkPos rawPos;

    public boolean isInitialFullChunk, updatedIsFullChunk;

    private final ChunkDataS2CPacket rootPacket;

    public ChunkDataS2CPacket getRootPacket() {
        return rootPacket;
    }

    public ChunkPosition getPos() {
        return (ChunkPosition) rawPos;
    }

    public EventChunkDataReceive(ChunkDataS2CPacket rootPacket) {
        this.rootPacket = rootPacket;
        this.rawPos = new ChunkPos(rootPacket.getChunkX(), rootPacket.getChunkZ());
        isInitialFullChunk = rootPacket.isWritingErrorSkippable(); // TODO: Verify isWritingErrorSkippable
        updateFullChunk(rootPacket);
    }

    private void updateFullChunk(ChunkDataS2CPacket rootPacket) {
        updatedIsFullChunk = rootPacket.isWritingErrorSkippable();
    }

}
