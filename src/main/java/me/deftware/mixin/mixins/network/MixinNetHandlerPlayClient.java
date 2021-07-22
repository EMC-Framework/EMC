package me.deftware.mixin.mixins.network;

import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import me.deftware.client.framework.event.events.EventAnimation;
import me.deftware.client.framework.event.events.EventChunkDataReceive;
import me.deftware.client.framework.event.events.EventKnockback;
import net.minecraft.client.ClientBrandRetriever;
import me.deftware.client.framework.world.player.PlayerEntry;
import me.deftware.mixin.imp.IMixinNetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient implements IMixinNetworkHandler {

    @Shadow
    private Minecraft gameController;

    @Unique
    private final Map<UUID, PlayerEntry> playerEntryMap = Maps.newHashMap();

    @Redirect(method = "handlePlayerListItem", at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private Object onPlayerListUpdate(Map<UUID, NetworkPlayerInfo> map, Object key) {
        playerEntryMap.remove((UUID) key);
        return map.remove((UUID) key);
    }

    @Redirect(method = "handlePlayerListItem", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private Object onPlayerListAdd(Map<UUID, NetworkPlayerInfo> map, Object key, Object value) {
        playerEntryMap.put((UUID) key, new PlayerEntry((NetworkPlayerInfo) value));
        return map.put((UUID) key, (NetworkPlayerInfo) value);
    }

    @Override
    public Map<UUID, PlayerEntry> getPlayerEntryMap() {
        return playerEntryMap;
    }

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
            EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Totem);
            event.broadcast();
            if (event.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "handleExplosion", at = @At(value = "HEAD"), cancellable = true)
    private void onExplosion(S27PacketExplosion packet, CallbackInfo ci) {
        EventKnockback event = new EventKnockback(packet.func_149149_c(), packet.func_149144_d(), packet.func_149147_e());
        event.broadcast();
        if (!event.isCanceled()) {
            this.gameController.player.motionX += event.getX();
            this.gameController.player.motionY += event.getY();
            this.gameController.player.motionZ += event.getZ();
        }
        ci.cancel();
    }

    @Inject(method = "handleEntityVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V"), cancellable = true)
    public void onVelocityUpdate(S12PacketEntityVelocity packet, CallbackInfo ci) {
        EventKnockback event = new EventKnockback(packet.getMotionX(), packet.getMotionY(), packet.getMotionZ());
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleChunkData", at = @At("HEAD"), cancellable = true)
    public void onReceiveChunkData(S21PacketChunkData packet, CallbackInfo ci) {
        EventChunkDataReceive event = new EventChunkDataReceive(packet);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
    
}
