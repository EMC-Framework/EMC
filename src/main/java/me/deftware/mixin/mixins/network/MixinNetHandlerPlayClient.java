package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.network.NetworkHandler;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.player.PlayerEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.message.LastSeenMessagesCollector;
import net.minecraft.network.message.MessageBody;
import net.minecraft.network.message.MessageChain;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Deftware
 */
@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinNetHandlerPlayClient implements NetworkHandler {

    @Shadow private LastSeenMessagesCollector lastSeenMessagesCollector;

    @Shadow private MessageChain.Packer messagePacker;

    @Override
    public List<PlayerEntry> _getPlayerList() {
        return ((ClientPlayNetworkHandler) (Object) this).getPlayerList()
                .stream().map(PlayerEntry.class::cast).collect(Collectors.toList());
    }

    @Unique
    private final EventAnimation eventAnimation = new EventAnimation();

    @Inject(method = "onEntityStatus", at = @At("HEAD"), cancellable = true)
    public void onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        if (packet.getStatus() == 35) {
            eventAnimation.create(EventAnimation.AnimationType.Totem);
            eventAnimation.broadcast();
            if (eventAnimation.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "onExplosion", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"), cancellable = true)
    private void onExplosion(ExplosionS2CPacket packet, CallbackInfo ci) {
        var knockback = packet.playerKnockback();
        if (knockback.isPresent()) {
            var velocity = knockback.get();
            EventKnockback event = new EventKnockback(velocity.x, velocity.y, velocity.z).broadcast();
            if (!event.isCanceled()) {
                var player = MinecraftClient.getInstance().player;
                player.setVelocity(
                        player.getVelocity().add(event.getX(), event.getY(), event.getZ())
                );
            }
            ci.cancel();
        }
    }

    @Inject(method = "onChunkData", at = @At("HEAD"), cancellable = true)
    public void onReceiveChunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
        EventChunkDataReceive event = new EventChunkDataReceive(packet).broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "onBlockUpdate", at = @At("HEAD"))
    private void onBlockUpdate(BlockUpdateS2CPacket packet, CallbackInfo ci) {
        BlockPos pos = packet.getPos();
        int chunkX = pos.getX() >> 4, chunkZ = pos.getZ() >> 4, chunkY = pos.getY() >> 4;
        int x = pos.getX() - (chunkX << 4), z = pos.getZ() - (chunkZ << 4), y = pos.getY() & 0xF;

        short[] positions = { (short) ((x << 8) | (z << 4) | y) };
        Block[] blocks = { (Block) packet.getState().getBlock() };

        new EventChunk.EventDeltaChunk(
                chunkX, chunkY, chunkZ,
                positions, blocks
        ).broadcast();
    }

    @Inject(method = "onChunkDeltaUpdate", at = @At("HEAD"))
    private void onChunkDeltaPacket(ChunkDeltaUpdateS2CPacket packet, CallbackInfo ci) {
        ChunkDeltaAccessor accessor = (ChunkDeltaAccessor) packet;
        Block[] blocks = Arrays.stream(accessor.getBlockStates())
                .map(s -> (Block) s.getBlock())
                .toArray(Block[]::new);
        ChunkSectionPos pos = accessor.getSectionPos();
        new EventChunk.EventDeltaChunk(
                pos.getX(), pos.getY(), pos.getZ(),
                accessor.getPositions(), blocks
        ).broadcast();
    }

    /* TODO
    @Redirect(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/ClientBrandRetriever;getClientModName()Ljava/lang/String;", remap = false))
    private String onGameJoin$GetClientBrand() {
        return "vanilla";
    }*/

    @Unique
    @Override
    public LastSeenMessagesCollector.LastSeenMessages collect() {
        return lastSeenMessagesCollector.collect();
    }

    @Unique
    @Override
    public MessageSignatureData pack(MessageBody body) {
        return messagePacker.pack(body);
    }

}
