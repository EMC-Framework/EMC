package me.deftware.client.framework.fonts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.render.texture.GlTexture;
import me.deftware.client.framework.util.path.OSUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Deftware, Ananas
 */
public class AtlasTextureFont {

    @Getter
    public final Map<String, CharData> characterMap = new HashMap<>();

    private static final Canvas CANVAS = new Canvas();
    private static final List<String> characters = new ArrayList<>();

    static {
        // https://en.wikipedia.org/wiki/List_of_Unicode_characters
        for (char numeric = 33; numeric <= 255; numeric++) {
            if (numeric == 160) // Non-breaking space
                continue;
            characters.add(Character.toString(numeric));
        }
    }

    protected float fontSize;

    @Setter
    @Getter
    public int shadow = 1;

    @Getter
    private GlTexture textureAtlas;

    @Getter
    public int textureWidth, textureHeight;

    protected Font stdFont;

    protected FontMetrics metrics;

    public AtlasTextureFont(Font font, float fontSize, boolean scaled) {
        setFont(font, fontSize);
    }

    public AtlasTextureFont(Font font, float fontSize) {
        this(font, fontSize, true);
    }

    public static Font getSystem(String name) {
        Font fallback = new Font(name, Font.PLAIN, 18);
        if (OSUtils.isWindows()) {
            try {
                File font = Paths.get(System.getenv("LOCALAPPDATA"), "Microsoft", "Windows", "Fonts", name + ".ttf").toFile();
                if (font.exists()) {
                    fallback = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(font.getAbsolutePath()));
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return fallback;
    }

    private Font derive(Font font, float size) {
        return font.deriveFont(Font.PLAIN, size);
    }

    public void setFont(Font font, float size) {
        fontSize = size;
        stdFont = derive(font, fontSize);
        metrics = CANVAS.getFontMetrics(stdFont);
    }

    public void initialize() {
        destroy();
        BufferedImage texture = genAtlas();
        ByteBuffer buffer = GlTexture.getImageBuffer(texture);
        setTexture(buffer, texture.getWidth(), texture.getHeight());
    }

    public void setTexture(ByteBuffer buffer, int width, int height) {
        textureWidth = width;
        textureHeight = height;
        if (textureAtlas == null) {
            textureAtlas = new GlTexture(buffer, GL11.GL_NEAREST, width, height);
            return;
        }
        boolean replace = textureAtlas.setDimensions(width, height);
        textureAtlas.bind();
        textureAtlas.upload(buffer, replace);
        textureAtlas.unbind();
    }

    public BufferedImage genAtlas() {
        int characterWidth = 200, maxPerRow = 30;
        // Calculate size of texture
        // fixedWidth must be more than the width of the widest character
        int xLocation = 0, height = metrics.getHeight();
        List<Consumer<Graphics2D>> generation = new ArrayList<>();
        for (int i = 0; i < characters.size(); i++) {
            if (i > 0 && i % maxPerRow == 0) {
                height += getStringHeight() + 10;
                xLocation = 0;
            }
            String character = characters.get(i);
            int textWidth = metrics.charsWidth(character.toCharArray(), 0, character.length()), textHeight = getStringHeight();
            int xOffset = xLocation, yOffset = height - metrics.getHeight();
            generation.add(graphics -> {
                // Draw character
                graphics.drawString(character, xOffset, yOffset + (textHeight - textHeight / 4));
                // Generate data
                CharData data = new CharData(textWidth, textHeight, xOffset, yOffset, character);
                characterMap.put(character, data);
            });
            xLocation += characterWidth;
        }

        // Setup texture
        BufferedImage characterTexture = new BufferedImage(characterWidth * maxPerRow, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = characterTexture.createGraphics();
        graphics.setFont(stdFont);
        graphics.setColor(Color.white);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        // Draw characters
        generation.forEach(c -> c.accept(graphics));

        // Dispose graphics
        graphics.dispose();

        try {
            ImageIO.write(characterTexture, "png", new File("./atlas.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return characterTexture;
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

    public void setCharacterMap(Map<String, CharData> map) {
        characterMap.clear();
        characterMap.putAll(map);
    }

    public void destroy() {
        if (textureAtlas != null) {
            textureAtlas.destroy();
            characterMap.clear();
            textureAtlas = null;
        }
    }

    public float getFontSize() {
        return fontSize;
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

        private String character;

    }

}
