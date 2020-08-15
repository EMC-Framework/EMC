package me.deftware.client.framework.wrappers.render;

import net.minecraft.client.renderer.WorldRenderer;

public class IVertexBuffer {

    private WorldRenderer vertexbuffer;

    public IVertexBuffer(ITessellator tessellator) {
        vertexbuffer = tessellator.getTessellator().getWorldRenderer();
    }

    public IVertexBuffer(WorldRenderer buffer) {
        vertexbuffer = buffer;
    }

    public void begin(int glMode, IDefaultVertexFormats.IVertexFormat format) {
        vertexbuffer.begin(glMode, format.getFormat());
    }

    public IVertexBuffer pos(double x, double y, double z) {
        vertexbuffer.pos(x, y, z);
        return this;
    }

    @Deprecated
    public IVertexBuffer tex(float u, float v) {
        vertexbuffer.tex(u, v);
        return this;
    }

    public IVertexBuffer color(float red, float green, float blue, float alpha) {
        vertexbuffer.color(red, green, blue, alpha);
        return this;
    }

    public void endVertex() {
        vertexbuffer.endVertex();
    }

}
