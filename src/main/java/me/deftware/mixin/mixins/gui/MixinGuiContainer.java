package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.event.events.EventGuiScreenPostDraw;
import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.mixin.imp.IMixinGuiContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.container.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
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
    private void drawMouseoverTooltip(int x, int y, CallbackInfo ci) {
        if (!(((Screen) (Object) this) instanceof GuiScreen)) {
            new EventGuiScreenPostDraw(this.screenInstance.get(), x, y).broadcast();
        }
    }

}
