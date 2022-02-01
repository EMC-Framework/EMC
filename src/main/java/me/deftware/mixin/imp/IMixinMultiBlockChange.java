package me.deftware.mixin.imp;

import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.world.ChunkCoordIntPair;

public interface IMixinMultiBlockChange {

    S22PacketMultiBlockChange.BlockUpdateData[] getRecords();

    ChunkCoordIntPair getChunkPos();

}
