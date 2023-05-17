package me.deftware.client.framework.render.texture;

import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.render.batching.RenderStack;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author Deftware
 */
public class GlTexture implements GuiScreen.BackgroundType {

    protected int glId;

    protected int textureWidth, textureHeight, scaling;

    public GlTexture(EMCMod mod, String asset) throws IOException {
        this(
                ResourceUtils.getStreamFromModResources(mod, asset)
        );
    }

    public GlTexture(File file) throws IOException {
        this(ImageIO.read(file));
    }

    public GlTexture(InputStream stream) throws IOException {
        this(ImageIO.read(stream));
    }

    public GlTexture(BufferedImage image) {
        this(image, GL11.GL_LINEAR);
    }

    public GlTexture() {
        // Intended for classes extending this class
    }

    public GlTexture(BufferedImage image, int scaling) {
        this.init(image, scaling);
    }

    protected void init(BufferedImage image, int scaling) {
        this.scaling = scaling;
        this.textureWidth = image.getWidth();
        this.textureHeight = image.getHeight();
        this.glId = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.glId);
        this.upload(getImageBuffer(image), false);
    }

    public GlTexture draw(GLX context, int x, int y, int width, int height) {
        return draw(context, x, y, width, height, 0, 0, width, height);
    }

    public GlTexture draw(GLX context, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight) {
        drawTexture(context, x, y, width, height, u, v, textureWidth, textureHeight);
        return this;
    }

    public static void drawTexture(GLX context, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight) {
        GlStateManager.enableBlend();
        GuiScreen.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
    }

    public GlTexture bind() {
        bindTexture(glId);
        return this;
    }

    public boolean isReady() {
        return glId != 0;
    }

    public void unbind() {
        bindTexture(0);
    }

    public void upload(BufferedImage image) {
        upload(getImageBuffer(image), true);
    }

    /**
     * If the texture is to be replaced, the size MUST match the previous size
     */
    public void upload(ByteBuffer buffer, boolean replace) {
        // Minecraft modifies these
        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
        // Clamping mode
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        // Scaling
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, scaling);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, scaling);
        // Upload
        if (replace)
            // Replace texture, without reallocating
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, textureWidth, textureHeight, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        else
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, textureWidth, textureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
    }

    public void destroy() {
        bind();
        GL11.glDeleteTextures(glId);
        glId = -1;
    }

    public int getGlId() {
        return glId;
    }

    public int getScaling() {
        return scaling;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    @Override
    public void renderBackground(GLX context, int mouseX, int mouseY, float delta, GuiScreen parent) {
        context.color(1, 1, 1, 1);
        int width = parent.getGuiScreenWidth(), height = parent.getGuiScreenHeight();
        if (RenderStack.isInCustomMatrix()) {
            width = GuiScreen.getDisplayWidth();
            height = GuiScreen.getDisplayHeight();
        }
        bind().draw(context, 0,0, width, height).unbind();
    }

    public static ByteBuffer getImageBuffer(BufferedImage image) {
        int width = image.getWidth(), height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); // 4 for RGBA, 3 for RGB
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));    // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));     // Green component
                buffer.put((byte) (pixel & 0xFF));            // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }
        buffer.flip();
        return buffer;
    }

    public static void bindTexture(int id) {
        GlStateManager.bindTexture(id);
    }

    public static void bindTexture(MinecraftIdentifier texture) {
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
    }

}
