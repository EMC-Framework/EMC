package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.event.events.EventCollideCheck;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.global.types.BlockProperty;
import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.global.types.PropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class MixinBlockState implements me.deftware.client.framework.world.block.BlockState {

	@Inject(method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
	public void getOutlineShape(BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> ci) {
		EventCollideCheck event = new EventCollideCheck(
				this.getBlock(),
				(BlockPosition) pos
		).broadcast();
		if (event.updated) {
			if (event.canCollide) {
				ci.setReturnValue(Shapes.empty());
			}
		}
	}

	@Inject(method = "getLightEmission", at = @At("HEAD"), cancellable = true)
	public void getLuminance(CallbackInfoReturnable<Integer> callback) {
		PropertyManager<BlockProperty> blockProperties = Bootstrap.blockProperties;
		if (blockProperties.isActive()) {
			int id = BuiltInRegistries.BLOCK.getId(
					((BlockBehaviour.BlockStateBase) (Object) this).getBlock()
			);
			if (blockProperties.contains(id))
				callback.setReturnValue(blockProperties.get(id).getLuminance());
		}
	}

	@Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
	public void getCollisionShape(BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> ci) {
		int id = BuiltInRegistries.BLOCK.getId(
				((BlockBehaviour.BlockStateBase) (Object) this).getBlock()
		);
		BlockPropertyManager blockProperties = Bootstrap.blockProperties;
		if (blockProperties.contains(id)) {
			BlockProperty property = blockProperties.get(id);
			if (property.getVoxelShape() != null) {
				ci.setReturnValue((VoxelShape) property.getVoxelShape());
				return;
			}
		}
		if (this.getBlock() instanceof SweetBerryBushBlock && GameMap.INSTANCE.get(GameKeys.FULL_BERRY_VOXEL, false)) {
			ci.setReturnValue(Shapes.block());
		}
	}

	@Unique
	@Override
	public Block getBlock() {
		return (Block) ((BlockBehaviour.BlockStateBase) (Object) this).getBlock();
	}

	@Unique
	@Override
	public boolean isPathFindable() {
		return ((BlockBehaviour.BlockStateBase) (Object) this).isPathfindable(PathComputationType.LAND);
	}

}
