package me.deftware.client.framework.render.tessellation;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

/**
 * @author Deftware
 */
public enum VertexFormat {

	POSITION_COLOR(DefaultVertexFormats.POSITION_COLOR),
	POSITION_TEX_COLOR(DefaultVertexFormats.POSITION_TEX_COLOR),
	POSITION(DefaultVertexFormats.POSITION);

	private final net.minecraft.client.renderer.vertex.VertexFormat format;

	VertexFormat(net.minecraft.client.renderer.vertex.VertexFormat format) {
		this.format = format;
	}

	public net.minecraft.client.renderer.vertex.VertexFormat getMinecraftFormat() {
		return format;
	}

}
