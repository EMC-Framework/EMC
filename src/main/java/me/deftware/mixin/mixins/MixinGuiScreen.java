package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventGuiScreenDraw;
import me.deftware.client.framework.event.events.EventGuiScreenPostDraw;
import me.deftware.mixin.imp.IMixinGuiScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen implements IMixinGuiScreen {

    public boolean shouldSendPostRenderEvent = true;

    @Shadow
    protected FontRenderer fontRendererObj;

    @Shadow
    protected List<GuiButton> buttonList;

    @Shadow
    protected abstract void drawHoveringText(List<String> text, int x, int y);

    @Override
    public List<GuiButton> getButtonList() {
        return buttonList;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void drawScreen(int x, int y, float p_render_3_, CallbackInfo ci) {
        new EventGuiScreenDraw((GuiScreen) (Object) this, x, y).broadcast();
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void drawScreen_return(int x, int y, float p_render_3_, CallbackInfo ci) {
        if (shouldSendPostRenderEvent) {
            new EventGuiScreenPostDraw((GuiScreen) (Object) this, x, y).broadcast();
        }
    }

}
