package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.world.phys.AABB;
import org.lwjgl.opengl.GL11;

/**
 * @author Deftware
 */
public class CubeRenderStack extends RenderStack<CubeRenderStack> {

	private boolean lines = false;

	@Override
	public CubeRenderStack begin(GLX context) {
		return begin(context, false);
	}

	public CubeRenderStack begin(GLX context, boolean lines) {
		return begin(
				context, (this.lines = lines) ? GL11.GL_LINES : GL11.GL_QUADS
		);
	}

	public CubeRenderStack draw(BoundingBox box) {
		if (box == null)
			return this;
		AABB minecraftBox = (AABB) box.offset(-Minecraft.getMinecraftGame().getCamera()._getRenderPosX(), -Minecraft.getMinecraftGame().getCamera()._getRenderPosY(), -Minecraft.getMinecraftGame().getCamera()._getRenderPosZ());
		if (lines)
			drawSelectionBoundingBox(minecraftBox);
		else
			drawColorBox(minecraftBox);
		return this;
	}

	private void drawColorBox(AABB box) {
		// North (-Z)
		vertex(box.minX, box.minY, box.minZ).next();
		vertex(box.minX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.minY, box.minZ).next();

		// South (+Z)
		vertex(box.maxX, box.minY, box.maxZ).next();
		vertex(box.maxX, box.maxY, box.maxZ).next();
		vertex(box.minX, box.maxY, box.maxZ).next();
		vertex(box.minX, box.minY, box.maxZ).next();

		// Top (+Y)
		vertex(box.minX, box.maxY, box.minZ).next();
		vertex(box.minX, box.maxY, box.maxZ).next();
		vertex(box.maxX, box.maxY, box.maxZ).next();
		vertex(box.maxX, box.maxY, box.minZ).next();

		// Bottom (-Y)
		vertex(box.minX, box.minY, box.minZ).next();
		vertex(box.maxX, box.minY, box.minZ).next();
		vertex(box.maxX, box.minY, box.maxZ).next();
		vertex(box.minX, box.minY, box.maxZ).next();

		// West (-X)
		vertex(box.minX, box.minY, box.minZ).next();
		vertex(box.minX, box.minY, box.maxZ).next();
		vertex(box.minX, box.maxY, box.maxZ).next();
		vertex(box.minX, box.maxY, box.minZ).next();

		// East (+X)
		vertex(box.maxX, box.minY, box.maxZ).next();
		vertex(box.maxX, box.minY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.maxZ).next();
	}

	private void edge(double x0, double y0, double z0, double x1, double y1, double z1) {
		vertex(x0, y0, z0).next();
		vertex(x1, y1, z1).next();
	}

	private void drawSelectionBoundingBox(AABB box) {
		// Bottom face edges
		edge(box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ);
		edge(box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ);
		edge(box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ);
		edge(box.minX, box.minY, box.maxZ, box.minX, box.minY, box.minZ);
		// Top face edges
		edge(box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ);
		edge(box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ);
		edge(box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ);
		edge(box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ);
		// Vertical edges
		edge(box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ);
		edge(box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ);
		edge(box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ);
		edge(box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ);
	}

}
