package me.deftware.client.framework.fonts;

import me.deftware.client.framework.utils.Texture;
import me.deftware.client.framework.wrappers.IMinecraft;
import me.deftware.client.framework.wrappers.gui.IGuiScreen;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class DynamicFont implements EMCFont {

    protected Texture textTexture;
    protected int lastRenderedWidth;
    protected int lastRenderedHeight;
    protected int rendererTaskId;

    protected String fontName;
    protected int fontSize, shadowSize = 1;
    protected boolean bold;
    protected boolean italics;
    protected boolean underlined;
    protected boolean striked;
    protected boolean moving;
    protected boolean antialiased;
    protected boolean memorysaving;
    protected java.awt.Font stdFont;

    protected HashMap<String, Texture> textureStore = new HashMap<>();

    public DynamicFont(@Nonnull String fontName, int fontSize, int modifiers) {
        this.fontName = fontName;
        this.fontSize = fontSize;

        this.bold = ((modifiers & 1) != 0);
        this.italics = ((modifiers & 2) != 0);
        this.underlined = ((modifiers & 4) != 0);
        this.striked = ((modifiers & 8) != 0);
        this.moving = ((modifiers & 16) != 0);
        this.antialiased = ((modifiers & 32) != 0);
        this.memorysaving = ((modifiers & 64) != 0);

        if (!bold && !italics) {
            this.stdFont = new java.awt.Font(fontName, java.awt.Font.PLAIN, fontSize);
        } else {
            if (bold && italics) {
                this.stdFont = new java.awt.Font(fontName, java.awt.Font.BOLD | java.awt.Font.ITALIC, fontSize);
            } else if (!bold) { //One of them must be false by now so we have to check only one
                this.stdFont = new java.awt.Font(fontName, java.awt.Font.ITALIC, fontSize);
            } else {
                this.stdFont = new java.awt.Font(fontName, java.awt.Font.BOLD, fontSize);
            }
        }

        textTexture = null;
        rendererTaskId = 0;
        lastRenderedWidth = 0;
        lastRenderedHeight = 0;
    }

    @Override
    public void clearCache() {
        for (String key : textureStore.keySet()) {
            textureStore.get(key).destroy();
        }
        textureStore.clear();
    }

    @Override
    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setAntialiased(boolean antialiased) {
        this.antialiased = antialiased;
    }

    public void setMemorysaving(boolean memorysaving) {
        this.memorysaving = memorysaving;
        if (!memorysaving)
            textureStore.clear();
    }

    public void prepareAndPushMatrix() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST); //3008 alpha test
        GL11.glPushMatrix();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Transparency blending
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, IGuiScreen.getDisplayWidth(), IGuiScreen.getDisplayHeight(), 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    public void renderAndPopMatrix(int x, int y, int width, int height) {
        //Draw quad counterclockwise
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2d(x, y); //top-left
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2d(x, y + height); //down-left aka height
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2d(x + width, y + height); //down-right aka width
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2d(x + width, y); //top-right
        }
        GL11.glEnd();

        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        IMinecraft.triggerGuiRenderer();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public int generateString(String text, Color color) {
        String key = text + color.getRGB() + bold + fontName;
        int textwidth = getStringWidth(text);
        int textheight = getStringHeight(text);
        if (!memorysaving && textureStore.containsKey(key)) {
            textTexture = textureStore.get(key);
        } else {
            BufferedImage premadeTexture = new BufferedImage(textwidth, textheight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = premadeTexture.createGraphics();
            graphics.setFont(stdFont);
            graphics.setColor(color);
            if (antialiased) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            }
            graphics.drawString(text, 1, textheight - textheight / 4);
            graphics.dispose();
            textTexture = new Texture(textwidth, textheight, true);
            textTexture.fillFromBufferedImageFlip(premadeTexture);
            textTexture.update();
            if (!memorysaving)
                textureStore.put(key, textTexture);
        }
        lastRenderedWidth = textwidth;
        lastRenderedHeight = textheight;
        return 0;
    }

    @Override
    public int prepareForRendering() {
        if (textTexture != null) {
            textTexture.updateTexture();
            textTexture.bind(GL11.GL_ONE_MINUS_SRC_ALPHA);
        } else
            return 1;
        return 0;
    }

    @Override
    public int drawString(int x, int y, String text) {
        return drawString(x, y, text, Color.white);
    }

    @Override
    public int drawString(int x, int y, String text, Color color) {
        generateString(text, color);
        drawOnScreen(x, y);
        return 0;
    }

    @Override
    public int drawStringWithShadow(int x, int y, String text) {
        return drawStringWithShadow(x, y, text, Color.white);
    }

    @Override
    public int drawStringWithShadow(int x, int y, String text, Color color) {
        generateString(text, Color.black);
        drawOnScreen(x + shadowSize, y + shadowSize);
        generateString(text, color);
        drawOnScreen(x, y);
        return 0;
    }

    @Override
    public int drawCenteredString(int x, int y, String text) {
        generateString(text, Color.white);
        drawOnScreen(x - getLastRenderedWidth() / 2, y - getLastRenderedHeight() / 2);
        return 0;
    }

    @Override
    public int drawCenteredString(int x, int y, String text, Color color) {
        generateString(text, color);
        drawOnScreen(x - getLastRenderedWidth() / 2, y - getLastRenderedHeight() / 2);
        return 0;
    }

    @Override
    public int drawCenteredStringWithShadow(int x, int y, String text) {
        return drawCenteredStringWithShadow(x, y, text, Color.white);
    }

    @Override
    public int drawCenteredStringWithShadow(int x, int y, String text, Color color) {
        drawCenteredString(x + shadowSize, y + shadowSize, text, Color.black);
        drawCenteredString(x, y, text, color);
        return 0;
    }

    @Override
    public int drawOnScreen(int x, int y) {
        prepareAndPushMatrix(); //GL PART
        prepareForRendering(); //BINDING
        renderAndPopMatrix(x, y, getLastRenderedWidth(), getLastRenderedHeight()); //GL PART
        return 0;
    }

    @Override
    public int getStringWidth(String text) {
        FontMetrics fontMetrics = new Canvas().getFontMetrics(stdFont);
        return fontMetrics.charsWidth(text.toCharArray(), 0, text.length()) + 1;
    }

    @Override
    public int getStringHeight(String text) {
        FontMetrics fontMetrics = new Canvas().getFontMetrics(stdFont);
        return fontMetrics.getHeight();
    }

    @Override
    public int getLastRenderedWidth() {
        return lastRenderedWidth;
    }

    @Override
    public int getLastRenderedHeight() {
        return lastRenderedHeight;
    }

    public static class Modifiers {
        public static byte NONE = 0b00000000;
        public static byte BOLD = 0b00000001;
        public static byte ITALICS = 0b00000010;
        public static byte UNDERLINED = 0b00000100;
        public static byte STRIKED = 0b00001000;
        public static byte MOVING = 0b00010000;
        public static byte ANTIALIASED = 0b00100000;
        public static byte MEMORYSAVING = 0b01000000;
    }

}
