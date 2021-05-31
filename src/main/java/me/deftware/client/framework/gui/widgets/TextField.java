package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.gui.GuiEventListener;
import me.deftware.mixin.imp.IMixinGuiTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

import java.util.function.Predicate;

/**
 * @author Deftware
 */
public class TextField extends GuiTextField implements GuiEventListener {

	public TextField(int id, int x, int y, int width, int height) {
		super(id, Minecraft.getInstance().fontRenderer, x, y, width, height);
	}

	public String getTextboxText() {
		return getText();
	}

	public void setTextboxPasswordMode(boolean flag) {
		((IMixinGuiTextField) this).setPasswordField(flag);
	}

	public void setTextboxPredicate(Predicate<String> textPredicate) {
		this.setValidator(textPredicate);
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

	public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
		return keyPressed(keyCode, scanCode, modifiers);
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

