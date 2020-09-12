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
import java.util.function.BiFunction;

@Mixin(GuiTextField.class)
public abstract class MixinGuiTextField  implements IMixinGuiTextField {

    @Unique
    private boolean overlay = false, passwordField = false;

    @Unique
    private String overlayText = "";

    @Shadow
    private int maxStringLength;

    @Shadow
    private String suggestion;

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
    public int x;

    @Shadow
    public int y;

    @Shadow
    @Final
    private FontRenderer fontRenderer;

    @Shadow public abstract String getText();

    @Shadow public abstract boolean isFocused();

    @Shadow
    private BiFunction<String, Integer, String> textFormatter;

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
        return fontRenderer;
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
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
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
    @Inject(method = "drawTextField", at = @At("RETURN"))
    public void drawTextFieldReturn(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
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

    @Redirect(method = "drawTextField", at = @At(value = "INVOKE", target = "Ljava/util/function/BiFunction;apply(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    public Object render(BiFunction<String, Integer, String> biFunction, Object text, Object index) {
        String data = (String) text;
        if (passwordField) {
            StringBuilder hidden = new StringBuilder();
            for (int i = 0; i < data.length(); i++) {
                hidden.append("*");
            }
            data = hidden.toString();
        }
        return biFunction.apply(data, (int) index);
    }

    public int getMaxTextLength() {
        return maxStringLength;
    }

    @Override
    public String getSuggestion() {
        return suggestion;
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
