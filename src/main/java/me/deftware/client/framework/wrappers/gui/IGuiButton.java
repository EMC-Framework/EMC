package me.deftware.client.framework.wrappers.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.mixin.imp.IMixinGuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public abstract class IGuiButton extends GuiButton implements CustomIGuiEventListener {
    private boolean shouldPlaySound = true;

    public IGuiButton(int id, int x, int y, ChatMessage buttonText) {
        super(id, x, y, 200, 20, buttonText.toString(true));
    }

    public IGuiButton(int id, int x, int y, int widthIn, int heightIn, ChatMessage buttonText) {
        super(id, x, y, widthIn, heightIn, buttonText.toString(true));
    }

    @Override
    public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        if (onDraw(mouseX, mouseY) == 0) {
            super.drawButton(minecraft, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
        if (super.mousePressed(minecraft, mouseX, mouseY)) {
            if (shouldPlaySound) this.playPressSound(Minecraft.getMinecraft().getSoundHandler());
            onButtonClick(x, y);
            return true;
        }
        return false;
    }

    public void doHandleMouse() {

    }

    public abstract void onButtonClick(double mouseX, double mouseY);

    protected int onDraw(int mouseX, int mouseY) {
        return 0;
    }

    protected boolean isVisible() {
        return visible;
    }

    public void setEnabled(boolean state) {
        enabled = state;
    }

    public void setVisible(boolean state) {
        visible = state;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected int getButtonX() {
        return x;
    }

    protected int getButtonY() {
        return y;
    }

    protected void setButtonY(int y) {
        this.y = y;
    }

    protected void setButtonX(int x) {
        this.x = x;
    }

    protected int getTheButtonWidth() {
        return width;
    }

    protected int getTheButtonHeight() {
        return height;
    }

    protected void setButtonWidth(int width) {
        this.width = width;
    }

    protected void setButtonHeight(int height) {
        this.height = height;
    }

    protected boolean isButtonHovered() {
        return hovered;
    }

    protected void setButtonHovered(boolean state) {
        hovered = state;
    }

    public ChatMessage getButtonText() {
        return new ChatMessage().fromString(displayString);
    }

    public IGuiButton setButtonText(ChatMessage text) {
        displayString = text.toString(true);
        return this;
    }

    public void resetToAfter(int ms, ChatMessage text) {
        new Thread(() -> {
            Thread.currentThread().setName("Button reset thread");
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ignored) {
            }
            setButtonText(text);
        }).start();
    }

    public boolean isShouldPlaySound() {
        return this.shouldPlaySound;
    }

    public void setShouldPlaySound(final boolean shouldPlaySound) {
        this.shouldPlaySound = shouldPlaySound;
    }
}
