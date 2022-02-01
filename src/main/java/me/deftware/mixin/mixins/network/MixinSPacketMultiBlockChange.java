package me.deftware.mixin.mixins.network;

import me.deftware.mixin.imp.IMixinMultiBlockChange;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.world.ChunkCoordIntPair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(S22PacketMultiBlockChange.class)
public class MixinSPacketMultiBlockChange implements IMixinMultiBlockChange {

    @Shadow
    private ChunkCoordIntPair chunkPosCoord;

    @Shadow
    private S22PacketMultiBlockChange.BlockUpdateData[] changedBlocks;

    @Override
    public S22PacketMultiBlockChange.BlockUpdateData[] getRecords() {
        return this.changedBlocks;
    }

    @Override
    public ChunkCoordIntPair getChunkPos() {
        return this.chunkPosCoord;
    }

}
