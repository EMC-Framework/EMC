package me.deftware.mixin.mixins.network;

import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkDeltaUpdateS2CPacket.class)
public interface ChunkDeltaAccessor {

    @Accessor("records")
    ChunkDeltaUpdateS2CPacket.ChunkDeltaRecord[] getRecords();

    @Accessor("chunkPos")
    ChunkPos getChunkPos();

}
