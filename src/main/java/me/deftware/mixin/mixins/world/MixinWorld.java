package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.event.events.EventTileBlockRemoved;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.world.Biome;
import net.minecraft.client.Minecraft;
import me.deftware.client.framework.world.chunk.ChunkAccessor;
import net.minecraft.util.math.BlockPos;
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
	public abstract net.minecraft.tileentity.TileEntity getTileEntity(BlockPos pos);

	@Unique
	public final HashMap<net.minecraft.tileentity.TileEntity, TileEntity> emcTileEntities = new HashMap<>();

	@Inject(method = "addTileEntity", at = @At("TAIL"))
	public void addBlockEntity(net.minecraft.tileentity.TileEntity blockEntity, CallbackInfoReturnable<Boolean> ci) {
		emcTileEntities.put(blockEntity, TileEntity.newInstance(blockEntity));
	}

	@Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z", ordinal = 1))
	private boolean onRemoveEntityIf(List<net.minecraft.tileentity.TileEntity> list, Collection<net.minecraft.tileentity.TileEntity> entities) {
		for (net.minecraft.tileentity.TileEntity entity : entities) {
			new EventTileBlockRemoved(emcTileEntities.remove(entity)).broadcast();
		}
		return list.removeAll(entities);
	}

	@SuppressWarnings("RedundantCast")
	@Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z"))
	private boolean onRemoveEntity(List<net.minecraft.tileentity.TileEntity> list, Object entity) {
		new EventTileBlockRemoved(emcTileEntities.remove((net.minecraft.tileentity.TileEntity) entity)).broadcast();
		return list.remove((net.minecraft.tileentity.TileEntity) entity);
	}

	@Inject(method = "removeTileEntity", at = @At("HEAD"))
	public void removeBlockEntity(BlockPos pos, CallbackInfo info) {
		net.minecraft.tileentity.TileEntity blockEntity = this.getTileEntity(pos);
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
		return ((World) (Object) this).getWorldTime();
	}

	@Override
	public int _getWorldHeight() {
		return ((World) (Object) this).getHeight();
	}

	@Override
	public int _getBlockLightLevel(BlockPosition position) {
		return ((World) (Object) this).getLight(position.getMinecraftBlockPos());
	}

	@Override
	public void _disconnect() {
		((World) (Object) this).sendQuittingDisconnectingPacket();
	}

	@Override
	public ChunkAccessor getChunk(int x, int z) {
		return (ChunkAccessor) ((World) (Object) this).getChunk(x, z);
	}

	@Override
	public int _getDimension() {
		return Minecraft.getMinecraft().player.dimension;
	}

	@Override
	public me.deftware.client.framework.world.block.BlockState _getBlockState(BlockPosition position) {
		return new me.deftware.client.framework.world.block.BlockState(
				((World) (Object) this).getBlockState(position.getMinecraftBlockPos())
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TileEntity> T getTileEntityByReference(net.minecraft.tileentity.TileEntity reference) {
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
				((World) (Object) this).getBiome(
						Minecraft.getMinecraft().player.getPosition()
				)
		);
	}

}
