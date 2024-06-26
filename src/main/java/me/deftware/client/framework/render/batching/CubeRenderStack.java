package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Box;
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
				context, (this.lines = lines) ? GL11.GL_LINE_STRIP : GL11.GL_QUADS
		);
	}

	public CubeRenderStack draw(BoundingBox box) {
		if (box == null)
			return this;
		Box minecraftBox = (Box) box.offset(-Minecraft.getMinecraftGame().getCamera()._getRenderPosX(), -Minecraft.getMinecraftGame().getCamera()._getRenderPosY(), -Minecraft.getMinecraftGame().getCamera()._getRenderPosZ());
		if (lines)
			drawSelectionBoundingBox(minecraftBox);
		else
			drawColorBox(minecraftBox);
		return this;
	}

	private void drawColorBox(Box box) {
		vertex(box.minX, box.minY, box.minZ).next();
		vertex(box.minX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.minY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.minY, box.maxZ).next();
		vertex(box.maxX, box.maxY, box.maxZ).next();
		vertex(box.minX, box.minY, box.maxZ).next();
		vertex(box.minX, box.maxY, box.maxZ).next();

		vertex(box.maxX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.minY, box.minZ).next();
		vertex(box.minX, box.maxY, box.minZ).next();
		vertex(box.minX, box.minY, box.minZ).next();
		vertex(box.minX, box.maxY, box.maxZ).next();
		vertex(box.minX, box.minY, box.maxZ).next();
		vertex(box.maxX, box.maxY, box.maxZ).next();
		vertex(box.maxX, box.minY, box.maxZ).next();

		vertex(box.minX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.maxZ).next();
		vertex(box.minX, box.maxY, box.maxZ).next();
		vertex(box.minX, box.maxY, box.minZ).next();
		vertex(box.minX, box.maxY, box.maxZ).next();
		vertex(box.maxX, box.maxY, box.maxZ).next();
		vertex(box.maxX, box.maxY, box.minZ).next();

		vertex(box.minX, box.minY, box.minZ).next();
		vertex(box.maxX, box.minY, box.minZ).next();
		vertex(box.maxX, box.minY, box.maxZ).next();
		vertex(box.minX, box.minY, box.maxZ).next();
		vertex(box.minX, box.minY, box.minZ).next();
		vertex(box.minX, box.minY, box.maxZ).next();
		vertex(box.maxX, box.minY, box.maxZ).next();
		vertex(box.maxX, box.minY, box.minZ).next();

		vertex(box.minX, box.minY, box.minZ).next();
		vertex(box.minX, box.maxY, box.minZ).next();
		vertex(box.minX, box.minY, box.maxZ).next();
		vertex(box.minX, box.maxY, box.maxZ).next();
		vertex(box.maxX, box.minY, box.maxZ).next();
		vertex(box.maxX, box.maxY, box.maxZ).next();
		vertex(box.maxX, box.minY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.minZ).next();

		vertex(box.minX, box.maxY, box.maxZ).next();
		vertex(box.minX, box.minY, box.maxZ).next();
		vertex(box.minX, box.maxY, box.minZ).next();
		vertex(box.minX, box.minY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.minY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.maxZ).next();
		vertex(box.maxX, box.minY, box.maxZ).next();
	}

	private void drawSelectionBoundingBox(Box box) {
		vertex(box.minX, box.minY, box.minZ).next();
		vertex(box.maxX, box.minY, box.minZ).next();
		vertex(box.maxX, box.minY, box.maxZ).next();
		vertex(box.minX, box.minY, box.maxZ).next();
		vertex(box.minX, box.minY, box.minZ).next();

		vertex(box.minX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.maxZ).next();
		vertex(box.minX, box.maxY, box.maxZ).next();
		vertex(box.minX, box.maxY, box.minZ).next();

		BufferRenderer.drawWithGlobalProgram(builder.end());
		setBuilder(VertexFormat.DrawMode.DEBUG_LINES, getFormat());

		vertex(box.minX, box.minY, box.minZ).next();
		vertex(box.minX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.minY, box.minZ).next();
		vertex(box.maxX, box.maxY, box.minZ).next();
		vertex(box.maxX, box.minY, box.maxZ).next();
		vertex(box.maxX, box.maxY, box.maxZ).next();
		vertex(box.minX, box.minY, box.maxZ).next();
		vertex(box.minX, box.maxY, box.maxZ).next();
	}

}
