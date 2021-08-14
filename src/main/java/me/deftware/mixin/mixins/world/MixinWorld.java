package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.event.events.EventTileBlockRemoved;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.world.Biome;
import me.deftware.client.framework.world.classifier.BlockClassifier;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
@Mixin(World.class)
public abstract class MixinWorld implements me.deftware.client.framework.world.World {

	@Shadow
	public abstract BlockEntity getBlockEntity(BlockPos pos);

	@Shadow
	@Final
	protected List<BlockEntity> unloadedBlockEntities;

	@Unique
	public final HashMap<BlockEntity, TileEntity> emcTileEntities = new HashMap<>();

	@Inject(method = "addBlockEntity", at = @At("HEAD"))
	public void addBlockEntity(BlockEntity blockEntity, CallbackInfoReturnable<Boolean> ci) {
		emcTileEntities.put(blockEntity, TileEntity.newInstance(blockEntity));
	}

	@Inject(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z", ordinal = 1))
	private void onRemoveEntityIf(CallbackInfo info) {
		for (BlockEntity entity : this.unloadedBlockEntities) {
			new EventTileBlockRemoved(emcTileEntities.remove(entity)).broadcast();
		}
	}

	@Redirect(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z"))
	private boolean onRemoveEntity(List<BlockEntity> list, Object entity) {
		new EventTileBlockRemoved(emcTileEntities.remove((BlockEntity) entity)).broadcast();
		return list.remove((BlockEntity) entity);
	}

	@Inject(method = "removeBlockEntity", at = @At("HEAD"))
	public void removeBlockEntity(BlockPos pos, CallbackInfo info) {
		BlockEntity blockEntity = this.getBlockEntity(pos);
		if (blockEntity != null) {
			new EventTileBlockRemoved(emcTileEntities.remove(blockEntity)).broadcast();
		}
	}

	@Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("TAIL"))
	public void setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> info) {
		if (state.isAir()) {
			BlockClassifier.getClassifiers().forEach(blockClassifier -> {
				if (blockClassifier.getClassifiedBlocks().containsKey(pos.asLong())) {
					blockClassifier.getClassifiedBlocks().remove(pos.asLong());
				}
			});
		}
	}


	@Override
	public Stream<TileEntity> getLoadedTileEntities() {
		return emcTileEntities.values().stream();
	}

	@Override
	public int _getDifficulty() {
		return ((World) (Object) this).getDifficulty().getId();
	}

	@Override
	public long _getWorldTime() {
		return ((World) (Object) this).getTimeOfDay();
	}

	@Override
	public int _getWorldHeight() {
		return ((World) (Object) this).getHeight();
	}

	@Override
	public int _getBlockLightLevel(BlockPosition position) {
		return ((World) (Object) this).getLightLevel(position.getMinecraftBlockPos());
	}

	@Override
	public void _disconnect() {
		((World) (Object) this).disconnect();
	}

	@Override
	public int _getDimension() {
		RegistryKey<World> key = ((World) (Object) this).getRegistryKey();
		if (World.END.equals(key)) {
			return 1;
		} else if (World.OVERWORLD.equals(key)) {
			return 0;
		} else if (World.NETHER.equals(key)) {
			return -1;
		}
		return -2;
	}

	@Override
	public me.deftware.client.framework.world.block.BlockState _getBlockState(BlockPosition position) {
		return new me.deftware.client.framework.world.block.BlockState(
				((World) (Object) this).getBlockState(position.getMinecraftBlockPos())
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TileEntity> T getTileEntityByReference(BlockEntity reference) {
		if (reference != null) {
			return (T) emcTileEntities.get(reference);
		}
		return null;
	}

	@Unique
	private static final Biome _biome = new Biome();

	@Override
	public Biome _getBiome() {
		return _biome.setReference(
				((ClientWorld) (Object) this).getBiome(
						MinecraftClient.getInstance().player.getBlockPos()
				)
		);
	}

}
