package me.deftware.client.framework.event.events;

import me.deftware.client.framework.math.ChunkPosition;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.world.level.ChunkPos;
import me.deftware.client.framework.event.Event;

public class EventChunkDataReceive extends Event {

    private final ChunkPos rawPos;

    public boolean isInitialFullChunk, updatedIsFullChunk;

    private final ClientboundLevelChunkWithLightPacket rootPacket;

    public ClientboundLevelChunkWithLightPacket getRootPacket() {
        return rootPacket;
    }

    public ChunkPosition getPos() {
        return (ChunkPosition) (Object) rawPos;
    }

    public EventChunkDataReceive(ClientboundLevelChunkWithLightPacket rootPacket) {
        this.rootPacket = rootPacket;
        this.rawPos = new ChunkPos(rootPacket.x(), rootPacket.z());
        isInitialFullChunk = rootPacket.isSkippable(); // TODO: Verify isWritingErrorSkippable
        updateFullChunk(rootPacket);
    }

    private void updateFullChunk(ClientboundLevelChunkWithLightPacket rootPacket) {
        updatedIsFullChunk = rootPacket.isSkippable();
    }

}
