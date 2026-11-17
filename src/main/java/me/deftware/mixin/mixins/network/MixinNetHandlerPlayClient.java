package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.network.NetworkHandler;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.player.PlayerEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.LastSeenMessagesTracker;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.SignedMessageBody;
import net.minecraft.network.chat.SignedMessageChain;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
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
@Mixin(ClientPacketListener.class)
public abstract class MixinNetHandlerPlayClient implements NetworkHandler {

    @Shadow private LastSeenMessagesTracker lastSeenMessages;

    @Shadow private SignedMessageChain.Encoder signedMessageEncoder;

    @Override
    public List<PlayerEntry> _getPlayerList() {
        return ((ClientPacketListener) (Object) this).getOnlinePlayers()
                .stream().map(PlayerEntry.class::cast).collect(Collectors.toList());
    }

    @Unique
    private final EventAnimation eventAnimation = new EventAnimation();

    @Inject(method = "handleEntityEvent", at = @At("HEAD"), cancellable = true)
    public void onEntityStatus(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        if (packet.getEventId() == 35) {
            eventAnimation.create(EventAnimation.AnimationType.Totem);
            eventAnimation.broadcast();
            if (eventAnimation.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "handleExplosion", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"), cancellable = true)
    private void onExplosion(ClientboundExplodePacket packet, CallbackInfo ci) {
        var knockback = packet.playerKnockback();
        if (knockback.isPresent()) {
            var velocity = knockback.get();
            EventKnockback event = new EventKnockback(velocity.x, velocity.y, velocity.z).broadcast();
            if (!event.isCanceled()) {
                var player = Minecraft.getInstance().player;
                player.setDeltaMovement(
                        player.getDeltaMovement().add(event.getX(), event.getY(), event.getZ())
                );
            }
            ci.cancel();
        }
    }

    @Inject(method = "handleLevelChunkWithLight", at = @At("HEAD"), cancellable = true)
    public void onReceiveChunkData(ClientboundLevelChunkWithLightPacket packet, CallbackInfo ci) {
        EventChunkDataReceive event = new EventChunkDataReceive(packet).broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleBlockUpdate", at = @At("HEAD"))
    private void onBlockUpdate(ClientboundBlockUpdatePacket packet, CallbackInfo ci) {
        BlockPos pos = packet.getPos();
        int chunkX = pos.getX() >> 4, chunkZ = pos.getZ() >> 4, chunkY = pos.getY() >> 4;
        int x = pos.getX() - (chunkX << 4), z = pos.getZ() - (chunkZ << 4), y = pos.getY() & 0xF;

        short[] positions = { (short) ((x << 8) | (z << 4) | y) };
        Block[] blocks = { (Block) packet.getBlockState().getBlock() };

        new EventChunk.EventDeltaChunk(
                chunkX, chunkY, chunkZ,
                positions, blocks
        ).broadcast();
    }

    @Inject(method = "handleChunkBlocksUpdate", at = @At("HEAD"))
    private void onChunkDeltaPacket(ClientboundSectionBlocksUpdatePacket packet, CallbackInfo ci) {
        ChunkDeltaAccessor accessor = (ChunkDeltaAccessor) packet;
        Block[] blocks = Arrays.stream(accessor.getBlockStates())
                .map(s -> (Block) s.getBlock())
                .toArray(Block[]::new);
        SectionPos pos = accessor.getSectionPos();
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
    public LastSeenMessagesTracker.Update collect() {
        return lastSeenMessages.generateAndApplyUpdate();
    }

    @Unique
    @Override
    public MessageSignature pack(SignedMessageBody body) {
        return signedMessageEncoder.pack(body);
    }

}
