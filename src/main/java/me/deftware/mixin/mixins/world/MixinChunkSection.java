package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.chunk.SectionAccessor;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExtendedBlockStorage.class)
public class MixinChunkSection implements SectionAccessor {

    @Override
    public Block getBlock(int x, int y, int z) {
        return BlockRegistry.INSTANCE.getBlock(((ExtendedBlockStorage) (Object) this).get(x, y, z).getBlock());
    }

}
