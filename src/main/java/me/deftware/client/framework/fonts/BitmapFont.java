package me.deftware.client.framework.fonts;

import me.deftware.client.framework.utils.TexUtil;
import me.deftware.client.framework.utils.Texture;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class BitmapFont implements EMCFont{

    protected int lastRenderedWidth;
    protected int lastRenderedHeight;

    protected String fontName;
    protected int fontSize, shadowSize = 1;
    protected boolean bold;
    protected boolean italics;
    protected boolean underlined;
    protected boolean striked;
    protected boolean moving;
    protected boolean antialiased;
    protected boolean memorysaving;
    protected Font stdFont;

    protected HashMap<Character, Texture> bitmapStore = new HashMap<>();

    public BitmapFont(@Nonnull String fontName, int fontSize, int modifiers) {
        this.fontName = fontName;
        this.fontSize = fontSize;

        this.bold = ((modifiers & 1) != 0);
        this.italics = ((modifiers & 2) != 0);
        this.underlined = ((modifiers & 4) != 0);
        this.striked = ((modifiers & 8) != 0);
        this.moving = ((modifiers & 16) != 0);
        this.antialiased = ((modifiers & 32) != 0);
        this.memorysaving = ((modifiers & 64) != 0);

        prepareStandardFont();

        lastRenderedWidth = 0;
        lastRenderedHeight = 0;
    }

    protected void prepareStandardFont(){
        if (!bold && !italics) {
            this.stdFont = new Font(fontName, Font.PLAIN, fontSize);
        } else {
            if (bold && italics) {
                this.stdFont = new Font(fontName, Font.BOLD | java.awt.Font.ITALIC, fontSize);
            } else if (!bold) { //One of them must be false by now so we have to check only one
                this.stdFont = new Font(fontName, Font.ITALIC, fontSize);
            } else {
                this.stdFont = new Font(fontName, Font.BOLD, fontSize);
            }
        }
    }

    @Override
    public int initialize(Color color, String extras) {
        if(extras == null)
            extras= "";
        char[] additionalCharacters = extras.toCharArray();
        //Generate the font bitmaps

        Texture bitmapTexture;
        //Lowercase alphabet
        for (char lowercaseAlphabet = 'a'; lowercaseAlphabet <= 'z'; lowercaseAlphabet++) {
            String letterBuffer = String.valueOf(lowercaseAlphabet);
            int textwidth = getStringWidth(letterBuffer);
            int textheight = getStringHeight(letterBuffer);

            BufferedImage characterTexture = new BufferedImage(textwidth,textheight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = characterTexture.createGraphics();
            graphics.setFont(stdFont);
            graphics.setColor(color);
            if (antialiased) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            }
            graphics.drawString(letterBuffer, 1, textheight - textheight / 4);
            graphics.dispose();

            bitmapTexture = new Texture(textwidth, textheight, true);
            bitmapTexture.fillFromBufferedImageFlip(characterTexture);
            bitmapTexture.update();
            bitmapStore.put(lowercaseAlphabet, bitmapTexture);
        }

        //Uppercase alphabet
        for (char uppercaseAlphabet = 'A'; uppercaseAlphabet <= 'Z'; uppercaseAlphabet++) {
            String letterBuffer = String.valueOf(uppercaseAlphabet);
            int textwidth = getStringWidth(letterBuffer);
            int textheight = getStringHeight(letterBuffer);

            BufferedImage characterTexture = new BufferedImage(textwidth,textheight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = characterTexture.createGraphics();
            graphics.setFont(stdFont);
            graphics.setColor(color);
            if (antialiased) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            }
            graphics.drawString(letterBuffer, 1, textheight - textheight / 4);
            graphics.dispose();

            bitmapTexture = new Texture(textwidth, textheight, true);
            bitmapTexture.fillFromBufferedImageFlip(characterTexture);
            bitmapTexture.update();
            bitmapStore.put(uppercaseAlphabet, bitmapTexture);
        }

        //Numbers
        for (char numeric = 48; numeric <= 57; numeric++) { //0 - 9 in ASCII
            String letterBuffer = String.valueOf(numeric);
            int textwidth = getStringWidth(letterBuffer);
            int textheight = getStringHeight(letterBuffer);

            BufferedImage characterTexture = new BufferedImage(textwidth,textheight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = characterTexture.createGraphics();
            graphics.setFont(stdFont);
            graphics.setColor(color);
            if (antialiased) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            }
            graphics.drawString(letterBuffer, 1, textheight - textheight / 4);
            graphics.dispose();

            bitmapTexture = new Texture(textwidth, textheight, true);
            bitmapTexture.fillFromBufferedImageFlip(characterTexture);
            bitmapTexture.update();
            bitmapStore.put(numeric, bitmapTexture);
        }

        char specialCharacters[] = {'!', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
                ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~'};

        if(additionalCharacters.length > 0)
            specialCharacters = ArrayUtils.addAll(specialCharacters, additionalCharacters);

        //Additional and special characters
        for (int additional = 0; additional < specialCharacters.length; additional++) { //0 - 9 in ASCII
            String letterBuffer = String.valueOf(specialCharacters[additional]);
            int textwidth = getStringWidth(letterBuffer);
            int textheight = getStringHeight(letterBuffer);

            BufferedImage characterTexture = new BufferedImage(textwidth,textheight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = characterTexture.createGraphics();
            graphics.setFont(stdFont);
            graphics.setColor(color);
            if (antialiased) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            }
            graphics.drawString(letterBuffer, 1, textheight - textheight / 4);
            graphics.dispose();

            bitmapTexture = new Texture(textwidth, textheight, true);
            bitmapTexture.fillFromBufferedImageFlip(characterTexture);
            bitmapTexture.update();
            bitmapStore.put(specialCharacters[additional], bitmapTexture);
        }

        System.out.println(specialCharacters);
        return 0;
    }

    /**
     * Unimplemented in BitmapFont
     * @param text
     * @param color
     * @return 0
     */
    @Override
    public int generateString(String text, Color color) {
        return 0;
    }

    @Override
    public int drawString(int x, int y, String text) {
        drawString(x, y, text, null);
        return 0;
    }

    @Override
    public int drawString(int x, int y, String text, Color color) {
        char[] buffer = text.toCharArray();
        int offset = 0;
        for(int character = 0; character < buffer.length; character++){
            if(buffer[character] == ' ') {
                offset += getStringWidth(" ");
                continue;
            }
            else if(!bitmapStore.containsKey(buffer[character])) {
                buffer[character] = '?';
            }
            TexUtil.prepareAndPushMatrix(); //GL PART
            if(color != null)
                GL11.glColor4ub((byte) color.getRed(),(byte) color.getGreen(),(byte) color.getBlue(), (byte) color.getAlpha());
            Texture texture = bitmapStore.get(buffer[character]);
            texture.updateTexture();
            texture.bind(GL11.GL_ONE_MINUS_SRC_ALPHA);
            int width = texture.getWidth();
            int height = texture.getHeight();
            TexUtil.renderAndPopMatrix(x + offset, y, width, height); //GL PART
            offset += width;
            //System.out.println("Drawing " + buffer[character] + " at " + width + ":" + height);
        }
        lastRenderedWidth = offset;
        return 0;
    }

    @Override
    public int drawStringWithShadow(int x, int y, String text) {
        drawStringWithShadow(x, y, text, Color.white);
        return 0;
    }

    @Override
    public int drawStringWithShadow(int x, int y, String text, Color color) {
        drawString(x + shadowSize, y + shadowSize, text, Color.black);
        drawString(x, y, text, color);
        return 0;
    }

    @Override
    public int drawCenteredString(int x, int y, String text) {
        drawString(x - getStringWidth(text), y - getStringHeight(text), text);
        return 0;
    }

    @Override
    public int drawCenteredString(int x, int y, String text, Color color) {
        drawString(x - getStringWidth(text), y - getStringHeight(text), text, color);
        return 0;
    }

    @Override
    public int drawCenteredStringWithShadow(int x, int y, String text) {
        drawStringWithShadow(x + shadowSize, y + shadowSize, text, Color.black);
        drawStringWithShadow(x, y, text);
        return 0;
    }

    @Override
    public int drawCenteredStringWithShadow(int x, int y, String text, Color color) {
        drawStringWithShadow(x + shadowSize, y + shadowSize, text, Color.black);
        drawStringWithShadow(x, y, text, color);
        return 0;
    }

    @Override
    public int drawOnScreen(int x, int y) {
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
    public int getLastRenderedHeight() {
        return lastRenderedHeight;
    }

    @Override
    public int getLastRenderedWidth() {
        return lastRenderedWidth;
    }

    @Override
    public void clearCache() {
        for (Character key : bitmapStore.keySet()) {
            bitmapStore.get(key).destroy();
        }
        bitmapStore.clear();
    }

    @Override
    public void destroy() {
        clearCache();
    }

    @Override
    public String getFontName() {
        return fontName;
    }

    @Override
    public void setFontName(String fontName) {
        this.fontName = fontName;
        prepareStandardFont();
    }

    @Override
    public int getFontSize() {
        return fontSize;
    }

    @Override
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        prepareStandardFont();
    }

    @Override
    public int getShadowSize() {
        return shadowSize;
    }

    @Override
    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
    }

    @Override
    public boolean isBold() {
        return bold;
    }

    @Override
    public void setBold(boolean bold) {
        this.bold = bold;
    }

    @Override
    public boolean isItalics() {
        return italics;
    }

    @Override
    public void setItalics(boolean italics) {
        this.italics = italics;
    }

    @Override
    public boolean isUnderlined() {
        return underlined;
    }

    @Override
    public void setUnderlined(boolean underlined) {
        this.underlined = underlined;
    }

    @Override
    public boolean isStriked() {
        return striked;
    }

    @Override
    public void setStriked(boolean striked) {
        this.striked = striked;
    }

    @Override
    public boolean isMoving() {
        return moving;
    }

    @Override
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    @Override
    public boolean isAntialiased() {
        return antialiased;
    }

    @Override
    public void setAntialiased(boolean antialiased) {
        this.antialiased = antialiased;
    }

    @Override
    public boolean isMemorysaving() {
        return memorysaving;
    }

    @Override
    public void setMemorysaving(boolean memorysaving) {
        this.memorysaving = memorysaving;
    }
}
