package me.deftware.mixin.mixins;

import me.deftware.mixin.imp.IMixinCPacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(C03PacketPlayer.class)
public class MixinCPacketPlayer implements IMixinCPacketPlayer {

    @Shadow
    protected double y;

    @Shadow
    protected boolean onGround;

    @Shadow
    private boolean moving;

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
        return moving;
    }

    @Override
    public void setMoving(boolean state) {
        moving = state;
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
