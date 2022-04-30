package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.event.events.EventAnimation;
import me.deftware.client.framework.event.events.EventChunk;
import me.deftware.client.framework.event.events.EventChunkDataReceive;
import me.deftware.client.framework.event.events.EventKnockback;
import me.deftware.client.framework.network.NetworkHandler;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.player.PlayerEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
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
public class MixinNetHandlerPlayClient implements NetworkHandler {

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

    @Inject(method = "onExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"), cancellable = true)
    private void onExplosion(ExplosionS2CPacket packet, CallbackInfo ci) {
        EventKnockback event = new EventKnockback(packet.getPlayerVelocityX(), packet.getPlayerVelocityY(), packet.getPlayerVelocityZ());
        event.broadcast();
        if (!event.isCanceled()) {
            MinecraftClient.getInstance().player.setVelocity(
                    MinecraftClient.getInstance().player.getVelocity().add(event.getX(), event.getY(), event.getZ())
            );
        }
        ci.cancel();
    }

    @Inject(method = "onChunkData", at = @At("HEAD"), cancellable = true)
    public void onReceiveChunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
        EventChunkDataReceive event = new EventChunkDataReceive(packet);
        event.broadcast();
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
        Block[] blocks = { BlockRegistry.INSTANCE.getBlock(packet.getState().getBlock()) };

        new EventChunk.EventDeltaChunk(
                chunkX, chunkY, chunkZ,
                positions, blocks
        ).broadcast();
    }

    @Inject(method = "onChunkDeltaUpdate", at = @At("HEAD"))
    private void onChunkDeltaPacket(ChunkDeltaUpdateS2CPacket packet, CallbackInfo ci) {
        ChunkDeltaAccessor accessor = (ChunkDeltaAccessor) packet;
        Block[] blocks = Arrays.stream(accessor.getRecords())
                .map(s -> BlockRegistry.INSTANCE.getBlock(s.getState().getBlock()))
                .toArray(Block[]::new);
        int section = 0;
        short[] positions = new short[accessor.getRecords().length];
        for (int i = 0; i < positions.length; i++) {
            // Format is wrong so we need to repack it
            short pos = accessor.getRecords()[i].getPosShort();
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

    @Redirect(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/ClientBrandRetriever;getClientModName()Ljava/lang/String;"))
    private String onGameJoin$GetClientBrand() {
        return "vanilla";
    }

}
