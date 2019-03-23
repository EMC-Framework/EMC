package me.deftware.client.framework.wrappers.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import me.deftware.client.framework.utils.ResourceUtils;
import me.deftware.client.framework.utils.render.Texture;
import me.deftware.client.framework.wrappers.IMinecraft;
import me.deftware.client.framework.wrappers.IResourceLocation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.InputListener;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.SystemUtil;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class IGuiScreen extends Screen {

    private boolean pause = true;
    private HashMap<String, Texture> textureHashMap = new HashMap<>();
    protected IGuiScreen parent = null;

    public IGuiScreen(boolean doesGuiPause) {
        pause = doesGuiPause;
    }

    public IGuiScreen() {
        this(true);
    }

    public IGuiScreen(IGuiScreen parent) {
        this(true);
        this.parent = parent;
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
        try {
            StringSelection stringselection = new StringSelection(copyText);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, (ClipboardOwner) null);
        } catch (Exception var2) {
            ;
        }
    }

    public static void openLink(String url) {
        SystemUtil.getOperatingSystem().open(url);
    }

    public static boolean isCtrlPressed() {
        return isControlPressed();
    }

    public static int getScaledHeight() {
        return MinecraftClient.getInstance().window.getScaledHeight();
    }

    public static int getScaledWidth() {
        return MinecraftClient.getInstance().window.getScaledWidth();
    }

    public static int getDisplayHeight() {
        return MinecraftClient.getInstance().window.getHeight();
    }

    public static int getDisplayWidth() {
        return MinecraftClient.getInstance().window.getWidth();
    }

    public static boolean isWindowMinimized() {
        if (getDisplayWidth() == 0 || getDisplayHeight() == 0)
            return true;
        return false;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        onDraw(mouseX, mouseY, partialTicks);
        super.draw(mouseX, mouseY, partialTicks);
        onPostDraw(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return pause;
    }

    @Override
    public void onScaleChanged(MinecraftClient mcIn, int w, int h) {
        super.onScaleChanged(mcIn, w, h);
        onGuiResize(w, h);
    }

    @Override
    public void update() {
        super.update();
        onUpdate();
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        onInitGui();
        listeners.add(new InputListener() {

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
    public void onClosed() {
        onGuiClose();
        super.onClosed();
    }

    @Override
    public boolean keyPressed(int keyCode, int action, int modifiers) {
        onKeyPressed(keyCode, action, modifiers);
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            IMinecraft.setGuiScreen(null);
        }
        return true;
    }

    public void addEventListener(CustomIGuiEventListener listener) {
        this.listeners.add(listener);
    }

    protected void drawIDefaultBackground() {
        drawBackground();
    }

    public void drawDarkOverlay() {
        DrawableHelper.drawRect(0, 0, screenWidth, screenHeight, Integer.MIN_VALUE);
    }

    protected List<AbstractButtonWidget> getButtonList() {
        return buttons;
    }

    protected void addButton(IGuiButton button) {
        listeners.add(button);
        buttons.add(button);
    }

    protected ArrayList<IGuiButton> getIButtonList() {
        ArrayList<IGuiButton> list = new ArrayList<>();
        for (AbstractButtonWidget b : buttons) {
            if (b instanceof IGuiButton) {
                list.add((IGuiButton) b);
            }
        }
        return list;
    }

    protected void clearButtons() {
        buttons.clear();
    }

    public void drawCenteredString(String text, int x, int y, int color) {
        MinecraftClient.getInstance().textRenderer.drawWithShadow(text,
                x - MinecraftClient.getInstance().textRenderer.getStringWidth(text) / 2, y, color);
    }

    public void setDoesGuiPauseGame(boolean state) {
        pause = state;
    }

    protected void drawTexture(String mod, String texture, int x, int y, int width, int height) {
        GL11.glPushMatrix();
        if (!textureHashMap.containsKey(texture)) {
            try {
                BufferedImage img = ImageIO.read(ResourceUtils.getStreamFromModResources(mod, texture));
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
        Screen.drawTexturedRect(x, y, 0, 0, width, height, width, height);
        GL11.glPopMatrix();
    }

    protected void drawTexture(IResourceLocation texture, int x, int y, int width, int height) {
        MinecraftClient.getInstance().getTextureManager().bindTexture(texture);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Screen.drawTexturedRect(x, y, 0, 0, width, height, width, height);
    }

    public int getIGuiScreenWidth() {
        return screenWidth;
    }

    public int getIGuiScreenHeight() {
        return screenHeight;
    }

    public void drawITintBackground(int tint) {
        drawTextureBackground(tint);
    }

    public void setFocusedComponent(CustomIGuiEventListener listener) {
        this.setFocused(listener);
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
