package me.deftware.mixin.mixins.gui;

import me.deftware.aristois.modules.loaders.ModLoader;
import me.deftware.client.framework.event.events.EventGuiScreenPostDraw;
import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.mixin.imp.IMixinGuiContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class MixinGuiContainer extends MixinGuiScreen implements IMixinGuiContainer {

    @Shadow
    protected Slot focusedSlot;

    @Override
    public Slot getHoveredSlot() {
        return focusedSlot;
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        this.shouldSendPostRenderEvent = false;
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "drawMouseoverTooltip", at = @At("RETURN"))
    private void drawMouseoverTooltip(MatrixStack matrices, int x, int y, CallbackInfo ci) {
        if (!(((Screen) (Object) this) instanceof GuiScreen)) {
            new EventGuiScreenPostDraw(this.screenInstance.get(), x, y).broadcast();
        }
    }

    /*@Inject(method = "onMouseClick", at = @At("HEAD"))
    private void fuck(Slot slot, int invSlot, int clickData, SlotActionType actionType, CallbackInfo ci) {
        if (ModLoader.isEnabled(ShulkerDupe.class)) {
            HitResult result = MinecraftClient.getInstance().player.raycast(5.0D, 0.0F, false);
            if (result instanceof BlockHitResult && result.getType() != HitResult.Type.MISS) {
                System.out.println("Attack block");
                Direction direction = ((BlockHitResult) result).getSide();
                BlockPos pos = ((BlockHitResult) result).getBlockPos();
                MinecraftClient.getInstance().interactionManager.attackBlock(pos, direction);
                MinecraftClient.getInstance().interactionManager.updateBlockBreakingProgress(pos, direction);
                MinecraftClient.getInstance().getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
            }
        }
    }*/

}
