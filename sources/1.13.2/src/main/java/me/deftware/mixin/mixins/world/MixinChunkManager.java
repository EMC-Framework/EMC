package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.event.events.EventChunk;
import me.deftware.client.framework.world.chunk.ChunkAccessor;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkProviderClient.class)
public class MixinChunkManager {

    @Inject(method = "func_212474_a", at = @At("TAIL"))
    private void onChunkLoadEvent(int x, int z, PacketBuffer buf, int p_212474_4_, boolean p_212474_5_, CallbackInfoReturnable<Chunk> cir) {
        new EventChunk((ChunkAccessor) cir.getReturnValue(), EventChunk.Action.LOAD, x, z).broadcast();
    }

    @Inject(method = "unloadChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;onUnload()V", shift = At.Shift.AFTER))
    private void onChunkUnloadEvent(int x, int z, CallbackInfo ci) {
        new EventChunk(null, EventChunk.Action.UNLOAD, x, z).broadcast();
    }

}
