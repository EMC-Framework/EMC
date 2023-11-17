package me.deftware.client.framework.render.shader;

import net.minecraft.client.Minecraft;

/**
 * @author Deftware
 */
public class Framebuffer {

    /**
     * The main Minecraft framebuffer
     */
    public static final Framebuffer Main = new Framebuffer(Minecraft.getInstance().getFramebuffer());

    private final net.minecraft.client.shader.Framebuffer buffer;

    public Framebuffer(net.minecraft.client.shader.Framebuffer buffer) {
        this.buffer = buffer;
    }

    public void clear() {
        buffer.framebufferClear();
    }

    public void bind(boolean setViewport) {
        buffer.bindFramebuffer(setViewport);
    }

    public void draw(int width, int height, boolean disableBlend) {
        buffer.framebufferRenderExt(width, height, disableBlend);
    }

    public void resize(int width, int height) {
        buffer.createBindFramebuffer(width, height);
    }

    public void close() {
        buffer.deleteFramebuffer();
    }

    public void copyDepth(Framebuffer buffer) {
        // this.buffer.copyDepthFrom(buffer.getMinecraftBuffer());
    }

    public net.minecraft.client.shader.Framebuffer getMinecraftBuffer() {
        return buffer;
    }

}
