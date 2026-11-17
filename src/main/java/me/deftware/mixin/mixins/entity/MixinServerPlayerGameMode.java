package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.network.packets.CPacketDig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerGameMode.class)
public abstract class MixinServerPlayerGameMode {

    @Shadow
    protected ServerLevel level;

    @Shadow
    @Final
    protected ServerPlayer player;

    @Shadow
    private BlockPos destroyPos;

    @Shadow
    public abstract void destroyAndAck(BlockPos pos, int sequence, String reason);

    @Inject(method = "handleBlockBreakAction", at = @At("HEAD"), cancellable = true)
    private void onHandleBlockBreakAction(
            BlockPos pos,
            ServerboundPlayerActionPacket.Action action,
            Direction direction,
            int maxBuildHeight,
            int sequence,
            CallbackInfo ci
    ) {
        if (action != ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK) {
            return;
        }
        if (System.currentTimeMillis() - CPacketDig.lastStopDigTimeMs > 1000L) {
            return;
        }
        if (level == null || level.getServer() == null || !level.getServer().isSingleplayer()) {
            return;
        }
        if (!player.isWithinBlockInteractionRange(pos, 1.0) || pos.getY() > maxBuildHeight) {
            return;
        }
        if (pos.equals(destroyPos)) {
            BlockState state = level.getBlockState(pos);
            if (!state.isAir() && state.getDestroySpeed(level, pos) >= 0.0f) {
                destroyAndAck(pos, sequence, "destroyed");
                ci.cancel();
            }
        }
    }

}
