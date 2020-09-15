package me.deftware.client.framework.render.camera.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.Vec3;

import java.util.Objects;

/**
 * @author wagyourtail, Deftware
 */
@SuppressWarnings("EntityConstructor")
public class CameraEntity extends EntityOtherPlayerMP {

	public MovementInput input;

	public CameraEntity(WorldClient clientWorld, GameProfile gameProfile, FoodStats hunger) {
		super(clientWorld, gameProfile);
		this.foodStats = hunger;
		Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
		this.input = new MovementInputFromOptions(mc.gameSettings);
	}

	@Override
	public boolean isInRangeToRender3d(double cameraX, double cameraY, double cameraZ) {
		return false;
	}

	@Override
	public boolean isInRangeToRenderDist(double distance) {
		return false;
	}

	@Override
	public boolean getAlwaysRenderNameTagForRender() {
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.setVelocity(0, 0, 0);

		input.updatePlayerMoveState();

		float upDown = (this.input.sneak ? -CameraEntityMan.speed : 0) + (this.input.jump ? CameraEntityMan.speed : 0);

		Vec3 forward = new Vec3(0, 0, CameraEntityMan.speed * 2.5).rotateYaw(-(float) Math.toRadians(this.rotationYawHead));
		Vec3 strafe = forward.rotateYaw((float) Math.toRadians(90));
		Vec3 motion = new Vec3(this.motionX, this.motionY, this.motionZ);

		motion = motion.addVector(0, 2 * upDown, 0);
		motion = motion.addVector(strafe.xCoord * input.moveStrafe, 0, strafe.zCoord * input.moveStrafe);
		motion = motion.addVector(forward.xCoord * input.moveForward, 0, forward.zCoord * input.moveForward);

		this.setPosition(this.posX + motion.xCoord, this.posY + motion.yCoord, this.posZ + motion.zCoord);
	}

	public void spawn() {
		Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
		Objects.requireNonNull(mc.theWorld).addEntityToWorld(this.getEntityId(), this);
	}

	public void despawn() {
		Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
		Objects.requireNonNull(mc.theWorld).removeEntityFromWorld(this.getEntityId());
	}

}
