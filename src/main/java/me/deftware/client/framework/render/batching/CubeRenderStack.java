package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Box;
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
		Box minecraftBox = (Box) box.offset(-Minecraft.getMinecraftGame().getCamera()._getRenderPosX(), -Minecraft.getMinecraftGame().getCamera()._getRenderPosY(), -Minecraft.getMinecraftGame().getCamera()._getRenderPosZ());
		if (lines)
			drawSelectionBoundingBox(minecraftBox);
		else
			drawColorBox(minecraftBox);
		return this;
	}

	private void drawColorBox(Box box) {
		vertex(box.x1, box.y1, box.z1).next();
		vertex(box.x1, box.y2, box.z1).next();
		vertex(box.x2, box.y1, box.z1).next();
		vertex(box.x2, box.y2, box.z1).next();
		vertex(box.x2, box.y1, box.z2).next();
		vertex(box.x2, box.y2, box.z2).next();
		vertex(box.x1, box.y1, box.z2).next();
		vertex(box.x1, box.y2, box.z2).next();

		vertex(box.x2, box.y2, box.z1).next();
		vertex(box.x2, box.y1, box.z1).next();
		vertex(box.x1, box.y2, box.z1).next();
		vertex(box.x1, box.y1, box.z1).next();
		vertex(box.x1, box.y2, box.z2).next();
		vertex(box.x1, box.y1, box.z2).next();
		vertex(box.x2, box.y2, box.z2).next();
		vertex(box.x2, box.y1, box.z2).next();

		vertex(box.x1, box.y2, box.z1).next();
		vertex(box.x2, box.y2, box.z1).next();
		vertex(box.x2, box.y2, box.z2).next();
		vertex(box.x1, box.y2, box.z2).next();
		vertex(box.x1, box.y2, box.z1).next();
		vertex(box.x1, box.y2, box.z2).next();
		vertex(box.x2, box.y2, box.z2).next();
		vertex(box.x2, box.y2, box.z1).next();

		vertex(box.x1, box.y1, box.z1).next();
		vertex(box.x2, box.y1, box.z1).next();
		vertex(box.x2, box.y1, box.z2).next();
		vertex(box.x1, box.y1, box.z2).next();
		vertex(box.x1, box.y1, box.z1).next();
		vertex(box.x1, box.y1, box.z2).next();
		vertex(box.x2, box.y1, box.z2).next();
		vertex(box.x2, box.y1, box.z1).next();

		vertex(box.x1, box.y1, box.z1).next();
		vertex(box.x1, box.y2, box.z1).next();
		vertex(box.x1, box.y1, box.z2).next();
		vertex(box.x1, box.y2, box.z2).next();
		vertex(box.x2, box.y1, box.z2).next();
		vertex(box.x2, box.y2, box.z2).next();
		vertex(box.x2, box.y1, box.z1).next();
		vertex(box.x2, box.y2, box.z1).next();

		vertex(box.x1, box.y2, box.z2).next();
		vertex(box.x1, box.y1, box.z2).next();
		vertex(box.x1, box.y2, box.z1).next();
		vertex(box.x1, box.y1, box.z1).next();
		vertex(box.x2, box.y2, box.z1).next();
		vertex(box.x2, box.y1, box.z1).next();
		vertex(box.x2, box.y2, box.z2).next();
		vertex(box.x2, box.y1, box.z2).next();
	}

	private void drawSelectionBoundingBox(Box box) {
		vertex(box.x1, box.y1, box.z1).next();
		vertex(box.x2, box.y1, box.z1).next();
		vertex(box.x2, box.y1, box.z2).next();
		vertex(box.x1, box.y1, box.z2).next();
		vertex(box.x1, box.y1, box.z1).next();

		vertex(box.x1, box.y2, box.z1).next();
		vertex(box.x2, box.y2, box.z1).next();
		vertex(box.x2, box.y2, box.z2).next();
		vertex(box.x1, box.y2, box.z2).next();
		vertex(box.x1, box.y2, box.z1).next();

		builder.end();
		BufferRenderer.draw(builder);

		builder.begin(GL11.GL_LINES, getFormat());

		vertex(box.x1, box.y1, box.z1).next();
		vertex(box.x1, box.y2, box.z1).next();
		vertex(box.x2, box.y1, box.z1).next();
		vertex(box.x2, box.y2, box.z1).next();
		vertex(box.x2, box.y1, box.z2).next();
		vertex(box.x2, box.y2, box.z2).next();
		vertex(box.x1, box.y1, box.z2).next();
		vertex(box.x1, box.y2, box.z2).next();
	}

}
