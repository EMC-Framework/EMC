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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * @author Deftware
 */
public abstract class GuiScreen extends Screen implements GenericScreen {

	public GenericScreen parent;

	@Setter
	private BackgroundType backgroundType = BackgroundType.Textured;

	public GuiScreen(GenericScreen parent) {
		super(Text.literal(""));
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

	private GLX glx;

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
		Mouse.updateMousePosition();
		glx = GLX.of(context);
		super.render(context, mouseX, mouseY, partialTicks);
		onDraw(glx, mouseX, mouseY, partialTicks);
		onPostDraw(glx, mouseX, mouseY, partialTicks);
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		if (backgroundType == BackgroundType.TexturedOrTransparent) {
			super.renderBackground(context, mouseX, mouseY, delta);
			return;
		}
		if (backgroundType != null) {
			backgroundType.renderBackground(glx, mouseX, mouseY, delta, this);
		}
	}

	@Override
	public void resize(MinecraftClient mcIn, int w, int h) {
		super.resize(mcIn, w, h);
		onGuiResize(w, h);
	}

	@Override
	public void tick() {
		super.tick();
		onUpdate();
	}

	@Override
	public void init() {
		super.init();
		onInitGui();
	}

	@Override
	public void removed() {
		onGuiClose();
		super.removed();
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
				new Label(x - MinecraftClient.getInstance().textRenderer.getWidth((Text) text) / 2, y, text)
		);
		return this;
	}

	protected boolean goBack() {
		MinecraftClient.getInstance().setScreen((Screen) parent);
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
		return MinecraftClient.getInstance().getWindow().getScaledHeight();
	}

	public static int getScaledWidth() {
		return MinecraftClient.getInstance().getWindow().getScaledWidth();
	}

	public static int getDisplayHeight() {
		return MinecraftClient.getInstance().getWindow().getHeight();
	}

	public static int getDisplayWidth() {
		return MinecraftClient.getInstance().getWindow().getWidth();
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
			if (MinecraftClient.getInstance().world == null) {
				parent.renderPanoramaBackground(context.getContext(), delta);
			}
			parent.applyBlur(delta);
			parent.renderDarkening(context.getContext());
		};

		/**
		 * A textured background will be rendered,
		 * but if a world is loaded, a transparent black
		 * overlay will be drawn instead
		 */
		BackgroundType TexturedOrTransparent = (context, mouseX, mouseY, delta, parent) -> parent.renderBackground(context.getContext(), mouseX, mouseY, delta);

		/**
		 * Renders the background
		 */
		void renderBackground(GLX context, int mouseX, int mouseY, float delta, GuiScreen parent);

	}

}
