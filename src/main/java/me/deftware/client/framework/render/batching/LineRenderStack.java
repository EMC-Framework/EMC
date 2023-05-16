package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.util.Vec3;
import me.deftware.client.framework.render.gl.GLX;
import org.lwjgl.opengl.GL11;

/**
 * @author Deftware
 */
public class LineRenderStack extends RenderStack<LineRenderStack> {

	private Vec3 eyes;

	@Override
	public LineRenderStack begin(GLX context) {
		return begin(context, GL11.GL_LINES);
	}

	@Override
	public LineRenderStack begin(GLX context, int mode) {
		eyes = new Vec3(0.0D, 0.0D, 1.0D);
		if (Minecraft.getMinecraftGame().getCamera() != null)
			eyes = eyes.rotatePitch(-(float) Math.toRadians(Minecraft.getMinecraftGame().getCamera()._getRotationPitch()))
				.rotateYaw(-(float) Math.toRadians(Minecraft.getMinecraftGame().getCamera()._getRotationYaw()));
		return super.begin(context, mode);
	}

	public LineRenderStack drawLine(float x1, float y1, float x2, float y2) {
		// Scale
		if (scaled) {
			x1 *= getScale();
			y1 *= getScale();
			x2 *= getScale();
			y2 *= getScale();
		}
		// Draw
		vertex(x1, y1, 0).next();
		vertex(x2, y2, 0).next();
		return this;
	}

	public void vertex(double x, double y) {
		vertex(x, y, 0).next();
	}

	public LineRenderStack lineToBlockPosition(BlockPosition pos) {
		return drawLine(
				pos.getX() - Minecraft.getMinecraftGame().getCamera()._getRenderPosX(),
				pos.getY() + 1f / 2.0F - Minecraft.getMinecraftGame().getCamera()._getRenderPosY(),
				pos.getZ() - Minecraft.getMinecraftGame().getCamera()._getRenderPosZ()
		);
	}

	public LineRenderStack lineToEntity(TileEntity entity) {
		return lineToBlockPosition(entity.getBlockPosition());
	}

	public LineRenderStack lineToEntity(Entity entity) {
		return drawLine(
				entity.getBlockPosition().getX() - Minecraft.getMinecraftGame().getCamera()._getRenderPosX(),
				entity.getBlockPosition().getY() + entity.getHeight() / 2.0F - Minecraft.getMinecraftGame().getCamera()._getRenderPosY(),
				entity.getBlockPosition().getZ() - Minecraft.getMinecraftGame().getCamera()._getRenderPosZ()
		);
	}

	public LineRenderStack drawPoint(double x, double y, double z) {
		vertex(x, y, z).next();
		return this;
	}

	public LineRenderStack drawLine(double x, double y, double z) {
		return drawPoint(eyes.xCoord, Minecraft.getMinecraftGame()._getPlayer().getEyeHeight() + eyes.yCoord, eyes.zCoord).drawPoint(x, y, z);
	}

}
