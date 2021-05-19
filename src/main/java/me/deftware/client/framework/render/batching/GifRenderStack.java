package me.deftware.client.framework.render.batching;


import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import me.deftware.client.framework.render.texture.GlTexture;
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

    private GlTexture texture;

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
            texture = new GlTexture(gif.getFrame(0));
            logger.debug("Allocated texture with id {}", texture.getGlId());

            // Loop all frames after the first frame
            for (int i = 0; i < gif.getFrameCount(); i++) {
                BufferedImage image = gif.getFrame(i);
                if (image.getWidth() != width || image.getHeight() != height)
                    throw new IOException("Target frame is not the same size as the first one");
                frames.put(i, new Frame(image.getWidth(), image.getHeight(), gif.getDelay(i), GlTexture.getImageBuffer(image)));
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
    private void nextFrame() {
        if (lastFrame < System.currentTimeMillis()) {
            if (frameIndex >= frames.size())
                frameIndex = 0;
            Frame frame = frames.get(this.frameIndex);
            // Update texture
            frame.upload(texture);
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
        texture.bind();
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
    public VertexConstructor vertex(double x, double y, double z) {
        builder.pos(x,y, z);
        return this;
    }

    public GifRenderStack draw(int x0, int y0, int x1, int y1) {
        // Draw frame
        int u0 = 0, u1 = 1, v0 = 0, v1 = 1;
        vertex(x0, y1, 0).texture(u0, v1).next();
        vertex(x1, y1, 0).texture(u1, v1).next();
        vertex(x1, y0, 0).texture(u1, v0).next();
        vertex(x0, y0, 0).texture(u0, v0).next();
        // Update texture
        nextFrame();
        return this;
    }

    public void destroy() {
        texture.destroy();
        texture = null;
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

    public GlTexture getTexture() {
        return texture;
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
        public void upload(GlTexture texture) {
            texture.upload(buffer, true);
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
