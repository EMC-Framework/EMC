package me.deftware.mixin.imp;

import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.math.ChunkPos;

public interface IMixinMultiBlockChange {

    SPacketMultiBlockChange.BlockUpdateData[] getRecords();

    ChunkPos getChunkPos();

}
