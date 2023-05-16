package me.deftware.client.framework.gui;

import me.deftware.client.framework.gui.widgets.GenericComponent;
import me.deftware.client.framework.input.Mouse;
import net.minecraft.client.Minecraft;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.screens.GenericScreen;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.gui.widgets.Label;
import me.deftware.client.framework.gui.widgets.TextField;
import me.deftware.client.framework.helper.GlStateHelper;
import net.minecraft.client.gui.GuiTextField;
import me.deftware.client.framework.render.gl.GLX;
import org.lwjgl.glfw.GLFW;

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
	public boolean mouseReleased(double x, double y, int button) {
		if (onMouseReleased((int) Math.round(x), (int) Math.round(y), button))
			return true;
		return super.mouseReleased(x, y, button);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (onMouseClicked((int) Math.round(mouseX), (int) Math.round(mouseY), mouseButton))
			return true;
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if (net.minecraft.client.Minecraft.getInstance().world != null)
			GlStateHelper.enableBlend();
		Mouse.updateMousePosition();
		GLX glx = GLX.getInstance();
		if (backgroundType != null)
			backgroundType.renderBackground(glx, mouseX, mouseY, partialTicks, this);
		super.render(mouseX, mouseY, partialTicks);
		onDraw(glx, mouseX, mouseY, partialTicks);
		onPostDraw(glx, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onResize(net.minecraft.client.Minecraft mcIn, int w, int h) {
		super.onResize(mcIn, w, h);
		onGuiResize(w, h);
	}

	@Override
	public void tick() {
		super.tick();
		onUpdate();
		// Do textbox cursor tick
		getMinecraftScreen().getChildren(TextField.class)
				.forEach(field -> ((GuiTextField) field).tick());
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
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE && goBack())
			return true;
		if (onKeyPressed(keyCode, scanCode, modifiers))
			return true;
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (onKeyReleased(keyCode, scanCode, modifiers))
			return true;
		return super.keyReleased(keyCode, scanCode, modifiers);
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
				new Label(x - Minecraft.getInstance().fontRenderer.getStringWidth(text.string()) / 2, y, text)
		);
		return this;
	}
	
	protected boolean goBack() {
		Minecraft.getInstance().displayGuiScreen((net.minecraft.client.gui.GuiScreen) parent);
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

	/**
	 * @see GLFW#GLFW_RELEASE

	 * @see GLFW#GLFW_PRESS

	 * @see GLFW#GLFW_REPEAT
	 */
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

	public static int getScaledHeight() {
		return Minecraft.getInstance().mainWindow.getScaledHeight();
	}

	public static int getScaledWidth() {
		return Minecraft.getInstance().mainWindow.getScaledWidth();
	}

	public static int getDisplayHeight() {
		return Minecraft.getInstance().mainWindow.getHeight();
	}

	public static int getDisplayWidth() {
		return Minecraft.getInstance().mainWindow.getWidth();
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
