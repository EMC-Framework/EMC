package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventCollideCheck;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.global.types.BlockProperty;
import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.global.types.PropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.math.position.DoubleBlockPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockState.class)
public abstract class MixinBlockState {

    @Shadow
    public abstract Block getBlock();

	@Shadow
	public abstract FluidState getFluidState();


	@Inject(method = "Lnet/minecraft/block/BlockState;getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/EntityContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
	public void getOutlineShape(BlockView world, BlockPos pos, EntityContext context, CallbackInfoReturnable<VoxelShape> ci) {
		EventCollideCheck event = new EventCollideCheck(
				me.deftware.client.framework.world.block.Block.newInstance(this.getBlock()),
				DoubleBlockPosition.fromMinecraftBlockPos(pos)
		).broadcast();
		if (event.updated) {
			if (event.canCollide) {
				ci.setReturnValue(VoxelShapes.empty());
			}
		}
	}

	@Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
	public void getLuminance(CallbackInfoReturnable<Integer> callback) {
		PropertyManager<BlockProperty> blockProperties = Bootstrap.blockProperties;
		if (blockProperties.isActive()) {
			int id = Registry.BLOCK.getRawId(this.getBlock());
			if (blockProperties.contains(id))
				callback.setReturnValue(blockProperties.get(id).getLuminance());
		}
	}

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/EntityContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
	public void getCollisionShape(BlockView world, BlockPos pos, EntityContext context, CallbackInfoReturnable<VoxelShape> ci) {
		int id = Registry.BLOCK.getRawId(this.getBlock());
		BlockPropertyManager blockProperties = Bootstrap.blockProperties;
		if (blockProperties.contains(id)) {
			BlockProperty property = blockProperties.get(id);
			if (property.getVoxelShape() != null) {
				ci.setReturnValue(property.getVoxelShape().getMinecraftVoxelShape());
				return;
			}
		}
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		// Deprecated
		if (!this.getFluidState().isEmpty() && player != null) {
			boolean fullCube = GameMap.INSTANCE.get(GameKeys.FULL_LIQUID_VOXEL, false);
			if (fullCube) {
				if (!(pos.getX() == player.getBlockPos().getX() &&
						pos.getZ() == player.getBlockPos().getZ())) {
					fullCube = false;
				}
			}
			if (fullCube) ci.setReturnValue(VoxelShapes.fullCube());
		} else if (this.getBlock() instanceof SweetBerryBushBlock && GameMap.INSTANCE.get(GameKeys.FULL_BERRY_VOXEL, false)) {
				ci.setReturnValue(VoxelShapes.fullCube());
		}
	}

}
