package me.deftware.mixin.mixins.network;

import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundSectionBlocksUpdatePacket.class)
public interface ChunkDeltaAccessor {

    @Accessor("positions")
    short[] getPositions();

    @Accessor("states")
    BlockState[] getBlockStates();

    @Accessor("sectionPos")
    SectionPos getSectionPos();

}
