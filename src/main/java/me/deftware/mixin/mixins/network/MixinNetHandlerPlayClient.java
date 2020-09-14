package me.deftware.mixin.mixins.network;

import io.netty.buffer.Unpooled;
import me.deftware.client.framework.event.events.EventAnimation;
import me.deftware.client.framework.event.events.EventChunkDataReceive;
import me.deftware.client.framework.event.events.EventKnockback;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Redirect(method = "handleJoinGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;sendPacket(Lnet/minecraft/network/Packet;)V"))
    private void handleJoinGame(NetworkManager connection, Packet<?> packet) {
        if (!(packet instanceof CPacketCustomPayload)) {
            connection.sendPacket(packet);
            return;
        }
        // Overwrite the brand packet to send vanilla, because Forge modifies it and some server do not like it
        connection.sendPacket(new CPacketCustomPayload(ClientBrandRetriever.getClientModName(), (new PacketBuffer(Unpooled.buffer())).writeString("vanilla")));
    }

    @Inject(method = "handleEntityStatus", at = @At("HEAD"), cancellable = true)
    public void onEntityStatus(SPacketEntityStatus packetIn, CallbackInfo ci) {
        if (packetIn.getOpCode() == 35) {
            EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Totem);
            event.broadcast();
            if (event.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "handleExplosion", at = @At(value = "HEAD"), cancellable = true)
    private void onExplosion(SPacketExplosion packet, CallbackInfo ci) {
        EventKnockback event = new EventKnockback(packet.getMotionX(), packet.getMotionY(), packet.getMotionZ());
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleEntityVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V"), cancellable = true)
    public void onVelocityUpdate(SPacketEntityVelocity packet, CallbackInfo ci) {
        EventKnockback event = new EventKnockback(packet.getMotionX(), packet.getMotionY(), packet.getMotionZ());
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleChunkData", at = @At("HEAD"), cancellable = true)
    public void onReceiveChunkData(SPacketChunkData packet, CallbackInfo ci) {
        EventChunkDataReceive event = new EventChunkDataReceive(packet);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
    
}
