package me.deftware.mixin.mixins.game;

import me.deftware.client.framework.world.chunk.BlockClassifier;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkProviderClient.class)
public abstract class MixinClientChunkManager {

	@Redirect(method = "unloadChunk", at = @At(target = "Lnet/minecraft/world/chunk/Chunk;onChunkUnload()V", value = "INVOKE", opcode = 180))
	private void positionEqualsRedirect(Chunk chunk) {
		BlockClassifier.CLASSIFIERS.forEach(b -> b.unload(chunk.xPosition, chunk.zPosition));
	}

}
