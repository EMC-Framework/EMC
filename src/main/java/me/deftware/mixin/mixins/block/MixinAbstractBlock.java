package me.deftware.mixin.mixins.block;

import me.deftware.mixin.imp.IMixinAbstractBlock;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class MixinAbstractBlock implements IMixinAbstractBlock {

    @Shadow @Final
    private float slipperiness;


    @Shadow @Final
    private float velocityMultiplier;

    @Override
    public float getTheSlipperiness() {
        return this.slipperiness;
    }

    @Override
    public float getTheVelocityMultiplier() {
        return this.velocityMultiplier;
    }

}
