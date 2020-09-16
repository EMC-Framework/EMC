package me.deftware.client.framework.gui;

import com.google.common.collect.Iterables;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.gui.minecraft.ScreenInstance;
import me.deftware.client.framework.gui.widgets.Button;
import me.deftware.client.framework.gui.widgets.TextField;
import me.deftware.client.framework.input.Mouse;
import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.texture.Texture;
import me.deftware.client.framework.util.ResourceUtils;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.client.framework.util.types.Tuple;
import me.deftware.client.framework.world.World;
import me.deftware.mixin.imp.IMixinGuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Deftware
 */
public abstract class GuiScreen extends net.minecraft.client.gui.GuiScreen {

	public GuiScreen parent;
	protected boolean escGoesBack = true;
	protected ScreenInstance parentInstance;
	protected HashMap<String, Texture> textureHashMap = new HashMap<>();
	protected List<Tuple<Integer, Integer, ChatMessage>> compiledText = new ArrayList<>();
	private List<GuiEventListener> children = new ArrayList<>();

	public GuiScreen() { }

	public GuiScreen(ScreenInstance parent) {
		this.parentInstance = parent;
	}

	public GuiScreen(GuiScreen parent) {
		this.parent = parent;
	}

	public static int getScaledHeight() {
		ScaledResolution r = new ScaledResolution(net.minecraft.client.Minecraft.getMinecraft());
		return r.getScaledHeight();
	}

	public static int getScaledWidth() {
		ScaledResolution r = new ScaledResolution(net.minecraft.client.Minecraft.getMinecraft());
		return r.getScaledWidth();
	}

	public static int getDisplayWidth() {
		return Display.getWidth();
	}

	public static int getDisplayHeight() {
		return Display.getHeight();
	}

	@Override
	public void mouseReleased(int x, int y, int button) {
		onMouseReleased(x, y, button);
		super.mouseReleased(x, y, button);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		onMouseClicked(mouseX, mouseY, mouseButton);
		children.forEach(c -> {
			if (c instanceof TextField) {
				c.doMouseClicked(mouseX, mouseY, mouseButton);
			}
		});
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (World.isLoaded()) GlStateManager.enableBlend();
		Mouse.updateMousePosition();
		onDraw(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
		for (Tuple<Integer, Integer, ChatMessage> text : compiledText) {
			fontRenderer.drawStringWithShadow(text.getRight().toString(true), text.getLeft(), text.getMiddle(), 16777215);
		}
		onPostDraw(mouseX, mouseY, partialTicks);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		children.forEach(GuiEventListener::doHandleMouse);
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
	@SuppressWarnings("OptionalGetWithoutIsPresent")
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			if (escGoesBack) {
				goBack();
				return;
			}
			onGoBackRequested();
			return;
		} else {
			children.forEach(c -> {
				if (c instanceof TextField) {
					c.doKeyTyped(typedChar, keyCode);
				}
			});
			keyPressed(GLFW.toGLFW.getOrDefault(keyCode, keyCode), 0, 0);
			if (keyCode == GLFW.GLFW_KEY_TAB && children.stream().anyMatch(e -> e instanceof GuiTextField)) {
				int i = Iterables.indexOf(children, e -> e instanceof GuiTextField && ((GuiTextField) e).isFocused());
				int newIndex = i == Iterables.indexOf(children, e -> e == children.stream().filter(t -> t instanceof GuiTextField).reduce((first, second) -> second).get()) || i == -1 ? Iterables.indexOf(children, e -> e == children.stream().filter(t -> t instanceof GuiTextField).findFirst().get()) : i + 1;
				if (i != -1 && ((GuiTextField) children.get(i)).isFocused()) {
					children.get(newIndex).focusChanged(true);
				}
				children.get(newIndex).focusChanged(true);
			}
			super.keyTyped(typedChar, keyCode);
		}
		GLFW.callbacks.forEach((c) -> c.invoke(0L, typedChar));
	}

	public boolean keyPressed(int keyCode, int action, int modifiers) {
		onKeyPressed(keyCode, action, modifiers);
		return true;
	}

	public void addEventListener(GuiEventListener listener) {
		this.children.add(listener);
	}

	protected void renderBackgroundWrap(int offset) {
		drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
	}

	protected void renderBackgroundTextureWrap(int offset) {
		drawWorldBackground(offset);
	}

	protected GuiScreen addButton(Button button) {
		children.add(button);
		buttonList.add(button);
		return this;
	}

	protected GuiScreen addText(int x, int y, ChatMessage text) {
		compiledText.add(new Tuple<>(x, y, text));
		return this;
	}

	protected GuiScreen addCenteredText(int x, int y, ChatMessage text) {
		compiledText.add(new Tuple<>(x - fontRenderer.getStringWidth(text.toString(true)) / 2, y, text));
		return this;
	}

	protected List<Button> getIButtonList() {
		return ((IMixinGuiScreen) this).getEmcButtons();
	}

	protected void clearButtons() {
		buttonList.clear();
		children.removeIf(element -> element instanceof Button);
	}

	protected void clearTexts() {
		compiledText.clear();
	}

	protected void drawTexture(EMCMod mod, String texture, int x, int y, int width, int height) {
		// TODO: Redo this function
		GL11.glPushMatrix();
		if (!textureHashMap.containsKey(texture)) {
			try {
				BufferedImage img = ImageIO.read(Objects.requireNonNull(ResourceUtils.getStreamFromModResources(mod, texture)));
				Texture tex = new Texture(img.getWidth(), img.getHeight(), true);
				tex.fillFromBufferedImageFlip(img);
				tex.update();
				tex.bind();
				textureHashMap.put(texture, tex);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			textureHashMap.get(texture).updateTexture();
		}
		drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
		GL11.glPopMatrix();
	}

	protected void drawTexture(MinecraftIdentifier texture, int x, int y, int width, int height) {
		net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		net.minecraft.client.gui.GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
	}

	protected void goBack() {
		if (parentInstance != null) {
			net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(parentInstance.getMinecraftScreen());
		} else {
			Minecraft.openScreen(parent);
		}
	}

	public int getGuiScreenWidth() {
		return width;
	}

	public int getGuiScreenHeight() {
		return height;
	}

	public void setFocusedComponent(GuiEventListener listener) {
		// Dummy method
	}

	protected void onGuiClose() { }

	protected abstract void onInitGui();

	protected void onPostDraw(int mouseX, int mouseY, float partialTicks) { }

	protected abstract void onDraw(int mouseX, int mouseY, float partialTicks);

	protected abstract void onUpdate();

	protected abstract void onKeyPressed(int keyCode, int action, int modifiers);

	protected abstract void onMouseReleased(int mouseX, int mouseY, int mouseButton);

	protected abstract void onMouseClicked(int mouseX, int mouseY, int mouseButton);

	protected abstract void onGuiResize(int w, int h);

	protected boolean onGoBackRequested() {
		return false;
	}

	public List<Tuple<Integer, Integer, ChatMessage>> getCompiledText() {
		return this.compiledText;
	}
}
