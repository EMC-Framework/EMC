package me.deftware.client.framework.render.camera.entity;

import net.minecraft.util.MovementInput;

/**
 * @author wagyourtail, Deftware
 */
public class DummyInput extends MovementInput {

	public DummyInput() {
		this.moveStrafe = 0.0F;
		this.moveForward = 0.0F;
		this.forwardKeyDown = false;
		this.backKeyDown = false;
		this.leftKeyDown = false;
		this.rightKeyDown = false;
		this.jump = false;
		this.sneak = false;
	}

}
