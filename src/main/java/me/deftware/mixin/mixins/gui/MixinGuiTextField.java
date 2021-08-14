package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.gui.widgets.TextField;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
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
@Mixin(TextFieldWidget.class)
public class MixinGuiTextField extends MixinGuiButton implements TextField {

    @Unique
    private String overlay = "";

    @Unique
    private boolean passwordField = false;

    @Shadow
    private BiFunction<String, Integer, String> renderTextProvider;

    @Shadow
    @Final
    private TextRenderer textRenderer;

    @Inject(method = "renderButton", at = @At("RETURN"))
    public void drawTextFieldReturn(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        TextFieldWidget self = (TextFieldWidget) (Object) this;
        if (!overlay.isEmpty()) {
            int currentWidth = textRenderer.getStringWidth(self.getText());
            int x = getPositionX(), y = getPositionY();
            if (self.isFocused()) {
                x += 4;
                y += (getComponentHeight() - 8) / 2;
            }
            textRenderer.drawWithShadow(overlay, x + currentWidth - 3, y - 2, Color.GRAY.getRGB());
        }
    }

    @Redirect(method = "renderButton", at = @At(value = "INVOKE", target = "Ljava/util/function/BiFunction;apply(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    public Object render(BiFunction<String, Integer, String> biFunction, Object text, Object index) {
        String data = (String) text;
        if (passwordField) {
            StringBuilder builder = new StringBuilder(data);
            for (int i = 0; i < data.length(); i++)
                builder.setCharAt(i, '*');
            data = builder.toString();
        }
        return renderTextProvider.apply(data, (int) index);
    }

    @Override
    public void _setText(String text) {
        ((TextFieldWidget) (Object) this).setText(text);
    }

    @Override
    public String _getText() {
        return ((TextFieldWidget) (Object) this).getText();
    }

    @Override
    public void _setPasswordMode(boolean state) {
        this.passwordField = true;
    }

    @Override
    public void _setMaxLength(int length) {
        ((TextFieldWidget) (Object) this).setMaxLength(length);
    }

    @Override
    public void _setOverlay(String text) {
        this.overlay = text;
    }

    @Override
    public void _setPredicate(Predicate<String> predicate) {
        ((TextFieldWidget) (Object) this).setTextPredicate(predicate);
    }

}
