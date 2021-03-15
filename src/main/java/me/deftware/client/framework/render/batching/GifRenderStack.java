package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.render.texture.GraphicsUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Deftware
 */
public class GifRenderStack extends RenderStack<GifRenderStack> {

    private final static Logger logger = LogManager.getLogger("GifRenderer");

    private boolean isAvailable = false;

    /**
     * The size of the texture
     */
    private int width = 0, height = 0;

    /**
     * Frame index to frame data
     */
    private final Map<Integer, Frame> frames = new HashMap<>();

    private int glId = -1;

    private int frameIndex = 0;

    private long lastFrame = System.currentTimeMillis();

    public GifRenderStack(GifProvider gif) {
        try {
            // Initialize texture atlas
            logger.debug("Loading gif, with {} frames", gif.getFrameCount());

            // Get size
            this.width = gif.getWidth();
            this.height = gif.getHeight();

            // Allocate image
            glId = GraphicsUtil.loadTextureFromBufferedImage(gif.getFrame(0));
            logger.debug("Allocated texture with id {}", glId);

            // Loop all frames after the first frame
            for (int i = 0; i < gif.getFrameCount(); i++) {
                BufferedImage image = gif.getFrame(i);
                if (image.getWidth() != width || image.getHeight() != height)
                    throw new IOException("Target frame is not the same size as the first one");
                frames.put(i, new Frame(image.getWidth(), image.getHeight(), gif.getDelay(i), GraphicsUtil.getImageBuffer(image)));
            }

            logger.debug("Successfully loaded gif");
            isAvailable = true;
        } catch (Throwable ex) {
            logger.error("Failed to load gif", ex);
        }
    }

    /**
     * Uploads the next frame in the gif
     */
    private void next() {
        if (lastFrame < System.currentTimeMillis()) {
            if (frameIndex >= frames.size())
                frameIndex = 0;
            Frame frame = frames.get(this.frameIndex);
            // Update texture
            frame.upload();
            this.frameIndex++;
            // Update delay
            lastFrame = System.currentTimeMillis() + frame.getDelay();
        }
    }

    @Override
    public GifRenderStack begin() {
        if (!isAvailable)
            throw new RuntimeException("Cannot render unavailable gif!");
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(glId);
        return begin(GL11.GL_QUADS);
    }

    @Override
    public void end() {
        super.end();
        GlStateManager.disableTexture2D();
    }

    @Override
    protected VertexFormat getFormat() {
        return DefaultVertexFormats.POSITION_TEX;
    }

    @Override
    protected WorldRenderer vertex(double x, double y, double z) {
        return builder.pos(x,y, z);
    }

    public GifRenderStack draw(int x0, int y0, int x1, int y1) {
        // Draw frame
        int u0 = 0, u1 = 1, v0 = 0, v1 = 1;
        vertex(x0, y1, 0).tex(u0, v1).endVertex();
        vertex(x1, y1, 0).tex(u1, v1).endVertex();
        vertex(x1, y0, 0).tex(u1, v0).endVertex();
        vertex(x0, y0, 0).tex(u0, v0).endVertex();
        // Update texture
        next();
        return this;
    }

    public void destroy() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);
        GL11.glDeleteTextures(glId);
        glId = -1;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<Integer, Frame> getFrames() {
        return frames;
    }

    public int getGlId() {
        return glId;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    private static class Frame {

        /**
         * Frame size
         */
        private final int width, height;

        /**
         * The delay in ms until the next frame can be drawn
         */
        private final int delay;

        /**
         * A buffer that contains the pixel data of a frame
         */
        private final ByteBuffer buffer;

        /**
         * Uploads the frame to the gpu
         */
        public void upload() {
            // Minecraft modifies these
            GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0);
            GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
            GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
            // Upload texture to gpu
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        }

        public Frame(int width, int height, int delay, ByteBuffer buffer) {
            this.width = width;
            this.height = height;
            this.delay = delay;
            this.buffer = buffer;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getDelay() {
            return delay;
        }

        public ByteBuffer getBuffer() {
            return buffer;
        }

    }

    public interface GifProvider {

        /**
         * @return The total amount of frames
         */
        int getFrameCount();

        /**
         * @return The image for a frame
         */
        BufferedImage getFrame(int index);

        /**
         * @return The delay before showing the next frame, in ms
         */
        int getDelay(int index);

        /**
         * @return The width of the gif
         */
        int getWidth();

        /**
         * @return The height if the gif
         */
        int getHeight();

    }

}
