package me.deftware.client.framework.render.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.GpuTexture;
import lombok.Getter;
import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.render.batching.RenderStack;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.util.ResourceUtils;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

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

    @Getter
    protected GpuTexture glId;

    protected DynamicTexture dynamicTexture;

    @Getter
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
        if (RenderSystem.tryGetDevice() != null) {
            this.upload(getImageBuffer(image), false);
        }
    }

    public GlTexture draw(GLX context, int x, int y, int width, int height) {
        return draw(context, x, y, width, height, 0, 0, width, height);
    }

    public GlTexture draw(GLX context, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight) {
        drawTexture(context, x, y, width, height, u, v, textureWidth, textureHeight);
        return this;
    }

    public static void drawTexture(GLX context, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight) {
        drawTexture(context, x, x + width, y, y + height, 0, width, height, u, v, textureWidth, textureHeight);
    }

    public GlTexture bind() {
        boundGlTexture = this;
        boundMcTexture = dynamicTexture;
        bindTexture(glId);
        return this;
    }

    public boolean isReady() {
        return glId != null;
    }

    public void unbind() {
        boundTexture = null;
        boundGlTexture = null;
        boundMcTexture = null;
    }

    public void upload(BufferedImage image) {
        this.textureWidth = image.getWidth();
        this.textureHeight = image.getHeight();
        upload(getImageBuffer(image), dynamicTexture != null);
    }

    /**
     * If the texture is to be replaced, the size MUST match the previous size
     */
    public void upload(ByteBuffer buffer, boolean replace) {
        if (textureWidth <= 0 || textureHeight <= 0 || RenderSystem.tryGetDevice() == null) {
            return;
        }
        NativeImage img = new NativeImage(textureWidth, textureHeight, false);
        ByteBuffer view = buffer.duplicate();
        view.rewind();
        for (int y = 0; y < textureHeight; y++) {
            for (int x = 0; x < textureWidth; x++) {
                int r = view.get() & 0xFF;
                int g = view.get() & 0xFF;
                int b = view.get() & 0xFF;
                int a = view.get() & 0xFF;
                img.setPixel(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        if (dynamicTexture == null || !replace) {
            if (dynamicTexture != null) {
                dynamicTexture.close();
            }
            dynamicTexture = new DynamicTexture(() -> "emc_tex", img);
        } else {
            dynamicTexture.setPixels(img);
            dynamicTexture.upload();
        }
        this.glId = dynamicTexture.getTexture();
        if (boundGlTexture == this) {
            boundMcTexture = dynamicTexture;
            boundTexture = this.glId;
        }
    }

    public void destroy() {
        unbind();
        if (dynamicTexture != null) {
            dynamicTexture.close();
            dynamicTexture = null;
        }
        glId = null;
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

    private static GpuTexture boundTexture;
    private static GlTexture boundGlTexture;
    private static AbstractTexture boundMcTexture;

    public static void bindTexture(GpuTexture texture) {
        boundTexture = texture;
    }

    public static void bindTexture(MinecraftIdentifier id) {
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(id);
        boundMcTexture = texture;
        if (texture != null) {
            bindTexture(texture.getTexture());
        }
    }

    public static AbstractTexture getBoundAbstractTexture() {
        return boundMcTexture;
    }

    private static void drawTexture(GLX context, int x0, int x1, int y0, int y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
        drawTexturedQuad(context, x0, x1, y0, y1, z, u / textureWidth, (u + regionWidth) / textureWidth, v / textureHeight, (v + regionHeight) / textureHeight);
    }

    private static void drawTexturedQuad(GLX context, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
        if (context != null && context.getContext() != null && boundMcTexture != null && boundMcTexture.getTextureView() != null) {
            var gfx = context.getContext();
            ScreenRectangle clip = RenderStack.getActiveScissor(gfx);
            if (clip != null && (clip.width() <= 0 || clip.height() <= 0)) {
                return;
            }
            boolean pushedScissor = false;
            if (clip != null) {
                gfx.enableScissor(clip.left(), clip.top(), clip.right(), clip.bottom());
                pushedScissor = true;
            }
            boolean custom = RenderStack.isInCustomMatrix();
            if (custom) {
                float inv = 1f / (float) Minecraft.getInstance().getWindow().getGuiScale();
                gfx.pose().pushMatrix();
                gfx.pose().scale(inv, inv);
            }
            gfx.blit(boundMcTexture.getTextureView(), boundMcTexture.getSampler(), x0, y0, x1, y1, u0, u1, v0, v1);
            if (custom) {
                gfx.pose().popMatrix();
            }
            if (pushedScissor) {
                gfx.disableScissor();
            }
        }
    }

}
