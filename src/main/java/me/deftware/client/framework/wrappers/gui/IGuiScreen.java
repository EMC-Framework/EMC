package me.deftware.client.framework.wrappers.gui;

import com.google.common.collect.Iterables;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.utils.ResourceUtils;
import me.deftware.client.framework.utils.Tuple;
import me.deftware.client.framework.utils.render.Texture;
import me.deftware.client.framework.wrappers.IMinecraft;
import me.deftware.client.framework.wrappers.IResourceLocation;
import me.deftware.client.framework.wrappers.gui.imp.ScreenInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Deftware
 */
public abstract class IGuiScreen extends GuiScreen {
    protected IGuiScreen parent;
    protected boolean escGoesBack = true;
    protected ScreenInstance parentInstance;
    protected HashMap<String, Texture> textureHashMap = new HashMap<>();
    protected List<Tuple<Integer, Integer, ChatMessage>> compiledText = new ArrayList<>();

    public IGuiScreen() {
        // N/A
    }

    public IGuiScreen(ScreenInstance parent) {
        this.parentInstance = parent;
    }

    public IGuiScreen(IGuiScreen parent) {
        this.parent = parent;
    }

    public static String getClipboardString() {
        return Minecraft.getInstance().keyboardListener.getClipboardString();
    }

    public static void setClipboardString(String copyText) {
        Minecraft.getInstance().keyboardListener.setClipboardString(copyText);
    }

    public static void openLink(String url) {
        Util.getOSType().openURI(url);
    }

    public static boolean isCtrlPressed() {
        return isCtrlKeyDown();
    }

    public static boolean isShiftPressed() {
        return isShiftKeyDown();
    }

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

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        onMouseReleased((int) Math.round(x), (int) Math.round(y), button);
        super.mouseReleased(x, y, button);
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        onMouseClicked((int) Math.round(mouseX), (int) Math.round(mouseY), mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        onDraw(mouseX, mouseY, partialTicks);
        super.render(mouseX, mouseY, partialTicks);
        for (Tuple<Integer, Integer, ChatMessage> text : compiledText) {
            fontRenderer.drawStringWithShadow(text.getRight().toString(true), text.getLeft(), text.getMiddle(), 16777215);
        }
        onPostDraw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        super.onResize(mcIn, w, h);
        onGuiResize(w, h);
    }

    @Override
    public void tick() {
        super.tick();
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
    public boolean keyPressed(int keyCode, int action, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            if (escGoesBack) {
                goBack();
                return true;
            }
            return onGoBackRequested();
        } else {
            onKeyPressed(keyCode, action, modifiers);
            if (keyCode == GLFW.GLFW_KEY_TAB && children.stream().anyMatch(e -> e instanceof GuiTextField)) {
                int i = Iterables.indexOf(children, e -> e instanceof GuiTextField && ((GuiTextField) e).isFocused());
                int newIndex = i == Iterables.indexOf(children, e -> e == children.stream().filter(t -> t instanceof GuiTextField).reduce((first, second) -> second).get()) || i == -1 ? Iterables.indexOf(children, e -> e == children.stream().filter(t -> t instanceof GuiTextField).findFirst().get()) : i + 1;
                if (i != -1 && ((GuiTextField) children.get(i)).isFocused()) {
                    children.get(newIndex).focusChanged(true);
                }
                children.get(newIndex).focusChanged(true);
            }
            super.keyPressed(keyCode, action, modifiers);
        }
        return false;
    }

    public void addEventListener(CustomIGuiEventListener listener) {
        this.children.add(listener);
    }

    public void addRawEventListener(IGuiEventListener listener) {
        this.children.add(listener);
    }

    /**
     * @param offset Default value is 0
     */
    protected void renderBackgroundWrap(int offset) {
        drawBackground(offset);
    }

    protected void renderBackgroundTextureWrap(int offset) {
        this.drawWorldBackground(offset);
    }

    protected IGuiScreen addButton(IGuiButton button) {
        children.add(button);
        buttons.add(button);
        return this;
    }

    protected IGuiScreen addText(int x, int y, ChatMessage text) {
        compiledText.add(new Tuple<>(x, y, text));
        return this;
    }

    protected IGuiScreen addCenteredText(int x, int y, ChatMessage text) {
        compiledText.add(new Tuple<>(x - Minecraft.getInstance().fontRenderer.getStringWidth(text.toString(true)) / 2, y, text));
        return this;
    }

    protected ArrayList<IGuiButton> getIButtonList() {
        ArrayList<IGuiButton> list = new ArrayList<>();
        for (GuiButton b : buttons) {
            if (b instanceof IGuiButton) {
                list.add((IGuiButton) b);
            }
        }
        return list;
    }

    protected void clearButtons() {
        buttons.clear();
        children.removeIf(element -> element instanceof IGuiButton);
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
        GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GL11.glPopMatrix();
    }

    protected void drawTexture(IResourceLocation texture, int x, int y, int width, int height) {
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
    }

    protected void goBack() {
        if (parentInstance != null) {
            Minecraft.getInstance().displayGuiScreen(parentInstance.screen);
        } else {
            IMinecraft.setGuiScreen(parent);
        }
    }

    public int getIGuiScreenWidth() {
        return width;
    }

    public int getIGuiScreenHeight() {
        return height;
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

    protected boolean onGoBackRequested() {
        return false;
    }

    public List<Tuple<Integer, Integer, ChatMessage>> getCompiledText() {
        return this.compiledText;
    }
}
