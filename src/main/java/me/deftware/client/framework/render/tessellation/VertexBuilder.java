package me.deftware.client.framework.render.tessellation;

import net.minecraft.client.renderer.BufferBuilder;

/**
 * @author Deftware
 */
public class VertexBuilder {

	private final BufferBuilder builder;

	public VertexBuilder(BufferBuilder builder) {
		this.builder = builder;
	}

	public void begin(int glMode, VertexFormat format) {
		builder.begin(glMode, format.getMinecraftFormat());
	}

	public VertexBuilder pos(double x, double y, double z) {
		builder.pos(x, y, z);
		return this;
	}

	public VertexBuilder tex(float u, float v) {
		builder.tex(u, v);
		return this;
	}

	public VertexBuilder color(float red, float green, float blue, float alpha) {
		builder.color(red, green, blue, alpha);
		return this;
	}

	public void endVertex() {
		builder.endVertex();
	}

}
