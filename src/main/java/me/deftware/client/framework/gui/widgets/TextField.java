package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.gui.GuiEventListener;
import me.deftware.mixin.imp.IMixinGuiTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.util.function.Predicate;

/**
 * @author Deftware
 */
public class TextField extends GuiTextField implements GuiEventListener {

	public TextField(int id, int x, int y, int width, int height) {
		super(id, Minecraft.getMinecraft().fontRenderer, x, y, width, height);
	}

	public String getTextboxText() {
		return getText();
	}

	public void setTextboxPasswordMode(boolean flag) {
		((IMixinGuiTextField) this).setPasswordField(flag);
	}

	public void setTextboxPredicate(Predicate<String> textPredicate) {
		// Because Minecraft doesnt use the java type...
		this.setValidator(textPredicate::test);
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

	public void onKeyPressed(int keyCode, int action, int modifiers) {
		textboxKeyTyped(Keyboard.getEventCharacter(), keyCode);
	}

	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		drawTextBox();
	}

	public void doCursorTick() {
		updateCursorCounter();
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

	@Override
	public void doHandleMouse() { }

	@Override
	public void doKeyTyped(char typedChar, int keyCode) {
		textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	public void doMouseClicked(int mouseX, int mouseY, int mouseButton) {
		mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void focusChanged(boolean state) { }

}

