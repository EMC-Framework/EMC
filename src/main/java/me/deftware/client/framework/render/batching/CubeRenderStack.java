package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;

/**
 * @author Deftware
 */
public class CubeRenderStack extends RenderStack<CubeRenderStack> {

	public CubeRenderStack() {
		customMatrix = false;
	}

	@Override
	public CubeRenderStack begin() {
		return this; /* Not used in this stack */
	}

	public CubeRenderStack ESPBox(BoundingBox box) {
		if (box == null) return this;
		drawColorBox(box.getOffsetMinecraftBox(-Minecraft.getCamera().getRenderPosX(), -Minecraft.getCamera().getRenderPosY(), -Minecraft.getCamera().getRenderPosZ()));
		return this;
	}

	public CubeRenderStack emptyESPBox(BoundingBox box) {
		drawSelectionBoundingBox(box.getOffsetMinecraftBox(-Minecraft.getCamera().getRenderPosX(), -Minecraft.getCamera().getRenderPosY(), -Minecraft.getCamera().getRenderPosZ()));
		return this;
	}

	private void drawColorBox(AxisAlignedBB box) {
		Tessellator ts = Tessellator.getInstance();
		BufferBuilder vb = ts.getBufferBuilder();
		vb.begin(7, VertexFormats.POSITION_UV);
		vb.vertex(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		ts.draw();
		vb.begin(7, VertexFormats.POSITION_UV);
		vb.vertex(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		ts.draw();
		vb.begin(7, VertexFormats.POSITION_UV);
		vb.vertex(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		ts.draw();
		vb.begin(7, VertexFormats.POSITION_UV);
		vb.vertex(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		ts.draw();
		vb.begin(7, VertexFormats.POSITION_UV);
		vb.vertex(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		ts.draw();
		vb.begin(7, VertexFormats.POSITION_UV);
		vb.vertex(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).next();
		vb.vertex(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).next();
		ts.draw();
	}

	private void drawSelectionBoundingBox(AxisAlignedBB box) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBufferBuilder();
		vertexbuffer.begin(3, VertexFormats.POSITION);
		vertexbuffer.vertex(box.minX, box.minY, box.minZ).next();
		vertexbuffer.vertex(box.maxX, box.minY, box.minZ).next();
		vertexbuffer.vertex(box.maxX, box.minY, box.maxZ).next();
		vertexbuffer.vertex(box.minX, box.minY, box.maxZ).next();
		vertexbuffer.vertex(box.minX, box.minY, box.minZ).next();
		tessellator.draw();
		vertexbuffer.begin(3, VertexFormats.POSITION);
		vertexbuffer.vertex(box.minX, box.maxY, box.minZ).next();
		vertexbuffer.vertex(box.maxX, box.maxY, box.minZ).next();
		vertexbuffer.vertex(box.maxX, box.maxY, box.maxZ).next();
		vertexbuffer.vertex(box.minX, box.maxY, box.maxZ).next();
		vertexbuffer.vertex(box.minX, box.maxY, box.minZ).next();
		tessellator.draw();
		vertexbuffer.begin(1, VertexFormats.POSITION);
		vertexbuffer.vertex(box.minX, box.minY, box.minZ).next();
		vertexbuffer.vertex(box.minX, box.maxY, box.minZ).next();
		vertexbuffer.vertex(box.maxX, box.minY, box.minZ).next();
		vertexbuffer.vertex(box.maxX, box.maxY, box.minZ).next();
		vertexbuffer.vertex(box.maxX, box.minY, box.maxZ).next();
		vertexbuffer.vertex(box.maxX, box.maxY, box.maxZ).next();
		vertexbuffer.vertex(box.minX, box.minY, box.maxZ).next();
		vertexbuffer.vertex(box.minX, box.maxY, box.maxZ).next();
		tessellator.draw();
	}

}
