package me.deftware.client.framework.render.shader;

import net.minecraft.client.MinecraftClient;

/**
 * @author Deftware
 */
public class Framebuffer {

    /**
     * The main Minecraft framebuffer
     */
    public static final Framebuffer Main = new Framebuffer(MinecraftClient.getInstance().getFramebuffer());

    private final net.minecraft.client.gl.Framebuffer buffer;

    public Framebuffer(net.minecraft.client.gl.Framebuffer buffer) {
        this.buffer = buffer;
    }

    public void clear() {
        buffer.clear();
    }

    public void bind(boolean setViewport) {
        buffer.beginWrite(setViewport);
    }

    public void draw(int width, int height, boolean disableBlend) {
        buffer.draw(width, height);
    }

    public void resize(int width, int height) {
        buffer.resize(width, height);
    }

    public void close() {
        buffer.delete();
    }

    public void copyDepth(Framebuffer buffer) {
        this.buffer.copyDepthFrom(buffer.getMinecraftBuffer());
    }

    public net.minecraft.client.gl.Framebuffer getMinecraftBuffer() {
        return buffer;
    }

}
