package me.deftware.mixin.mixins;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.deftware.client.framework.event.PacketCircuit;
import me.deftware.client.framework.event.events.EventPacketReceive;
import me.deftware.client.framework.event.events.EventPacketSend;
import me.deftware.client.framework.network.IPacket;
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
    protected abstract void dispatchPacket(Packet<?> p_dispatchPacket_1_, GenericFutureListener<? extends Future<? super Void>> p_dispatchPacket_2_);

    @Redirect(method = "channelRead0", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;processPacket(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;)V"))
    private void channelRead0(Packet<INetHandler> packet, INetHandler listener) {
        EventPacketReceive event = new EventPacketReceive(packet);
        event.broadcast();
        if (!event.isCanceled()) {
            ((Packet<INetHandler>) event.getIPacket().getPacket()).processPacket(listener);
        }
    }

    @Redirect(method = "sendPacket(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At(value = "INVOKE", target = "net/minecraft/network/NetworkManager.dispatchPacket(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V"))
    private void sendPacket$dispatchPacket(NetworkManager networkManager, Packet<?> packetIn, final GenericFutureListener<? extends Future<? super Void>> futureListeners) {
        EventPacketSend event = new EventPacketSend(packetIn);
        event.broadcast();
        if (event.isCanceled()) {
            return;
        }

        // Run it through the circuits.
        IPacket ipacket = event.getIPacket();
        ipacket = PacketCircuit.handlePacket(ipacket);

        // Packets can be null if out of the circuits and can be blocked from being sent.
        if(ipacket != null) dispatchPacket(ipacket.getPacket(), futureListeners);
    }

    public void sendPacketImmediately(Packet<?> packet) {
        dispatchPacket(packet, null);
    }

}