package me.deftware.mixin.mixins.render;

import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import me.deftware.client.framework.math.vector.Vector3d;
import me.deftware.client.framework.render.camera.GameCamera;

/**
 * @author Deftware, wagyourtail
 */
@Mixin(RenderManager.class)
public class MixinCamera implements GameCamera {

	@Shadow
	private double renderPosX;

	@Shadow
	private double renderPosY;

	@Shadow
	private double renderPosZ;

    @Override
    public Vector3d getCameraPosition() {
        return new Vector3d(renderPosX, renderPosY, renderPosZ);
    }

    @Override
    public float _getRotationPitch() {
        return ((RenderManager) (Object) this).playerViewX;
    }

    @Override
    public float _getRotationYaw() {
        return ((RenderManager) (Object) this).playerViewY;
    }

    @Override
    public double _getRenderPosX() {
        return renderPosX;
    }

    @Override
    public double _getRenderPosY() {
        return renderPosY + 1;
    }

    @Override
    public double _getRenderPosZ() {
        return renderPosZ;
    }

}
