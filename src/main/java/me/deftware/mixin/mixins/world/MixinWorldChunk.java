package me.deftware.mixin.mixins.world;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

@Mixin(LevelChunk.class)
public class MixinWorldChunk {

	@Shadow
	@Final
	Level level;

	@Inject(method = "updateBlockEntityTicker", at = @At("HEAD"))
	private <T extends BlockEntity> void test(T blockEntity, CallbackInfo ci) {
		BlockState blockState = blockEntity.getBlockState();
		if (this.level != null && blockState.getTicker(this.level, blockEntity.getType()) != null) {
			long pos = blockEntity.getBlockPos().asLong();
			HashMap<Long, BlockEntity> entities = ((me.deftware.client.framework.world.World) level).getInternalLongToBlockEntity();
			if (entities != null) {
				entities.put(pos, blockEntity);
			}
		}
	}

}
