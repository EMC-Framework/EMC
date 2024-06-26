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
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.Registries;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class MixinBlockState implements me.deftware.client.framework.world.block.BlockState {

	@Inject(method = "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
	public void getOutlineShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> ci) {
		EventCollideCheck event = new EventCollideCheck(
				this.getBlock(),
				(BlockPosition) pos
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
			int id = Registries.BLOCK.getRawId(
					((AbstractBlock.AbstractBlockState) (Object) this).getBlock()
			);
			if (blockProperties.contains(id))
				callback.setReturnValue(blockProperties.get(id).getLuminance());
		}
	}

	@Inject(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
	public void getCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> ci) {
		int id = Registries.BLOCK.getRawId(
				((AbstractBlock.AbstractBlockState) (Object) this).getBlock()
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
			ci.setReturnValue(VoxelShapes.fullCube());
		}
	}

	@Unique
	@Override
	public Block getBlock() {
		return (Block) ((AbstractBlock.AbstractBlockState) (Object) this).getBlock();
	}

	@Unique
	@Override
	public boolean isPathFindable() {
		return ((AbstractBlock.AbstractBlockState) (Object) this).canPathfindThrough(NavigationType.LAND);
	}

}
