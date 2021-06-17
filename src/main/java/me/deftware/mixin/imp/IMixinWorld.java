package me.deftware.mixin.imp;

import me.deftware.client.framework.entity.block.TileEntity;

import java.util.Map;

public interface IMixinWorld {

	Map<net.minecraft.tileentity.TileEntity, TileEntity> getLoadedTilesAccessor();
	
}
