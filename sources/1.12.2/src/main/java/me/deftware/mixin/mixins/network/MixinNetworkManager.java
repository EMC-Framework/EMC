package me.deftware.mixin.mixins.network;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.deftware.client.framework.event.events.EventPacketReceive;
import me.deftware.client.framework.event.events.EventPacketSend;
import me.deftware.mixin.imp.IMixinNetworkManager;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager implements IMixinNetworkManager {

    @Shadow
    protected abstract void dispatchPacket(Packet<?> inPacket, GenericFutureListener<? extends Future<? super Void>>[] futureListeners);

    @Redirect(method = "channelRead0", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V"))
    private void channelRead0(Packet<?> packet, INetHandler listener) {
        EventPacketReceive event = new EventPacketReceive(packet);
        event.broadcast();
        if (!event.isCanceled()) {
            ((Packet<INetHandler>) event.getIPacket().getPacket()).processPacket(listener);
        }
    }

    @Redirect(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;dispatchPacket(Lnet/minecraft/network/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V"))
    private void sendPacket$dispatchPacket2(NetworkManager networkManager, Packet<?> inPacket, GenericFutureListener<? extends Future<? super Void>>[] futureListeners) {
        handlePacket(inPacket, null);
    }

    @Redirect(method = "sendPacket(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;[Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;dispatchPacket(Lnet/minecraft/network/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V"))
    private void sendPacket$dispatchPacket(NetworkManager networkManager, Packet<?> packetIn, final GenericFutureListener<? extends Future<? super Void>>[] futureListeners) {
        handlePacket(packetIn, futureListeners);
    }

    private void handlePacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>>[] futureListeners) {
        EventPacketSend event = new EventPacketSend(packet);
        event.broadcast();
        if (event.isCanceled()) {
            return;
        }
        dispatchPacket(event.getPacket(), futureListeners);
    }

    public void sendPacketImmediately(Packet<?> packet) {
        dispatchPacket(packet, null);
    }

}