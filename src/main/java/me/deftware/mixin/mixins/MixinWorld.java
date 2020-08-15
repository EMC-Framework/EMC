package me.deftware.mixin.mixins;


import me.deftware.client.framework.wrappers.entity.ITileEntity;
import me.deftware.mixin.imp.IMixinWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Mixin(World.class)
public abstract class MixinWorld implements IMixinWorld {

	@Shadow
	@Final
	public List<TileEntity> loadedTileEntityList;

	@Shadow
	@Final
	protected List<TileEntity> tileEntitiesToBeRemoved;

	@Shadow
	public abstract TileEntity getTileEntity(BlockPos pos);

	@Unique
	public final HashMap<TileEntity, ITileEntity> emcTileEntities = new HashMap<>();

	@Override
	public Collection<ITileEntity> getEmcTileEntities() {
		return emcTileEntities.values();
	}

	@Inject(method = "addTileEntity", at = @At("TAIL"))
	public void addBlockEntity(TileEntity blockEntity, CallbackInfoReturnable<Boolean> ci) {
		emcTileEntities.put(blockEntity, new ITileEntity(blockEntity));
	}

	@Inject(method = "updateEntities", at = @At("HEAD"))
	private void tickBlockEntities(CallbackInfo info) {
		if (!tileEntitiesToBeRemoved.isEmpty()) {
			for (TileEntity entity : tileEntitiesToBeRemoved) {
				emcTileEntities.remove(entity);
			}
		}
	}

	@Inject(method = "removeTileEntity", at = @At("HEAD"))
	public void removeBlockEntity(BlockPos pos, CallbackInfo info) {
		TileEntity blockEntity = this.getTileEntity(pos);
		if (blockEntity != null) {
			emcTileEntities.remove(blockEntity);
		}
	}

}
