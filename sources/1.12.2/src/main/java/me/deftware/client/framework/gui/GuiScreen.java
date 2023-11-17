package me.deftware.client.framework.gui;

import me.deftware.client.framework.gui.widgets.TextField;
import me.deftware.client.framework.gui.widgets.GenericComponent;
import me.deftware.client.framework.input.Mouse;
import net.minecraft.client.Minecraft;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.screens.GenericScreen;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.gui.widgets.Label;
import me.deftware.client.framework.helper.GlStateHelper;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatAllowedCharacters;
import me.deftware.client.framework.render.gl.GLX;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.io.IOException;

/**
 * @author Deftware
 */
public abstract class GuiScreen extends net.minecraft.client.gui.GuiScreen implements GenericScreen {

	public GenericScreen parent;

	private BackgroundType backgroundType = BackgroundType.Textured;

	public GuiScreen(GenericScreen parent) {
		this.parent = parent;
	}

	public GuiScreen() {
		this(null);
	}

	@Override
	public void mouseReleased(int x, int y, int button) {
		if (onMouseReleased(x, y, button))
			return;
		super.mouseReleased(x, y, button);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (onMouseClicked(mouseX, mouseY, mouseButton))
			return;
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (net.minecraft.client.Minecraft.getMinecraft().world != null)
			GlStateHelper.enableBlend();
		Mouse.updateMousePosition();
		GLX glx = GLX.getInstance();
		if (backgroundType != null)
			backgroundType.renderBackground(glx, mouseX, mouseY, partialTicks, this);
		super.drawScreen(mouseX, mouseY, partialTicks);
		onDraw(glx, mouseX, mouseY, partialTicks);
		onPostDraw(glx, mouseX, mouseY, partialTicks);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		// Emulate scrolling
		if (org.lwjgl.input.Mouse.hasWheel()) {
			int mY = org.lwjgl.input.Mouse.getEventDWheel();
			if (mY != 0) {
				Mouse.onScroll(0, mY < 0 ? -1 : 1);
			}
		}
	}

	@Override
	public void onResize(net.minecraft.client.Minecraft mcIn, int w, int h) {
		super.onResize(mcIn, w, h);
		onGuiResize(w, h);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		onUpdate();
		// Do textbox cursor tick
		getMinecraftScreen().getChildren(TextField.class)
				.forEach(field -> ((GuiTextField) field).updateCursorCounter());
	}

	@Override
	public void initGui() {
		super.initGui();
		onInitGui();
	}

	@Override
	public void onGuiClosed() {
		onGuiClose();
		super.onGuiClosed();
	}

	@Override
	public void handleKeyboardInput() throws IOException {
		char typedChar = Keyboard.getEventCharacter();
		int keyCode = Keyboard.getEventKey();
		if (Keyboard.getEventKeyState()) {
			// Pressed
			if (keyCode == Keyboard.KEY_ESCAPE && goBack())
				return;
			if (onKeyPressed(GLFW.toGLFW.getOrDefault(keyCode, keyCode), 0, getModifier()))
				return;
			if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
				GLFW.callbacks.forEach((c) -> c.invoke(0L, typedChar));
			super.keyTyped(typedChar, keyCode);
		} else {
			// Released
			onKeyReleased(GLFW.toGLFW.getOrDefault(keyCode, keyCode), 0, getModifier());
		}
	}

    private int getModifier() {
        if (isShiftKeyDown())
            return 0x1;
        if (isCtrlKeyDown())
            return 0x2;
        if (isAltKeyDown())
            return 0x4;
        return 0;
    }

	public void setBackgroundType(BackgroundType backgroundType) {
		this.backgroundType = backgroundType;
	}

	protected MinecraftScreen getMinecraftScreen() {
		return (MinecraftScreen) this;
	}

	protected void addComponent(GenericComponent component) {
		getMinecraftScreen().addScreenComponent(component);
	}

	protected GuiScreen addText(int x, int y, Message text) {
		addComponent(
				new Label(x, y, text)
		);
		return this;
	}

	protected GuiScreen addCenteredText(int x, int y, Message text) {
		addComponent(
				new Label(x - Minecraft.getMinecraft().fontRenderer.getStringWidth(text.string()) / 2, y, text)
		);
		return this;
	}

	protected boolean goBack() {
		Minecraft.getMinecraft().displayGuiScreen((net.minecraft.client.gui.GuiScreen) parent);
		return true;
	}

	public int getGuiScreenWidth() {
		return width;
	}

	public int getGuiScreenHeight() {
		return height;
	}

	protected void onGuiClose() { }

	protected abstract void onInitGui();

	protected void onPostDraw(GLX context, int mouseX, int mouseY, float partialTicks) { }

	protected abstract void onDraw(GLX context, int mouseX, int mouseY, float partialTicks);

	protected void onUpdate() { }

	protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
		return false;
	}

	protected boolean onKeyReleased(int keyCode, int scanCode, int modifiers) {
		return false;
	}

	protected boolean onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		return false;
	}

	protected boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		return false;
	}

	protected void onGuiResize(int w, int h) { }

	private static ScaledResolution getScaledResolution() {
		return new ScaledResolution(net.minecraft.client.Minecraft.getMinecraft());
	}

	public static int getScaledHeight() {
		return getScaledResolution().getScaledHeight();
	}

	public static int getScaledWidth() {
		return getScaledResolution().getScaledWidth();
	}

	public static int getDisplayWidth() {
		return Display.getWidth();
	}

	public static int getDisplayHeight() {
		return Display.getHeight();
	}

	public interface BackgroundType {

		/**
		 * No background will be rendered
		 */
		BackgroundType None = (context, mouseX, mouseY, delta, parent) -> { };

		/**
		 * A textured background will always be rendered
		 */
		BackgroundType Textured = (context, mouseX, mouseY, delta, parent) -> parent.drawBackground(0);

		/**
		 * A textured background will be rendered,
		 * but if a world is loaded, a transparent black
		 * overlay will be drawn instead
		 */
		BackgroundType TexturedOrTransparent = (context, mouseX, mouseY, delta, parent) -> parent.drawWorldBackground(0);

		/**
		 * Renders the background
		 */
		void renderBackground(GLX context, int mouseX, int mouseY, float delta, GuiScreen parent);

	}

}
