package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Box;

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

	private void drawColorBox(Box box) {
		Tessellator ts = Tessellator.getInstance();
		BufferBuilder vb = ts.getBuffer();
		vb.begin(7, VertexFormats.POSITION_TEXTURE);
		vb.vertex(box.x1, box.y1, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y2, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y2, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y2, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y1, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y2, box.z2).color(0, 0, 0, 0).next();
		ts.draw();
		vb.begin(7, VertexFormats.POSITION_TEXTURE);
		vb.vertex(box.x2, box.y2, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y2, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y1, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y2, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y1, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y2, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z2).color(0, 0, 0, 0).next();
		ts.draw();
		vb.begin(7, VertexFormats.POSITION_TEXTURE);
		vb.vertex(box.x1, box.y2, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y2, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y2, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y2, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y2, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y2, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y2, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y2, box.z1).color(0, 0, 0, 0).next();
		ts.draw();
		vb.begin(7, VertexFormats.POSITION_TEXTURE);
		vb.vertex(box.x1, box.y1, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y1, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y1, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y1, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z1).color(0, 0, 0, 0).next();
		ts.draw();
		vb.begin(7, VertexFormats.POSITION_TEXTURE);
		vb.vertex(box.x1, box.y1, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y2, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y1, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y2, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y2, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y2, box.z1).color(0, 0, 0, 0).next();
		ts.draw();
		vb.begin(7, VertexFormats.POSITION_TEXTURE);
		vb.vertex(box.x1, box.y2, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y1, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y2, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x1, box.y1, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y2, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z1).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y2, box.z2).color(0, 0, 0, 0).next();
		vb.vertex(box.x2, box.y1, box.z2).color(0, 0, 0, 0).next();
		ts.draw();
	}

	private void drawSelectionBoundingBox(Box box) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(3, VertexFormats.POSITION);
		vertexbuffer.vertex(box.x1, box.y1, box.z1).next();
		vertexbuffer.vertex(box.x2, box.y1, box.z1).next();
		vertexbuffer.vertex(box.x2, box.y1, box.z2).next();
		vertexbuffer.vertex(box.x1, box.y1, box.z2).next();
		vertexbuffer.vertex(box.x1, box.y1, box.z1).next();
		tessellator.draw();
		vertexbuffer.begin(3, VertexFormats.POSITION);
		vertexbuffer.vertex(box.x1, box.y2, box.z1).next();
		vertexbuffer.vertex(box.x2, box.y2, box.z1).next();
		vertexbuffer.vertex(box.x2, box.y2, box.z2).next();
		vertexbuffer.vertex(box.x1, box.y2, box.z2).next();
		vertexbuffer.vertex(box.x1, box.y2, box.z1).next();
		tessellator.draw();
		vertexbuffer.begin(1, VertexFormats.POSITION);
		vertexbuffer.vertex(box.x1, box.y1, box.z1).next();
		vertexbuffer.vertex(box.x1, box.y2, box.z1).next();
		vertexbuffer.vertex(box.x2, box.y1, box.z1).next();
		vertexbuffer.vertex(box.x2, box.y2, box.z1).next();
		vertexbuffer.vertex(box.x2, box.y1, box.z2).next();
		vertexbuffer.vertex(box.x2, box.y2, box.z2).next();
		vertexbuffer.vertex(box.x1, box.y1, box.z2).next();
		vertexbuffer.vertex(box.x1, box.y2, box.z2).next();
		tessellator.draw();
	}

}
