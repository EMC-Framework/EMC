package me.deftware.mixin.mixins.network;

import io.netty.buffer.Unpooled;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.network.NetworkHandler;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.player.PlayerEntry;
import me.deftware.mixin.imp.IMixinMultiBlockChange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
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
@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient implements NetworkHandler {

    @Shadow
    private Minecraft client;

    @Override
    public List<PlayerEntry> _getPlayerList() {
        return ((NetHandlerPlayClient) (Object) this).getPlayerInfoMap()
                .stream().map(PlayerEntry.class::cast).collect(Collectors.toList());
    }

    @Unique
    private final EventAnimation eventAnimation = new EventAnimation();

    @Redirect(method = "handleJoinGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;sendPacket(Lnet/minecraft/network/Packet;)V"))
    private void handleJoinGame(NetworkManager connection, Packet<?> packet) {
        if (!(packet instanceof CPacketCustomPayload)) {
            connection.sendPacket(packet);
            return;
        }
        // Overwrite the brand packet to send vanilla, because Rift modifies it and some server do not like it
        connection.sendPacket(new CPacketCustomPayload(CPacketCustomPayload.BRAND, (new PacketBuffer(Unpooled.buffer())).writeString("vanilla")));
    }

    @Inject(method = "handleEntityStatus", at = @At("HEAD"), cancellable = true)
    public void onEntityStatus(SPacketEntityStatus packetIn, CallbackInfo ci) {
        if (packetIn.getOpCode() == 35) {
            eventAnimation.create(EventAnimation.AnimationType.Totem);
            eventAnimation.broadcast();
            if (eventAnimation.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "handleExplosion", at = @At(value = "HEAD"), cancellable = true)
    private void onExplosion(SPacketExplosion packet, CallbackInfo ci) {
        EventKnockback event = new EventKnockback(packet.getMotionX(), packet.getMotionY(), packet.getMotionZ());
        event.broadcast();
        if (!event.isCanceled()) {
            this.client.player.motionX += event.getX();
            this.client.player.motionY += event.getY();
            this.client.player.motionZ += event.getZ();
        }
        ci.cancel();
    }

    @Inject(method = "handleChunkData", at = @At("HEAD"), cancellable = true)
    public void onReceiveChunkData(SPacketChunkData packet, CallbackInfo ci) {
        EventChunkDataReceive event = new EventChunkDataReceive(packet);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleBlockChange", at = @At("HEAD"))
    private void onBlockUpdate(SPacketBlockChange packet, CallbackInfo ci) {
        BlockPos pos = packet.getPos();
        int chunkX = pos.getX() >> 4, chunkZ = pos.getZ() >> 4, chunkY = pos.getY() >> 4;
        int x = pos.getX() - (chunkX << 4), z = pos.getZ() - (chunkZ << 4), y = pos.getY() & 0xF;

        short[] positions = { (short) ((x << 8) | (z << 4) | y) };
        Block[] blocks = { BlockRegistry.INSTANCE.getBlock(packet.getState().getBlock()) };
        
        new EventChunk.EventDeltaChunk(
                chunkX, chunkY, chunkZ,
                positions, blocks
        ).broadcast();
    }

    @Inject(method = "handleMultiBlockChange", at = @At("HEAD"))
    private void onChunkDeltaPacket(SPacketMultiBlockChange packet, CallbackInfo ci) {
        IMixinMultiBlockChange accessor = (IMixinMultiBlockChange) packet;
        Block[] blocks = Arrays.stream(accessor.getRecords())
                .map(s -> BlockRegistry.INSTANCE.getBlock(s.getBlockState().getBlock()))
                .toArray(Block[]::new);
        int section = 0;
        short[] positions = new short[accessor.getRecords().length];
        for (int i = 0; i < positions.length; i++) {
            // Format is wrong so we need to repack it
            short pos = accessor.getRecords()[i].getOffset();
            int x = pos >> 12 & 0xF, y = pos & 0xFF, z = pos >> 8 & 0xF;
            positions[i] = (short) ((x << 8) | (z << 4) | (y & 0xF));
            section = y >> 4;
        }
        ChunkPos chunkPos = accessor.getChunkPos();
        new EventChunk.EventDeltaChunk(
                chunkPos.x, section, chunkPos.z,
                positions, blocks
        ).broadcast();
    }

    @Redirect(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;addChatMessage(Lnet/minecraft/util/text/ChatType;Lnet/minecraft/util/text/ITextComponent;)V"))
    private void onChatMessage(GuiIngame instance, ChatType type, ITextComponent message) {
        EventChatReceive event = new EventChatReceive((Message) message).broadcast();
        if (!event.isCanceled()) {
            instance.addChatMessage(type, (ITextComponent) event.getMessage());
        }
    }

}
