package me.deftware.client.framework.gui;

import lombok.Setter;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.screens.GenericScreen;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.gui.widgets.GenericComponent;
import me.deftware.client.framework.gui.widgets.Label;
import me.deftware.client.framework.gui.widgets.TextField;
import me.deftware.client.framework.input.Mouse;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

/**
 * @author Deftware
 */
public abstract class GuiScreen extends Screen implements GenericScreen {

	public GenericScreen parent;

	@Setter
	private BackgroundType backgroundType = BackgroundType.Textured;

	public GuiScreen(GenericScreen parent) {
		super(Component.literal(""));
		this.parent = parent;
	}

	public GuiScreen() {
		this(null);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		int button = GLFW.sdlButtonToGlfwButton(event.button());
		if (onMouseReleased((int) Math.round(event.x()), (int) Math.round(event.y()), button))
			return true;
		return super.mouseReleased(event);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		int button = GLFW.sdlButtonToGlfwButton(event.button());
		if (onMouseClicked((int) Math.round(event.x()), (int) Math.round(event.y()), button))
			return true;
		return super.mouseClicked(event, doubleClick);
	}

	private GLX glx;

	@Override
	public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {
		Mouse.updateMousePosition();
		org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
		org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_STENCIL_TEST);
		glx = GLX.of(context);
		super.extractRenderState(context, mouseX, mouseY, partialTicks);
		onDraw(glx, mouseX, mouseY, partialTicks);
		onPostDraw(glx, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
		if (backgroundType == BackgroundType.TexturedOrTransparent) {
			super.extractBackground(context, mouseX, mouseY, delta);
			return;
		}
		if (backgroundType != null) {
			backgroundType.renderBackground(glx != null ? glx : GLX.of(context), mouseX, mouseY, delta, this);
		}
	}

	@Override
	public void resize(int w, int h) {
		super.resize(w, h);
		onGuiResize(w, h);
	}

	@Override
	public void tick() {
		super.tick();
		onUpdate();
	}

	@Override
	public void init() {
		Minecraft.getInstance().textInputManager().startTextInput(this);
		super.init();
		onInitGui();
	}

	@Override
	public void removed() {
		Minecraft.getInstance().textInputManager().stopTextInput(this);
		onGuiClose();
		super.removed();
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		int glfwKey = GLFW.sdlScancodeToGlfwKey(event.key(), event.keycode());
		int scanCode = event.key();
		int modifiers = GLFW.sdlModsToGlfwMods(event.modifiers());
		if (glfwKey == GLFW.GLFW_KEY_ESCAPE && goBack())
			return true;
		if (onKeyPressed(glfwKey, scanCode, modifiers))
			return true;
		return super.keyPressed(event);
	}

	@Override
	public boolean keyReleased(KeyEvent event) {
		int glfwKey = GLFW.sdlScancodeToGlfwKey(event.key(), event.keycode());
		int scanCode = event.key();
		int modifiers = GLFW.sdlModsToGlfwMods(event.modifiers());
		if (onKeyReleased(glfwKey, scanCode, modifiers))
			return true;
		return super.keyReleased(event);
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
				new Label(x - Minecraft.getInstance().font.width((Component) text) / 2, y, text)
		);
		return this;
	}

	protected boolean goBack() {
		Minecraft.getInstance().gui.setScreen((Screen) parent);
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
		return Minecraft.getInstance().getWindow().getGuiScaledHeight();
	}

	public static int getScaledWidth() {
		return Minecraft.getInstance().getWindow().getGuiScaledWidth();
	}

	public static int getDisplayHeight() {
		return Minecraft.getInstance().getWindow().getScreenHeight();
	}

	public static int getDisplayWidth() {
		return Minecraft.getInstance().getWindow().getScreenWidth();
	}

	public interface BackgroundType {

		/**
		 * No background will be rendered
		 */
		BackgroundType None = (context, mouseX, mouseY, delta, parent) -> { };

		/**
		 * A textured background will always be rendered
		 */
		BackgroundType Textured = (context, mouseX, mouseY, delta, parent) -> {
			if (Minecraft.getInstance().level == null) {
				parent.extractPanorama(context.getContext(), delta);
			}
			parent.extractBlurredBackground(context.getContext());
			parent.extractMenuBackground(context.getContext());
		};

		/**
		 * A textured background will be rendered,
		 * but if a world is loaded, a transparent black
		 * overlay will be drawn instead
		 */
		BackgroundType TexturedOrTransparent = (context, mouseX, mouseY, delta, parent) -> parent.extractBackground(context.getContext(), mouseX, mouseY, delta);

		/**
		 * Renders the background
		 */
		void renderBackground(GLX context, int mouseX, int mouseY, float delta, GuiScreen parent);

	}

}
