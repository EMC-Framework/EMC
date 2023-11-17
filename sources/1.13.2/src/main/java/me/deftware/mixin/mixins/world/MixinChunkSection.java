package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.chunk.SectionAccessor;
import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkSection.class)
public class MixinChunkSection implements SectionAccessor {

    @Override
    public Block getBlock(int x, int y, int z) {
        return (Block) ((ChunkSection) (Object) this).get(x, y, z).getBlock();
    }

}
