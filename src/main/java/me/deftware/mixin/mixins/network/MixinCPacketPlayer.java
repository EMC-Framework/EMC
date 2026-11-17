package me.deftware.mixin.mixins.network;

import me.deftware.mixin.imp.IMixinCPacketPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerboundMovePlayerPacket.class)
public class MixinCPacketPlayer implements IMixinCPacketPlayer {

    @Mutable
    @Final
    @Shadow
    protected double y;

    @Mutable
    @Final
    @Shadow
    protected boolean onGround;

    @Mutable
    @Final
    @Shadow
    protected boolean hasPos;

    @Unique
    @Override
    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public void setOnGround(boolean state) {
        onGround = state;
    }

    @Override
    public boolean isMoving() {
        return hasPos;
    }

    @Override
    public void setMoving(boolean state) {
        hasPos = state;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getY() {
        return y;
    }

}
