package me.deftware.client.framework.render.camera.entity;

import net.minecraft.client.input.Input;
import net.minecraft.util.math.Vec2f;

/**
 * @author wagyourtail, Deftware
 */
public class DummyInput extends Input {

	public DummyInput() {
		this.movementVector = Vec2f.ZERO;
	}

}
