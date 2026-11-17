package me.deftware.client.framework.input;

import com.mojang.blaze3d.platform.InputConstants;
import me.deftware.mixin.imp.IMixinKeyBinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

/**
 * @author Deftware
 */
public enum MinecraftKeyBind {

	SNEAK(Minecraft.getInstance().options.keyShift),
	USE_ITEM(Minecraft.getInstance().options.keyUse),
	JUMP(Minecraft.getInstance().options.keyJump),
	SPRINT(Minecraft.getInstance().options.keySprint),
	FORWARD(Minecraft.getInstance().options.keyUp),
	BACK(Minecraft.getInstance().options.keyDown),
	LEFT(Minecraft.getInstance().options.keyLeft),
	RIGHT(Minecraft.getInstance().options.keyRight),
	ATTACK(Minecraft.getInstance().options.keyAttack);

	private final KeyMapping bind;

	public boolean isHeld() {
		return InputConstants.isKeyDown(((IMixinKeyBinding) this.bind).getInput().getValue());
	}

	public boolean isPressed() {
		return this.bind.isDown();
	}

	public void setPressed(boolean state) {
		this.bind.setDown(state);
	}

	MinecraftKeyBind(KeyMapping bind) {
		this.bind = bind;
	}

}
