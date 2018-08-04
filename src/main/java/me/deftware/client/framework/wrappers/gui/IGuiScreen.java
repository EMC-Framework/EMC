package me.deftware.client.framework.wrappers.gui;

import java.awt.*;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.util.StringUtils;

import me.deftware.client.framework.wrappers.IResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.glfw.GLFW;

public abstract class IGuiScreen extends GuiScreen {

	private boolean pause = true;

	public IGuiScreen(boolean doesGuiPause) {
		pause = doesGuiPause;
	}

	public IGuiScreen() {
		this(true);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		onDraw(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
		onPostDraw(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return pause;
	}

	@Override
	public void onResize(Minecraft mcIn, int w, int h) {
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
		field_195124_j.add(new IGuiEventListener() {

			@Override
			public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
				onMouseClicked((int) Math.round(mouseX), (int) Math.round(mouseY), mouseButton);
				return false;
			}

			@Override
			public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
				onMouseReleased((int) Math.round(mouseX), (int) Math.round(mouseY), mouseButton);
				return false;
			}

		});
	}

	@Override
	public void onGuiClosed() {
		onGuiClose();
		super.onGuiClosed();
	}

	@Override
	public boolean keyPressed(int keyCode, int action, int modifiers) {
		onKeyPressed(keyCode, action, modifiers);
		return true;
	}

	public void addEventListener(CustomIGuiEventListener listener) {
		this.field_195124_j.add(listener);
	}

	protected void drawIDefaultBackground() {
		drawDefaultBackground();
	}

	public void drawDarkOverlay() {
		Gui.drawRect(0, 0, width, height, Integer.MIN_VALUE);
	}

	protected List<GuiButton> getButtonList() {
		return buttonList;
	}

	protected void addButton(IGuiButton button) {
		buttonList.add(button);
		field_195124_j.add(button);
	}

	protected ArrayList<IGuiButton> getIButtonList() {
		ArrayList<IGuiButton> list = new ArrayList<>();
		for (GuiButton b : buttonList) {
			if (b instanceof IGuiButton) {
				list.add((IGuiButton) b);
			}
		}
		return list;
	}

	protected void clearButtons() {
		buttonList.clear();
	}

	/**
	 * Returns a string stored in the system clipboard.
	 */
	public static String getClipboardString() {
		try {
			Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object) null);

			if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return (String) transferable.getTransferData(DataFlavor.stringFlavor);
			}
		} catch (Exception var1) {
			;
		}

		return "";
	}

	/**
	 * Stores the given string in the system clipboard
	 */
	public static void setClipboardString(String copyText) {
		if (!StringUtils.isNullOrEmpty(copyText)) {
			try {
				StringSelection stringselection = new StringSelection(copyText);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, (ClipboardOwner) null);
			} catch (Exception var2) {
				;
			}
		}
	}

	public static void openLink(String url) {
		try {
			Desktop.getDesktop().browse(new URL(url).toURI());
		} catch (Exception e) {
		}
	}

	public void drawCenteredString(String text, int x, int y, int color) {
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text,
				x - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2, y, color);
	}

	public void setDoesGuiPauseGame(boolean state) {
		pause = state;
	}

	protected void drawTexture(IResourceLocation texture, int x, int y, int width, int height) {
		mc.getTextureManager().bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
	}

	public int getIGuiScreenWidth() {
		return width;
	}

	public int getIGuiScreenHeight() {
		return height;
	}

	public static int getScaledHeight() {
		return Minecraft.getMinecraft().mainWindow.getScaledHeight();
	}

	public static int getScaledWidth() {
		return Minecraft.getMinecraft().mainWindow.getScaledWidth();
	}

	public static int getDisplayHeight() {
		return Minecraft.getMinecraft().mainWindow.getHeight();
	}

	public static int getDisplayWidth() {
		return Minecraft.getMinecraft().mainWindow.getWidth();
	}

	public void drawITintBackground(int tint) {
		drawBackground(tint);
	}

	protected void onGuiClose() {
	}

	protected abstract void onInitGui();

	protected void onPostDraw(int mouseX, int mouseY, float partialTicks) {
	}

	protected abstract void onDraw(int mouseX, int mouseY, float partialTicks);

	protected abstract void onUpdate();

	/**
	 * @see GLFW#GLFW_RELEASE
	 * @see GLFW#GLFW_PRESS
	 * @see GLFW#GLFW_REPEAT
	 */
	protected abstract void onKeyPressed(int keyCode, int action, int modifiers);

	protected abstract void onMouseReleased(int mouseX, int mouseY, int mouseButton);

	protected abstract void onMouseClicked(int mouseX, int mouseY, int mouseButton);

	protected abstract void onGuiResize(int w, int h);

}
