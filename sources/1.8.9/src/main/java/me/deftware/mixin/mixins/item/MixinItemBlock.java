package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.world.block.Block;
import net.minecraft.item.ItemBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemBlock.class)
public class MixinItemBlock extends MixinItem implements me.deftware.client.framework.item.items.BlockItem {

    @Unique
    @Override
    public Block getBlock() {
        return (Block) ((ItemBlock) (Object) this).getBlock();
    }

}
