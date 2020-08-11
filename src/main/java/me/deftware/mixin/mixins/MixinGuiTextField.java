package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventChatboxType;
import me.deftware.client.framework.fonts.EMCFont;
import me.deftware.client.framework.utils.render.GraphicsUtil;
import me.deftware.client.framework.wrappers.gui.IGuiScreen;
import me.deftware.mixin.imp.IMixinGuiTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.lang.ref.WeakReference;

@Mixin(GuiTextField.class)
public abstract class MixinGuiTextField implements IMixinGuiTextField {

    private boolean useMinecraftScaling = true;
    private boolean useCustomFont = false;
    private EMCFont customFont;

    private boolean overlay = false;
    private String overlayText = "";

    @Shadow
    public int xPosition;

    @Shadow
    public int yPosition;

    @Mutable
    @Final
    @Shadow
    private int height;

    @Mutable
    @Final
    @Shadow
    private int width;

    @Shadow
    private int cursorCounter;

    @Shadow
    private int selectionEnd;

    @Shadow
    private int lineScrollOffset;

    @Shadow
    private int maxStringLength;

    @Shadow
    @Final
    private FontRenderer fontRendererInstance;

    @Shadow public abstract String getText();

    @Shadow public abstract boolean isFocused();

    @Shadow private int cursorPosition;

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public FontRenderer getFontRendererInstance() {
        return fontRendererInstance;
    }

    @Override
    public int getCursorCounter() {
        return cursorCounter;
    }

    @Override
    public int getSelectionEnd() {
        return selectionEnd;
    }

    @Override
    public int getLineScrollOffset() {
        return lineScrollOffset;
    }

    @Override
    public int getX() {
        return xPosition;
    }

    @Override
    public void setX(int x) {
        this.xPosition = x;
    }

    @Override
    public int getY() {
        return yPosition;
    }

    @Override
    public void setY(int y) {
        this.yPosition = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setUseMinecraftScaling(boolean state) {
        useMinecraftScaling = state;
    }

    @Override
    public void setUseCustomFont(boolean state) {
        useCustomFont = state;
    }

    @Override
    public void setCustomFont(EMCFont font) {
        customFont = font;
    }

    @Inject(method = "drawTextBox", at = @At("HEAD"))
    public void drawTextField(CallbackInfo ci) {
        if (!useMinecraftScaling) {
            GL11.glPushMatrix();
            GraphicsUtil.prepareMatrix(IGuiScreen.getDisplayWidth(), IGuiScreen.getDisplayHeight());
        }
    }

    @Inject(method = "drawTextBox", at = @At("RETURN"))
    public void drawTextFieldReturn(CallbackInfo ci) {
        if (!useMinecraftScaling) {
            GL11.glPopMatrix();
            Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
        }
        if (overlay) {
            String currentText = getText();
            int currentWidth = ((IMixinGuiTextField) this).getFontRendererInstance().getStringWidth(currentText);
            int x = isFocused() ? ((IMixinGuiTextField) this).getX() + 4 : ((IMixinGuiTextField) this).getX();
            int y = isFocused() ? ((IMixinGuiTextField) this).getY() + (((IMixinGuiTextField) this).getHeight() - 8) / 2 : ((IMixinGuiTextField) this).getY();
            ((IMixinGuiTextField) this).getFontRendererInstance().drawStringWithShadow(overlayText, x + currentWidth - 3, y - 2, Color.GRAY.getRGB());
            WeakReference<EventChatboxType> event = new WeakReference<>(new EventChatboxType(getText(), overlayText));
            event.get().broadcast();
            overlayText = event.get().getOverlay();
        }
    }

    @Redirect(method = "drawTextBox", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/FontRenderer.drawStringWithShadow(Ljava/lang/String;FFI)I"))
    public int onDrawText(FontRenderer self, String text, float x, float y, int color) {
        if (useCustomFont) {
            customFont.drawStringWithShadow((int) x, (int) y - 6, text, new Color(color));
            return (int) (x + customFont.getStringWidth(text) + 1f);
        } else {
            return this.fontRendererInstance.drawStringWithShadow(text, x, y, color);
        }
    }

    public int getMaxTextLength() {
        return maxStringLength;
    }

    @Override
    public int getCursorMax() {
        return cursorPosition;
    }

    @Override
    public void setOverlay(boolean flag) {
        overlay = flag;
    }

}
