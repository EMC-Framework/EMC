package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventGuiScreenPostDraw;
import me.deftware.mixin.imp.IMixinGuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends MixinGuiScreen implements IMixinGuiContainer {

    @Shadow
    private Slot theSlot;

    @Override
    public Slot getHoveredSlot() {
        return theSlot;
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        this.shouldSendPostRenderEvent = false;
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void render_return(int x, int y, float p_render_3_, CallbackInfo ci) {
        new EventGuiScreenPostDraw((GuiScreen) (Object) this, x, y).broadcast();
    }
}
