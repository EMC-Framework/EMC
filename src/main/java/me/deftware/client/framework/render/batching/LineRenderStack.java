package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

/**
 * @author Deftware
 */
public class LineRenderStack extends RenderStack<LineRenderStack> {

	private Vec3 eyes;

	@Override
	public LineRenderStack begin() {
		return begin(GL11.GL_LINES);
	}

	@Override
	public LineRenderStack begin(int mode) {
		eyes = new Vec3(0.0D, 0.0D, 1.0D);
		if (Minecraft.getCamera().getMinecraftCamera() != null)
			eyes = eyes.rotatePitch(-(float) Math.toRadians(Minecraft.getCamera().getRotationPitch()))
				.rotateYaw(-(float) Math.toRadians(Minecraft.getCamera().getRotationYaw()));
		return super.begin(mode);
	}

	public LineRenderStack drawLine(float x1, float y1, float x2, float y2, boolean scaling) {
		// Scale
		if (scaling) {
			x1 *= getScale();
			y1 *= getScale();
			x2 *= getScale();
			y2 *= getScale();
		}
		// Draw
		vertex(x1, y1, 0).endVertex();
		vertex(x2, y2, 0).endVertex();
		return this;
	}

	public void vertex(double x, double y) {
		vertex(x, y, 0).endVertex();
	}

	public LineRenderStack lineToBlockPosition(BlockPosition pos) {
		return drawLine(
				pos.getX() - Minecraft.getCamera().getRenderPosX(),
				pos.getY() + 1f / 2.0F - Minecraft.getCamera().getRenderPosY(),
				pos.getZ() - Minecraft.getCamera().getRenderPosZ()
		);
	}

	public LineRenderStack lineToEntity(TileEntity entity) {
		return lineToBlockPosition(entity.getBlockPosition());
	}

	public LineRenderStack lineToEntity(Entity entity) {
		return drawLine(
				entity.getBlockPosition().getX() - Minecraft.getCamera().getRenderPosX(),
				entity.getBlockPosition().getY() + entity.getHeight() / 2.0F - Minecraft.getCamera().getRenderPosY(),
				entity.getBlockPosition().getZ() - Minecraft.getCamera().getRenderPosZ()
		);
	}

	public LineRenderStack drawPoint(double x, double y, double z) {
		vertex(x, y, z).endVertex();
		return this;
	}

	public LineRenderStack drawLine(double x, double y, double z) {
		return drawPoint(eyes.xCoord, Minecraft.getPlayer().getEyeHeight() + eyes.yCoord, eyes.zCoord).drawPoint(x, y, z);
	}

}
