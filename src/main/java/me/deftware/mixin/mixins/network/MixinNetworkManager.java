package me.deftware.mixin.mixins.network;

import io.netty.channel.ChannelFutureListener;
import me.deftware.client.framework.event.events.EventPacketReceive;
import me.deftware.client.framework.event.events.EventPacketSend;
import me.deftware.mixin.imp.IMixinNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Connection.class)
public abstract class MixinNetworkManager implements IMixinNetworkManager {

    @Shadow
    protected abstract void sendPacket(Packet<?> packet, ChannelFutureListener callbacks, boolean flush);

    @SuppressWarnings("unchecked")
    @Redirect(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;genericsFtw(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;)V"))
    private void channelRead0(Packet<?> packet, PacketListener listener) {
        EventPacketReceive event = new EventPacketReceive(packet).broadcast();
        if (!event.isCanceled()) {
            ((Packet<PacketListener>) event.getIPacket().getPacket()).handle(listener);
        }
    }

    @Unique
    private int lastMovePosTick = Integer.MIN_VALUE;

    @Unique
    private Packet<?> deduplicateMovePosition(Packet<?> packet) {
        if (packet instanceof ServerboundMovePlayerPacket move && move.hasPosition()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc != null && mc.player != null) {
                int tick = mc.player.tickCount;
                if (tick == lastMovePosTick) {
                    if (move.hasRotation()) {
                        return new ServerboundMovePlayerPacket.Rot(
                                move.getYRot(0.0f),
                                move.getXRot(0.0f),
                                move.isOnGround(),
                                move.horizontalCollision()
                        );
                    }
                    return null;
                }
                lastMovePosTick = tick;
            }
        }
        return packet;
    }

    @Redirect(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;sendPacket(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V"))
    private void sendPacket$dispatchPacket(Connection instance, Packet<?> packet, ChannelFutureListener callbacks, boolean flush) {
        EventPacketSend event = new EventPacketSend(packet);
        event.broadcast();
        if (event.isCanceled()) {
            return;
        }
        Packet<?> out = deduplicateMovePosition(event.getPacket());
        if (out != null) {
            sendPacket(out, callbacks, flush);
        }
    }

    @Unique
    @Override
    public void sendPacketImmediately(Packet<?> packet) {
        Packet<?> out = deduplicateMovePosition(packet);
        if (out != null) {
            sendPacket(out, null, true);
        }
    }

}
