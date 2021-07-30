package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.gui.Drawable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.*;
import me.deftware.client.framework.gui.widgets.TextField;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * @author Deftware
 */
@Mixin(GuiTextField.class)
public class MixinGuiTextField implements TextField, Drawable {

    @Unique
    private String overlay = "";

    @Unique
    private boolean passwordField = false;

    @Shadow
    private BiFunction<String, Integer, String> textFormatter;

    @Mutable
    @Shadow
    @Final
    private int height;

    @Shadow
    private boolean isEnabled;

    @Mutable
    @Shadow
    @Final
    private int width;

    @Shadow
    @Final
    private FontRenderer fontRenderer;

    @Override
    public int getPositionX() {
        return ((GuiTextField) (Object) this).x;
    }

    @Override
    public int getPositionY() {
        return ((GuiTextField) (Object) this).y;
    }

    @Override
    public int getComponentWidth() {
        return ((GuiTextField) (Object) this).getWidth();
    }

    @Override
    public int getComponentHeight() {
        return this.height;
    }

    @Override
    public boolean isActive() {
        return this.isEnabled;
    }

    @Override
    public void setPositionX(int x) {
        ((GuiTextField) (Object) this).x = x;
    }

    @Override
    public void setPositionY(int y) {
        ((GuiTextField) (Object) this).y = y;
    }

    @Override
    public void setComponentWidth(int width) {
        this.width = width;
    }

    @Override
    public void setComponentHeight(int height) {
        this.height = height;
    }

    @Override
    public void setActive(boolean state) {
        this.isEnabled = state;
    }

    @Override
    public void _setText(String text) {
        ((GuiTextField) (Object) this).setText(text);
    }

    @Override
    public String _getText() {
        return ((GuiTextField) (Object) this).getText();
    }

    @Override
    public void _setPasswordMode(boolean state) {
        this.passwordField = state;
    }

    @Override
    public void _setMaxLength(int length) {
        ((GuiTextField) (Object) this).setMaxStringLength(length);
    }

    @Override
    public void _setOverlay(String text) {
        this.overlay = text;
    }

    @Override
    public void _setPredicate(Predicate<String> predicate) {
        ((GuiTextField) (Object) this).setValidator(predicate);
    }

    @Inject(method = "drawTextField", at = @At("RETURN"))
    public void drawTextFieldReturn(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        GuiTextField self = (GuiTextField) (Object) this;
        if (!overlay.isEmpty()) {
            int currentWidth = fontRenderer.getStringWidth(self.getText());
            int x = getPositionX(), y = getPositionY();
            if (self.isFocused()) {
                x += 4;
                y += (getComponentHeight() - 8) / 2;
            }
            fontRenderer.drawStringWithShadow(overlay, x + currentWidth - 3, y - 2, Color.GRAY.getRGB());
        }
    }

    @Redirect(method = "drawTextField", at = @At(value = "INVOKE", target = "Ljava/util/function/BiFunction;apply(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    public Object render(BiFunction<String, Integer, String> biFunction, Object text, Object index) {
        String data = (String) text;
        if (passwordField) {
            StringBuilder builder = new StringBuilder(data);
            for (int i = 0; i < data.length(); i++)
                builder.setCharAt(i, '*');
            data = builder.toString();
        }
        return textFormatter.apply(data, (int) index);
    }

    @Override
    public void onRender(int mouseX, int mouseY, float delta) {
        ((GuiTextField) (Object) this).drawTextField(mouseX, mouseY, delta);
    }

}
