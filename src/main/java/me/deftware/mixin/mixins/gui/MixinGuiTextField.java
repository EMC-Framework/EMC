package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.gui.widgets.TextField;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.function.Predicate;

/**
 * @author Deftware
 */
@Mixin(EditBox.class)
public class MixinGuiTextField extends MixinClickableWidget implements TextField {

    @Unique
    private String overlay = "";

    @Unique
    private boolean passwordField = false;

    @Unique
    private Predicate<String> filter = java.util.Objects::nonNull;

    @Shadow
    @Final
    private Font font;

    @Inject(method = "extractWidgetRenderState", at = @At("RETURN"))
    public void drawTextFieldReturn(GuiGraphicsExtractor matrixStack, int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        EditBox self = (EditBox) (Object) this;
        if (!overlay.isEmpty()) {
            int currentWidth = font.width(self.getValue());
            int x = getPositionX(), y = getPositionY();
            if (self.isFocused()) {
                x += 4;
                y += (getComponentHeight() - 8) / 2;
            }
            matrixStack.text(font, overlay, x + currentWidth - 3, y - 2, Color.GRAY.getRGB() | 0xFF000000);
        }
    }

    @ModifyVariable(method = "applyFormat", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String onApplyFormat(String string) {
        if (this.passwordField && string != null) {
            return "*".repeat(string.length());
        }
        return string;
    }

    @Override
    public void _setText(String text) {
        ((EditBox) (Object) this).setValue(text);
    }

    @Override
    public String _getText() {
        return ((EditBox) (Object) this).getValue();
    }

    @Override
    public void _setPasswordMode(boolean state) {
        this.passwordField = true;
    }

    @Override
    public void _setMaxLength(int length) {
        ((EditBox) (Object) this).setMaxLength(length);
    }

    @Override
    public void _setOverlay(String text) {
        this.overlay = text;
    }

    @Override
    public void _setPredicate(Predicate<String> predicate) {
        this.filter = predicate;
    }

}
