package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.event.events.EventChatboxType;
import me.deftware.mixin.imp.IMixinGuiTextField;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.lang.ref.WeakReference;

@Mixin(GuiTextField.class)
public abstract class MixinGuiTextField  implements IMixinGuiTextField {

    @Unique
    private boolean overlay = false, passwordField = false;

    @Unique
    private String overlayText = "";

    @Shadow
    private int maxStringLength;

    @Shadow private int cursorPosition;

    @Shadow
    private int cursorCounter;

    @Shadow
    private int selectionEnd;

    @Shadow
    private int lineScrollOffset;

    @Mutable
    @Final
    @Shadow
    private int height;

    @Mutable
    @Final
    @Shadow
    private int width;

    @Shadow
    public int xPosition;

    @Shadow
    public int yPosition;

    @Shadow
    @Final
    private FontRenderer fontRendererInstance;

    @Shadow public abstract String getText();

    @Shadow public abstract boolean isFocused();

    @Override
    public int getHeight() {
        return this.height;
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

    @Unique @Override
    public void setPasswordField(boolean flag) {
        this.passwordField = flag;
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "drawTextBox", at = @At("RETURN"))
    public void drawTextFieldReturn(CallbackInfo ci) {
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

    @Redirect(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    public int render(FontRenderer fontRenderer, String text, float x, float y, int color) {
        String data = text;
        if (!text.equalsIgnoreCase("_") && passwordField) {
            StringBuilder hidden = new StringBuilder();
            for (int i = 0; i < data.length(); i++) {
                hidden.append("*");
            }
            data = hidden.toString();
        }
        return fontRenderer.drawStringWithShadow(data, x, y, color);
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
