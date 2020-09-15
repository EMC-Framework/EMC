package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;

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
		WorldRenderer vb = ts.getWorldRenderer();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		ts.draw();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		ts.draw();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		ts.draw();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		ts.draw();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		ts.draw();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(box.minX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.minX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.maxY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.minZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.maxY, box.maxZ).color(0, 0, 0, 0).endVertex();
		vb.pos(box.maxX, box.minY, box.maxZ).color(0, 0, 0, 0).endVertex();
		ts.draw();
	}

	private void drawSelectionBoundingBox(AxisAlignedBB box) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(box.minX, box.minY, box.minZ).endVertex();
		vertexbuffer.pos(box.maxX, box.minY, box.minZ).endVertex();
		vertexbuffer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		vertexbuffer.pos(box.minX, box.minY, box.maxZ).endVertex();
		vertexbuffer.pos(box.minX, box.minY, box.minZ).endVertex();
		tessellator.draw();
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(box.minX, box.maxY, box.minZ).endVertex();
		vertexbuffer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		vertexbuffer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		vertexbuffer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		vertexbuffer.pos(box.minX, box.maxY, box.minZ).endVertex();
		tessellator.draw();
		vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(box.minX, box.minY, box.minZ).endVertex();
		vertexbuffer.pos(box.minX, box.maxY, box.minZ).endVertex();
		vertexbuffer.pos(box.maxX, box.minY, box.minZ).endVertex();
		vertexbuffer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		vertexbuffer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		vertexbuffer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		vertexbuffer.pos(box.minX, box.minY, box.maxZ).endVertex();
		vertexbuffer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		tessellator.draw();
	}

}
