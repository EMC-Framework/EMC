package me.deftware.client.framework.gui;

import com.google.common.collect.Iterables;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.gui.minecraft.ScreenInstance;
import me.deftware.client.framework.gui.widgets.Button;
import me.deftware.client.framework.gui.widgets.TextField;
import me.deftware.client.framework.input.Mouse;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.util.ResourceUtils;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.client.framework.util.types.Tuple;
import me.deftware.client.framework.world.World;
import me.deftware.mixin.imp.IMixinGuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Deftware
 */
public abstract class GuiScreen extends net.minecraft.client.gui.GuiScreen {

	public GuiScreen parent;
	protected boolean escGoesBack = true;
	protected ScreenInstance parentInstance;
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
		if (onMouseReleased(x, y, button))
			return;
		super.mouseReleased(x, y, button);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (onMouseClicked(mouseX, mouseY, mouseButton))
			return;
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
			fontRendererObj.drawStringWithShadow(text.getRight().toString(true), text.getLeft(), text.getMiddle(), 16777215);
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
	public void handleKeyboardInput() throws IOException {
		char typedChar = Keyboard.getEventCharacter();
		int keyCode = Keyboard.getEventKey();
		if (Keyboard.getEventKeyState()) {
			if (keyCode == Keyboard.KEY_ESCAPE) {
				if (escGoesBack) {
					goBack();
					return;
				}
				onGoBackRequested();
			} else {
				children.forEach(c -> {
					if (c instanceof TextField) {
						c.doKeyTyped(typedChar, keyCode);
					}
				});
				if (!onKeyPressed(GLFW.toGLFW.getOrDefault(keyCode, keyCode), 0, getModifier()))
					super.keyTyped(typedChar, keyCode);
				if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
					GLFW.callbacks.forEach((c) -> c.invoke(0L, typedChar));
			}
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
		compiledText.add(new Tuple<>(x - fontRendererObj.getStringWidth(text.toString(true)) / 2, y, text));
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

	public static void drawTexture(MinecraftIdentifier texture, int x, int y, int width, int height) {
		drawTexture(texture, x, y, 0, 0, width, height, width, height);
	}

	public static void drawTexture(MinecraftIdentifier texture, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight) {
		net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		net.minecraft.client.gui.GuiScreen.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
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

	protected void onUpdate() { }

	protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) { return false; }

	protected boolean onKeyReleased(int keyCode, int scanCode, int modifiers) { return false; }

	protected boolean onMouseReleased(int mouseX, int mouseY, int mouseButton) { return false; }

	protected boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) { return false; }

	protected void onGuiResize(int w, int h) { }

	protected boolean onGoBackRequested() {
		return false;
	}

	public List<Tuple<Integer, Integer, ChatMessage>> getCompiledText() {
		return this.compiledText;
	}
}
