package me.deftware.client.framework.fonts.legacy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.registry.font.TTFRegistry;
import me.deftware.client.framework.render.batching.RenderStack;
import me.deftware.client.framework.render.texture.GraphicsUtil;
import me.deftware.client.framework.util.path.OSUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Legacy bitmap font used for now
 *
 * @author Deftware, Ananas
 */
public class LegacyBitmapFont {

    @Getter
    public final Map<Character, CharData> characterMap = new HashMap<>();

    protected Font stdFont;
    public String fontName;
    protected int fontSize;
    public boolean scaled;

    @Setter
    @Getter
    public int shadow = 1;

    @Getter
    public int glId = -1;

    @Getter
    public int textureWidth, textureHeight;

    private FontMetrics metrics;

    public LegacyBitmapFont(String fontName, int fontSize, boolean scaled) {
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.scaled = scaled;
        RenderStack.scaleChangeCallback.add(() -> {
            setupFont();
            initialize();
        });
        setupFont();
    }

    public LegacyBitmapFont(String fontName, int fontSize) {
        this(fontName, fontSize, true);
    }

    public void setupFont() {
        Font fallback = new Font(this.fontName, Font.PLAIN, this.fontSize);
        if (OSUtils.isWindows()) {
            try {
                File font = Paths.get(System.getenv("LOCALAPPDATA"), "Microsoft", "Windows", "Fonts", fontName + ".ttf").toFile();
                if (font.exists()) {
                    fallback = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(font.getAbsolutePath()));
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        this.stdFont = TTFRegistry.getFont(this.fontName, fallback)
                .deriveFont(Font.PLAIN, fontSize * (scaled ? RenderStack.getScale() : 1f));
        this.metrics = new Canvas().getFontMetrics(this.stdFont);
    }

    public void initialize() {
        destroy();
        List<Character> characters = new ArrayList<>();
        // Lowercase alphabet
        for (char lowercaseAlphabet = 'a'; lowercaseAlphabet <= 'z'; lowercaseAlphabet++) {
            characters.add(lowercaseAlphabet);
        }
        // Uppercase alphabet
        for (char uppercaseAlphabet = 'A'; uppercaseAlphabet <= 'Z'; uppercaseAlphabet++) {
            characters.add(uppercaseAlphabet);
        }
        // Numbers
        for (char numeric = 48; numeric <= 57; numeric++) { // 0 - 9 in ASCII
            characters.add(numeric);
        }
        // Additional and special characters
        char[] specialCharacters = {'!', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
                ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~', '"'};
        for (char specialCharacter : specialCharacters) {
            characters.add(specialCharacter);
        }

        glId = characterGenerate(characters);
    }

    protected int characterGenerate(List<Character> characters) {
        // Calculate size of texture
        // fixedWidth must be more than the width of the widest character
        int width = 0, height = 0, fixedWidth = getStringWidth('W') * 2;
        List<Consumer<Graphics2D>> generation = new ArrayList<>();
        for (char character : characters) {
            String letterBuffer = String.valueOf(character);
            int textWidth = getStringWidth(character), textHeight = getStringHeight();
            if (height < textHeight)
                height = textHeight;
            width += fixedWidth;
            int xOffset = width;
            generation.add(graphics -> {
                // Draw character
                graphics.drawString(letterBuffer, xOffset, textHeight - textHeight / 4);
                // Generate data
                CharData data = new CharData(textWidth, textHeight, xOffset, 0);
                characterMap.put(character, data);
            });
        }

        // Setup texture
        BufferedImage characterTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = characterTexture.createGraphics();
        graphics.setFont(stdFont);
        graphics.setColor(Color.white);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        // Draw characters
        generation.forEach(c -> c.accept(graphics));

        // Dispose graphics
        graphics.dispose();

        this.textureWidth = characterTexture.getWidth();
        this.textureHeight = characterTexture.getHeight();

        DataBuffer dataBuffer = characterTexture.getData().getDataBuffer();

        // Each bank element in the data buffer is a 32-bit integer
        long sizeBytes = ((long) dataBuffer.getSize()) * 4L;
        long sizeMB = sizeBytes / (1024L * 1024L);

        Bootstrap.logger.info("Font atlas {}x{}, {} megabytes", textureWidth, textureHeight, sizeMB);

        // Upload to gpu
        return GraphicsUtil.loadTextureFromBufferedImage(characterTexture);
    }

    public int getStringWidth(char... chars) {
        return metrics.charsWidth(chars, 0, chars.length);
    }

    public int getStringWidth(String text) {
        return getStringWidth(text.toCharArray());
    }

    public int getStringHeight() {
        return metrics.getHeight();
    }

    public void destroy() {
        if (glId != -1) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL11.glDeleteTextures(glId);
            characterMap.clear();
            glId = -1;
        }
    }

    @Data
    @AllArgsConstructor
    public static class CharData {

        /**
         * The size of the character
         */
        private int width, height;

        /**
         * Texture offsets
         */
        private int u, v;

    }

}
