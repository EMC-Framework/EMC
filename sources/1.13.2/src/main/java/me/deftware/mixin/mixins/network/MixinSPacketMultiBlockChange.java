package me.deftware.mixin.mixins.network;

import me.deftware.mixin.imp.IMixinMultiBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SPacketMultiBlockChange.class)
public class MixinSPacketMultiBlockChange implements IMixinMultiBlockChange {

    @Shadow
    private ChunkPos chunkPos;

    @Shadow
    private SPacketMultiBlockChange.BlockUpdateData[] changedBlocks;

    @Override
    public SPacketMultiBlockChange.BlockUpdateData[] getRecords() {
        return this.changedBlocks;
    }

    @Override
    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }

}
