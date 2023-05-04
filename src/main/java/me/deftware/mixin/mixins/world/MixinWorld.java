package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.event.events.EventTileBlockRemoved;
import me.deftware.client.framework.world.Biome;
import me.deftware.client.framework.world.chunk.ChunkAccessor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
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

	@Unique
	public final HashMap<BlockEntity, TileEntity> emcTileEntities = new HashMap<>();

	@Inject(method = "addBlockEntity", at = @At("HEAD"))
	public void addBlockEntity(BlockEntity blockEntity, CallbackInfoReturnable<Boolean> ci) {
		emcTileEntities.put(blockEntity, TileEntity.newInstance(blockEntity));
	}

	@Redirect(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z", ordinal = 0))
	private boolean onRemoveEntity(List<BlockEntity> list, Collection<BlockEntity> entities) {
		for (BlockEntity entity : entities) {
			new EventTileBlockRemoved(emcTileEntities.remove(entity)).broadcast();
		}
		return list.removeAll(entities);
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
		return ((World) (Object) this).getLightLevel((BlockPos) position);
	}

	@Override
	public void _disconnect() {
		((World) (Object) this).disconnect();
	}

	@Override
	public ChunkAccessor getChunk(int x, int z) {
		return (ChunkAccessor) ((World) (Object) this).getChunk(x, z);
	}

	@Override
	public boolean _hasChunk(int x, int z) {
		return ((World) (Object) this).isChunkLoaded(x, z);
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
		return (me.deftware.client.framework.world.block.BlockState) ((World) (Object) this).getBlockState((BlockPos) position);
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

	@Unique
	@Override
	public boolean rayTraceBlocks(Vector3<Double> start, Vector3<Double> end) {
		RaycastContext context = new RaycastContext(
				(Vec3d) start, (Vec3d) end,
				RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE,
				MinecraftClient.getInstance().getCameraEntity());
		return ((World) (Object) this).raycast(context).getType() != HitResult.Type.MISS;
	}

}
