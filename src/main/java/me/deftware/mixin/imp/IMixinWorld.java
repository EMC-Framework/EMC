package me.deftware.mixin.imp;

import me.deftware.client.framework.entity.block.TileEntity;
import net.minecraft.block.entity.BlockEntity;

import java.util.Collection;
import java.util.Map;

public interface IMixinWorld {

	Map<BlockEntity, TileEntity> getLoadedTilesAccessor();
	
}
