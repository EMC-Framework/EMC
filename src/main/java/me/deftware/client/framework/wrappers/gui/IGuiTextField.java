package me.deftware.client.framework.wrappers.gui;

import me.deftware.mixin.imp.IMixinGuiTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.glfw.GLFW;

public class IGuiTextField extends GuiTextField implements CustomIGuiEventListener {

    public IGuiTextField(int id, int x, int y, int width, int height) {
        super(id,Minecraft.getInstance().fontRenderer, x, y, width, height);
    }

    public String getTextboxText() {
        return getText();
    }

    public void setTextboxText(String text) {
        setText(text);
    }

    public void setMaxTextboxLenght(int lenght) {
        setMaxStringLength(lenght);
    }

    public boolean isTextboxFocused() {
        return isFocused();
    }

    public void setTextboxFocused(boolean state) {
        setFocused(state);
    }

    /**
     * @see GLFW#GLFW_RELEASE
     * @see GLFW#GLFW_PRESS
     * @see GLFW#GLFW_REPEAT
     * @see GLFW#GLFW_MOD_SHIFT
     */
    public void onKeyPressed(int keyCode, int action, int modifiers) {
        keyPressed(keyCode, action, modifiers);
    }

    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        drawTextField(mouseX, mouseY, partialTicks);
    }

    public void doCursorTick() {
        tick();
    }

    public void setTextboxEnabled(boolean state) {
        setEnabled(state);
    }

    public int getPosX() {
        return ((IMixinGuiTextField) this).getX();
    }

    public void setPosX(int x) {
        ((IMixinGuiTextField) this).setX(x);
    }

    public int getPosY() {
        return ((IMixinGuiTextField) this).getY();
    }

    public void setPosY(int y) {
        ((IMixinGuiTextField) this).setY(y);
    }

}
