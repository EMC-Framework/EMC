package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.event.events.EventGuiScreenPostDraw;
import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.mixin.imp.IMixinGuiContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinGuiContainer extends MixinGuiScreen implements IMixinGuiContainer {

    @Shadow
    protected Slot hoveredSlot;

    @Override
    public Slot getHoveredSlot() {
        return hoveredSlot;
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        this.shouldSendPostRenderEvent = false;
    }

    @Inject(method = "renderHoveredToolTip", at = @At("RETURN"))
    private void drawMouseoverTooltip(int x, int y, CallbackInfo ci) {
        if (!(((net.minecraft.client.gui.GuiScreen) (Object) this) instanceof GuiScreen)) {
            new EventGuiScreenPostDraw(this.screenInstance.get(), x, y).broadcast();
        }
    }

}
