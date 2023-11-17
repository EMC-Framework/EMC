package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.gui.Drawable;
import me.deftware.client.framework.gui.Element;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Deftware
 */
@Mixin(GuiTextField.class)
public class MixinGuiTextField implements TextField, Drawable, Element {

    @Unique
    private String overlay = "";

    @Unique
    private boolean passwordField = false;

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
    private FontRenderer fontRendererInstance;

    @Override
    public int getPositionX() {
        return ((GuiTextField) (Object) this).xPosition;
    }

    @Override
    public int getPositionY() {
        return ((GuiTextField) (Object) this).yPosition;
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
        ((GuiTextField) (Object) this).xPosition = x;
    }

    @Override
    public void setPositionY(int y) {
        ((GuiTextField) (Object) this).yPosition = y;
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
        ((GuiTextField) (Object) this).setValidator(predicate::test);
    }

    @Inject(method = "drawTextBox", at = @At("RETURN"))
    public void drawTextFieldReturn(CallbackInfo ci) {
        GuiTextField self = (GuiTextField) (Object) this;
        if (!overlay.isEmpty()) {
            int currentWidth = fontRendererInstance.getStringWidth(self.getText());
            int x = getPositionX(), y = getPositionY();
            if (self.isFocused()) {
                x += 4;
                y += (getComponentHeight() - 8) / 2;
            }
            fontRendererInstance.drawStringWithShadow(overlay, x + currentWidth - 3, y - 2, Color.GRAY.getRGB());
        }
    }

    @Redirect(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    public int render(FontRenderer fontRenderer, String data, float x, float y, int color) {
        if (passwordField) {
            StringBuilder builder = new StringBuilder(data);
            for (int i = 0; i < data.length(); i++)
                builder.setCharAt(i, '*');
            data = builder.toString();
        }
        return fontRenderer.drawStringWithShadow(data, x, y, color);
    }

    @Override
    public void onRender(int mouseX, int mouseY, float delta) {
        ((GuiTextField) (Object) this).drawTextBox();
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        ((GuiTextField) (Object) this).mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        ((GuiTextField) (Object) this).textboxKeyTyped(typedChar, keyCode);
    }

    @Unique
    private final java.util.List<String> tooltipComponents = new ArrayList<>();

    @Override
    public List<String> getTooltipComponents(int mouseX, int mouseY) {
        return this.tooltipComponents;
    }

}
