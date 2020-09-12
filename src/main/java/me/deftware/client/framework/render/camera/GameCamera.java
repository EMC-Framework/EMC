package me.deftware.client.framework.render.camera;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.math.vector.Vector3d;
import me.deftware.mixin.imp.IMixinCamera;
import net.minecraft.client.renderer.entity.RenderManager;

/**
 * @author Deftware
 */
public class GameCamera {

	public RenderManager getMinecraftCamera() {
		return net.minecraft.client.Minecraft.getInstance().getRenderManager();
	}

	public Vector3d getCameraPosition() {
		return new Vector3d(getRenderPosX(), getRenderPosY(), getRenderPosZ());
	}

	public float getRotationPitch() {
		return getMinecraftCamera().renderViewEntity.rotationPitch;
	}

	public float getRotationYaw() {
		return getMinecraftCamera().renderViewEntity.rotationYaw;
	}

	public Entity getFocusedEntity() {
		return Entity.newInstance(getMinecraftCamera().pointedEntity);
	}

	public double getRenderPosX() {
		return ((IMixinCamera) getMinecraftCamera()).getRenderPosX();
	}

	public double getRenderPosY() {
		return ((IMixinCamera) getMinecraftCamera()).getRenderPosY();
	}

	public double getRenderPosZ() {
		return((IMixinCamera) getMinecraftCamera()).getRenderPosZ();
	}

}
