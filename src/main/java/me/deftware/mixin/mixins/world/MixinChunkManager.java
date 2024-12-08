package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.event.events.EventChunk;
import me.deftware.client.framework.world.chunk.ChunkAccessor;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.world.chunk.WorldChunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientChunkManager.ClientChunkMap.class)
public class MixinChunkManager {

    @Inject(method = "set", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientChunkManager$ClientChunkMap;loadChunkSections(Lnet/minecraft/world/chunk/WorldChunk;)V", shift = At.Shift.AFTER))
    private void onChunkSet$Load(int index, WorldChunk chunk, CallbackInfo ci) {
        new EventChunk((ChunkAccessor) chunk, EventChunk.Action.LOAD, chunk.getPos().x, chunk.getPos().z).broadcast();
    }

    @Inject(method = "unloadChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientChunkManager$ClientChunkMap;unloadChunkSections(Lnet/minecraft/world/chunk/WorldChunk;)V", shift = At.Shift.AFTER))
    private void onChunkUnload(int index, WorldChunk chunk, CallbackInfo ci) {
        new EventChunk(null, EventChunk.Action.UNLOAD, chunk.getPos().x, chunk.getPos().z).broadcast();
    }

}
