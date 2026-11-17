package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.event.events.EventChunk;
import me.deftware.client.framework.world.chunk.ChunkAccessor;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientChunkCache.Storage.class)
public class MixinChunkManager {

    @Inject(method = "replace", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;onChunkAdded(Lnet/minecraft/world/level/chunk/LevelChunk;)V", shift = At.Shift.AFTER))
    private void onChunkSet$Load(int index, LevelChunk chunk, CallbackInfo ci) {
        new EventChunk((ChunkAccessor) chunk, EventChunk.Action.LOAD, chunk.getPos().x(), chunk.getPos().z()).broadcast();
    }

    @Inject(method = "drop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;onChunkRemoved(Lnet/minecraft/world/level/chunk/LevelChunk;)V", shift = At.Shift.AFTER))
    private void onChunkUnload(int index, LevelChunk chunk, CallbackInfo ci) {
        new EventChunk(null, EventChunk.Action.UNLOAD, chunk.getPos().x(), chunk.getPos().z()).broadcast();
    }

}
