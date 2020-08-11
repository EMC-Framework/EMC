package me.deftware.client.framework.wrappers.gui;

import com.google.common.collect.Iterables;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.utils.ResourceUtils;
import me.deftware.client.framework.utils.Tuple;
import me.deftware.client.framework.utils.render.Texture;
import me.deftware.client.framework.wrappers.IMinecraft;
import me.deftware.client.framework.wrappers.IMouse;
import me.deftware.client.framework.wrappers.IResourceLocation;
import me.deftware.client.framework.wrappers.gui.imp.ScreenInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Deftware
 */
public abstract class IGuiScreen extends GuiScreen {
    protected IGuiScreen parent;
    protected boolean escGoesBack = true;
    protected ScreenInstance parentInstance;
    protected HashMap<String, Texture> textureHashMap = new HashMap<>();
    protected List<Tuple<Integer, Integer, ChatMessage>> compiledText = new ArrayList<>();
    private List<CustomIGuiEventListener> children = new ArrayList<>();

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
        try {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception ignored) {
        }

        return "";
    }

    public static void setClipboardString(String copyText) {
        try {
            StringSelection stringselection = new StringSelection(copyText);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
        } catch (Exception ignored) {
        }
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
            if (c instanceof IGuiTextField) {
                c.doMouseClicked(mouseX, mouseY, mouseButton);
            }
        });
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        onDraw(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (Tuple<Integer, Integer, ChatMessage> text : compiledText) {
            fontRendererObj.drawStringWithShadow(text.getRight().toString(true), text.getLeft(), text.getMiddle(), 16777215);
        }
        onPostDraw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        super.onResize(mcIn, w, h);
        onGuiResize(w, h);
    }

    public void addEventListener(CustomIGuiEventListener listener) {
        children.add(listener);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        children.forEach(CustomIGuiEventListener::doHandleMouse);
        if (Mouse.hasWheel()) {
            int mY = Mouse.getEventDWheel();
            if (mY != 0) {
                IMouse.onScroll(0, mY < 0 ? -1 : 1);
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        onUpdate();
    }

    @Override
    public void initGui() {
        super.initGui();
        children.clear();
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
                if (c instanceof IGuiTextField) {
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

    /**
     * @param offset Default value is 0
     */
    protected void renderBackgroundWrap(int offset) {
        drawWorldBackground(offset);
    }

    protected void renderBackgroundTextureWrap(int offset) {
        this.drawBackground(offset);
    }

    protected IGuiScreen addButton(IGuiButton button) {
        children.add(button);
        buttonList.add(button);
        return this;
    }

    protected IGuiScreen addText(int x, int y, ChatMessage text) {
        compiledText.add(new Tuple<>(x, y, text));
        return this;
    }

    protected IGuiScreen addCenteredText(int x, int y, ChatMessage text) {
        compiledText.add(new Tuple<>(x - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text.toString(true)) / 2, y, text));
        return this;
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
        GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GL11.glPopMatrix();
    }

    public static boolean isCtrlPressed() {
        return isCtrlKeyDown();
    }

    protected void goBack() {
        if (parentInstance != null) {
            Minecraft.getMinecraft().displayGuiScreen(parentInstance.screen);
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

    public static int getDisplayWidth() {
        return Display.getWidth();
    }

    public static int getDisplayHeight() {
        return Display.getHeight();
    }

    public static int getScaledHeight() {
        ScaledResolution r = new ScaledResolution(Minecraft.getMinecraft());
        return r.getScaledHeight();
    }

    public static int getScaledWidth() {
        ScaledResolution r = new ScaledResolution(Minecraft.getMinecraft());
        return r.getScaledWidth();
    }

    public void setFocusedComponent(CustomIGuiEventListener listener) {
        //this.setFocused(listener);
    }

    protected void onGuiClose() {
    }

    protected abstract void onInitGui();

    protected void onPostDraw(int mouseX, int mouseY, float partialTicks) {
    }

    protected abstract void onDraw(int mouseX, int mouseY, float partialTicks);

    protected abstract void onUpdate();

    protected abstract void onKeyPressed(char typedChar, int keyCode) throws IOException;

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
