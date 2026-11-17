package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.chunk.SectionAccessor;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelChunkSection.class)
public class MixinChunkSection implements SectionAccessor {

    @Override
    public Block getBlock(int x, int y, int z) {
        return (Block) ((LevelChunkSection) (Object) this).getBlockState(x, y, z).getBlock();
    }

}
