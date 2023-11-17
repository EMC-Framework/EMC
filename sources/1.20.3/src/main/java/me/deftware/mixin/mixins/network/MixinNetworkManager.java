package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.event.events.EventPacketReceive;
import me.deftware.client.framework.event.events.EventPacketSend;
import me.deftware.mixin.imp.IMixinNetworkManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientConnection.class)
public abstract class MixinNetworkManager implements IMixinNetworkManager {

    @Shadow
    protected abstract void sendImmediately(Packet<?> packet, PacketCallbacks callbacks, boolean flush);

    @SuppressWarnings("unchecked")
    @Redirect(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;handlePacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V"))
    private void channelRead0(Packet<?> packet, PacketListener listener) {
        EventPacketReceive event = new EventPacketReceive(packet).broadcast();
        if (!event.isCanceled()) {
            ((Packet<PacketListener>) event.getIPacket().getPacket()).apply(listener);
        }
    }

    @Redirect(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;sendImmediately(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Z)V"))
    private void sendPacket$dispatchPacket(ClientConnection instance, Packet<?> packet, PacketCallbacks callbacks, boolean flush) {
        EventPacketSend event = new EventPacketSend(packet);
        event.broadcast();
        if (event.isCanceled()) {
            return;
        }
        sendImmediately(event.getPacket(), callbacks, flush);
    }

    @Unique
    @Override
    public void sendPacketImmediately(Packet<?> packet) {
        // TODO: What should flush be
        sendImmediately(packet, null, false);
    }

}
