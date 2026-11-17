package me.deftware.client.framework.render.shader;

import net.minecraft.client.Minecraft;

/**
 * @author Deftware
 */
public class Framebuffer {

    /**
     * The main Minecraft framebuffer
     */
    public static final Framebuffer Main = new Framebuffer(Minecraft.getInstance().gameRenderer != null ? Minecraft.getInstance().gameRenderer.mainRenderTarget() : null);

    private final com.mojang.blaze3d.pipeline.RenderTarget buffer;

    public Framebuffer(com.mojang.blaze3d.pipeline.RenderTarget buffer) {
        this.buffer = buffer;
    }

    public void clear() {
        //buffer.clear();
    }

    public void bind(boolean setViewport) {
        //buffer.beginWrite(setViewport);
    }

    public void draw(int width, int height, boolean disableBlend) {
       // buffer.draw(width, height);
    }

    public void resize(int width, int height) {
        buffer.resize(width, height);
    }

    public void close() {
        buffer.destroyBuffers();
    }

    public void copyDepth(Framebuffer buffer) {
        this.buffer.copyDepthFrom(buffer.getMinecraftBuffer());
    }

    public com.mojang.blaze3d.pipeline.RenderTarget getMinecraftBuffer() {
        return buffer;
    }

}
