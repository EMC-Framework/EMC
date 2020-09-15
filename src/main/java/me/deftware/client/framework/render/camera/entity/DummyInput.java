package me.deftware.client.framework.render.camera.entity;

import net.minecraft.util.MovementInput;

/**
 * @author wagyourtail, Deftware
 */
public class DummyInput extends MovementInput {

	public DummyInput() {
		this.moveStrafe = 0.0F;
		this.moveForward = 0.0F;
		this.jump = false;
		this.sneak = false;
	}

}
