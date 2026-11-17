package me.deftware.client.framework.render.camera.entity;

import net.minecraft.client.player.ClientInput;
import net.minecraft.world.phys.Vec2;

/**
 * @author wagyourtail, Deftware
 */
public class DummyInput extends ClientInput {

	public DummyInput() {
		this.moveVector = Vec2.ZERO;
	}

}
