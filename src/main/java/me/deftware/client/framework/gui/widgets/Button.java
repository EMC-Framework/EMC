package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.gui.GuiEventListener;
import me.deftware.mixin.imp.IMixinGuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

/**
 * @author Deftware
 */
public abstract class Button extends GuiButton implements GuiEventListener {
	private boolean shouldPlaySound = true;

	public Button(int id, int x, int y, ChatMessage buttonText) {
		super(id, x, y, 200, 20, buttonText.toString(true));
	}

	public Button(int id, int x, int y, int widthIn, int heightIn, ChatMessage buttonText) {
		super(id, x, y, widthIn, heightIn, buttonText.toString(true));
	}

	@Override
	public void render(int mouseX, int mouseY, float float_1) {
		if (onDraw(mouseX, mouseY) == 0) {
			super.render(mouseX, mouseY, float_1);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0 && this.isPressable(mouseX, mouseY)) {
			if (shouldPlaySound) this.playPressSound(Minecraft.getInstance().getSoundHandler());
			onButtonClick(x, y);
			return true;
		}
		return false;
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

	protected void setButtonX(int x) {
		this.x = x;
	}

	protected int getButtonY() {
		return y;
	}

	protected void setButtonY(int y) {
		this.y = y;
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
		((IMixinGuiButton) this).setIsHovered(state);
	}

	public ChatMessage getButtonText() {
		return new ChatMessage().fromString(displayString);
	}

	public Button setButtonText(ChatMessage text) {
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
