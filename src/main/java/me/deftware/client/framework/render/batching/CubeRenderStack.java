package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

/**
 * @author Deftware
 */
public class CubeRenderStack extends RenderStack<CubeRenderStack> {

	private boolean lines = false;

	@Override
	public CubeRenderStack begin() {
		return begin(false);
	}

	public CubeRenderStack begin(boolean lines) {
		return begin(
				(this.lines = lines) ? GL11.GL_LINE_STRIP : GL11.GL_QUADS
		);
	}

	public CubeRenderStack draw(BoundingBox box) {
		if (box == null)
			return this;
		AxisAlignedBB minecraftBox = box.getOffsetMinecraftBox(-Minecraft.getCamera().getRenderPosX(), -Minecraft.getCamera().getRenderPosY(), -Minecraft.getCamera().getRenderPosZ());
		if (lines)
			drawSelectionBoundingBox(minecraftBox);
		else
			drawColorBox(minecraftBox);
		return this;
	}

	private void drawColorBox(AxisAlignedBB box) {
		vertex(box.minX, box.minY, box.minZ).endVertex();
		vertex(box.minX, box.maxY, box.minZ).endVertex();
		vertex(box.maxX, box.minY, box.minZ).endVertex();
		vertex(box.maxX, box.maxY, box.minZ).endVertex();
		vertex(box.maxX, box.minY, box.maxZ).endVertex();
		vertex(box.maxX, box.maxY, box.maxZ).endVertex();
		vertex(box.minX, box.minY, box.maxZ).endVertex();
		vertex(box.minX, box.maxY, box.maxZ).endVertex();

		vertex(box.maxX, box.maxY, box.minZ).endVertex();
		vertex(box.maxX, box.minY, box.minZ).endVertex();
		vertex(box.minX, box.maxY, box.minZ).endVertex();
		vertex(box.minX, box.minY, box.minZ).endVertex();
		vertex(box.minX, box.maxY, box.maxZ).endVertex();
		vertex(box.minX, box.minY, box.maxZ).endVertex();
		vertex(box.maxX, box.maxY, box.maxZ).endVertex();
		vertex(box.maxX, box.minY, box.maxZ).endVertex();

		vertex(box.minX, box.maxY, box.minZ).endVertex();
		vertex(box.maxX, box.maxY, box.minZ).endVertex();
		vertex(box.maxX, box.maxY, box.maxZ).endVertex();
		vertex(box.minX, box.maxY, box.maxZ).endVertex();
		vertex(box.minX, box.maxY, box.minZ).endVertex();
		vertex(box.minX, box.maxY, box.maxZ).endVertex();
		vertex(box.maxX, box.maxY, box.maxZ).endVertex();
		vertex(box.maxX, box.maxY, box.minZ).endVertex();

		vertex(box.minX, box.minY, box.minZ).endVertex();
		vertex(box.maxX, box.minY, box.minZ).endVertex();
		vertex(box.maxX, box.minY, box.maxZ).endVertex();
		vertex(box.minX, box.minY, box.maxZ).endVertex();
		vertex(box.minX, box.minY, box.minZ).endVertex();
		vertex(box.minX, box.minY, box.maxZ).endVertex();
		vertex(box.maxX, box.minY, box.maxZ).endVertex();
		vertex(box.maxX, box.minY, box.minZ).endVertex();

		vertex(box.minX, box.minY, box.minZ).endVertex();
		vertex(box.minX, box.maxY, box.minZ).endVertex();
		vertex(box.minX, box.minY, box.maxZ).endVertex();
		vertex(box.minX, box.maxY, box.maxZ).endVertex();
		vertex(box.maxX, box.minY, box.maxZ).endVertex();
		vertex(box.maxX, box.maxY, box.maxZ).endVertex();
		vertex(box.maxX, box.minY, box.minZ).endVertex();
		vertex(box.maxX, box.maxY, box.minZ).endVertex();

		vertex(box.minX, box.maxY, box.maxZ).endVertex();
		vertex(box.minX, box.minY, box.maxZ).endVertex();
		vertex(box.minX, box.maxY, box.minZ).endVertex();
		vertex(box.minX, box.minY, box.minZ).endVertex();
		vertex(box.maxX, box.maxY, box.minZ).endVertex();
		vertex(box.maxX, box.minY, box.minZ).endVertex();
		vertex(box.maxX, box.maxY, box.maxZ).endVertex();
		vertex(box.maxX, box.minY, box.maxZ).endVertex();
	}

	private void drawSelectionBoundingBox(AxisAlignedBB box) {
		vertex(box.minX, box.minY, box.minZ).endVertex();
		vertex(box.maxX, box.minY, box.minZ).endVertex();
		vertex(box.maxX, box.minY, box.maxZ).endVertex();
		vertex(box.minX, box.minY, box.maxZ).endVertex();
		vertex(box.minX, box.minY, box.minZ).endVertex();

		vertex(box.minX, box.maxY, box.minZ).endVertex();
		vertex(box.maxX, box.maxY, box.minZ).endVertex();
		vertex(box.maxX, box.maxY, box.maxZ).endVertex();
		vertex(box.minX, box.maxY, box.maxZ).endVertex();
		vertex(box.minX, box.maxY, box.minZ).endVertex();

		drawBuffer();
		builder.begin(GL11.GL_LINES, getFormat());

		vertex(box.minX, box.minY, box.minZ).endVertex();
		vertex(box.minX, box.maxY, box.minZ).endVertex();
		vertex(box.maxX, box.minY, box.minZ).endVertex();
		vertex(box.maxX, box.maxY, box.minZ).endVertex();
		vertex(box.maxX, box.minY, box.maxZ).endVertex();
		vertex(box.maxX, box.maxY, box.maxZ).endVertex();
		vertex(box.minX, box.minY, box.maxZ).endVertex();
		vertex(box.minX, box.maxY, box.maxZ).endVertex();
	}

}
