package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventCollideCheck;
import me.deftware.client.framework.global.types.BlockProperty;
import me.deftware.client.framework.global.types.PropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.math.position.DoubleBlockPosition;
import me.deftware.mixin.imp.IMixinAbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinAbstractBlock implements IMixinAbstractBlock {

    @Shadow @Final
    public float slipperiness;

    /* FIXMEW
    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(IBlockState blockState_1, IBlockAccess blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<AxisAlignedBB> ci) {
        EventCollideCheck event = new EventCollideCheck(
                me.deftware.client.framework.world.block.Block.newInstance(blockState_1.getBlock()),
                DoubleBlockPosition.fromMinecraftBlockPos(blockPos_1)
        ).broadcast();
        if (event.updated) {
            if (event.canCollide) {
                ci.setReturnValue(Block.NULL_AABB);
            }
        }
    }*/

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> callback) {
        PropertyManager<BlockProperty> blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive()) {
            int id = Block.blockRegistry.getIDForObject(((Block) (Object) this));
            if (blockProperties.contains(id))
                callback.setReturnValue(blockProperties.get(id).getLuminance());
        }
    }

    @Override
    public float getTheSlipperiness() {
        return this.slipperiness;
    }

}
