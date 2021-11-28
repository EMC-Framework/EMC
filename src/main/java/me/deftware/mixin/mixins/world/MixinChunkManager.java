package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.event.events.EventChunk;
import me.deftware.client.framework.world.chunk.ChunkAccessor;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientChunkManager.class)
public class MixinChunkManager {

    @Inject(method = "loadChunkFromPacket", at = @At("TAIL"))
    private void onChunkLoadEvent(World world, int x, int z, PacketByteBuf data, CompoundTag nbt, int updatedSectionsBits, boolean clearOld, CallbackInfoReturnable<WorldChunk> cir) {
        new EventChunk((ChunkAccessor) cir.getReturnValue(), EventChunk.Action.LOAD, x, z).broadcast();
    }

    @Inject(method = "unload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientChunkManager$ClientChunkMap;method_20183(ILnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/chunk/WorldChunk;)Lnet/minecraft/world/chunk/WorldChunk;", shift = At.Shift.AFTER))
    private void onChunkUnloadEvent(int x, int z, CallbackInfo ci) {
        new EventChunk(null, EventChunk.Action.UNLOAD, x, z).broadcast();
    }

}
