package me.deftware.mixin.mixins.network;

import io.netty.buffer.Unpooled;
import me.deftware.client.framework.event.events.*;
import net.minecraft.client.ClientBrandRetriever;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.network.NetworkHandler;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.player.PlayerEntry;
import me.deftware.mixin.imp.IMixinMultiBlockChange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ChunkCoordIntPair;
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
    private Minecraft gameController;

    @Override
    public List<PlayerEntry> _getPlayerList() {
        return ((NetHandlerPlayClient) (Object) this).getPlayerInfoMap()
                .stream().map(PlayerEntry.class::cast).collect(Collectors.toList());
    }

    @Unique
    private final EventAnimation eventAnimation = new EventAnimation();

    @Redirect(method = "handleJoinGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;sendPacket(Lnet/minecraft/network/Packet;)V"))
    private void handleJoinGame(NetworkManager connection, Packet<?> packet) {
        if (!(packet instanceof C17PacketCustomPayload)) {
            connection.sendPacket(packet);
            return;
        }
        // Overwrite the brand packet to send vanilla, because Forge modifies it and some server do not like it
        connection.sendPacket(new C17PacketCustomPayload(ClientBrandRetriever.getClientModName(), (new PacketBuffer(Unpooled.buffer())).writeString("vanilla")));
    }

    @Inject(method = "handleEntityStatus", at = @At("HEAD"), cancellable = true)
    public void onEntityStatus(S19PacketEntityStatus packetIn, CallbackInfo ci) {
        if (packetIn.getOpCode() == 35) {
            eventAnimation.create(EventAnimation.AnimationType.Totem);
            eventAnimation.broadcast();
            if (eventAnimation.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "handleExplosion", at = @At(value = "HEAD"), cancellable = true)
    private void onExplosion(S27PacketExplosion packet, CallbackInfo ci) {
        EventKnockback event = new EventKnockback(packet.func_149149_c(), packet.func_149144_d(), packet.func_149147_e());
        event.broadcast();
        if (!event.isCanceled()) {
            this.gameController.thePlayer.motionX += event.getX();
            this.gameController.thePlayer.motionY += event.getY();
            this.gameController.thePlayer.motionZ += event.getZ();
        }
        ci.cancel();
    }

    @Inject(method = "handleChunkData", at = @At("HEAD"), cancellable = true)
    public void onReceiveChunkData(S21PacketChunkData packet, CallbackInfo ci) {
        EventChunkDataReceive event = new EventChunkDataReceive(packet);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleBlockChange", at = @At("HEAD"))
    private void onBlockUpdate(S23PacketBlockChange packet, CallbackInfo ci) {
        BlockPos pos = packet.getBlockPosition();
        int chunkX = pos.getX() >> 4, chunkZ = pos.getZ() >> 4, chunkY = pos.getY() >> 4;
        int x = pos.getX() - (chunkX << 4), z = pos.getZ() - (chunkZ << 4), y = pos.getY() & 0xF;

        short[] positions = { (short) ((x << 8) | (z << 4) | y) };
        Block[] blocks = { (Block) packet.getBlockState().getBlock() };

        new EventChunk.EventDeltaChunk(
                chunkX, chunkY, chunkZ,
                positions, blocks
        ).broadcast();
    }

    @Inject(method = "handleMultiBlockChange", at = @At("HEAD"))
    private void onChunkDeltaPacket(S22PacketMultiBlockChange packet, CallbackInfo ci) {
        IMixinMultiBlockChange accessor = (IMixinMultiBlockChange) packet;
        Block[] blocks = Arrays.stream(accessor.getRecords())
                .map(s -> (Block) s.getBlockState().getBlock())
                .toArray(Block[]::new);
        int section = 0;
        short[] positions = new short[accessor.getRecords().length];
        for (int i = 0; i < positions.length; i++) {
            // Format is wrong so we need to repack it
            short pos = accessor.getRecords()[i].func_180089_b(); // getOffset
            int x = pos >> 12 & 0xF, y = pos & 0xFF, z = pos >> 8 & 0xF;
            positions[i] = (short) ((x << 8) | (z << 4) | (y & 0xF));
            section = y >> 4;
        }
        ChunkCoordIntPair chunkPos = accessor.getChunkPos();
        new EventChunk.EventDeltaChunk(
                chunkPos.chunkXPos, section, chunkPos.chunkZPos,
                positions, blocks
        ).broadcast();
    }

    @Redirect(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;printChatMessage(Lnet/minecraft/util/IChatComponent;)V"))
    private void onChatMessage(GuiNewChat instance, IChatComponent message) {
        EventChatReceive event = new EventChatReceive((Message) message).broadcast();
        if (!event.isCanceled()) {
            instance.printChatMessage((IChatComponent) event.getMessage());
        }
    }

}
