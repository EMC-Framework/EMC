package me.deftware.client.framework.input;

import me.deftware.mixin.imp.IMixinKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

/**
 * @author Deftware
 */
public enum MinecraftKeyBind {

	SNEAK(net.minecraft.client.Minecraft.getInstance().options.keySneak),
	USE_ITEM(net.minecraft.client.Minecraft.getInstance().options.keyUse),
	JUMP(net.minecraft.client.Minecraft.getInstance().options.keyJump),
	SPRINT(net.minecraft.client.Minecraft.getInstance().options.keySprint),
	FORWARD(net.minecraft.client.Minecraft.getInstance().options.keyForward),
	BACK(net.minecraft.client.Minecraft.getInstance().options.keyBack),
	LEFT(net.minecraft.client.Minecraft.getInstance().options.keyLeft),
	RIGHT(net.minecraft.client.Minecraft.getInstance().options.keyRight),
	ATTACK(net.minecraft.client.Minecraft.getInstance().options.keyAttack);

	private final KeyBinding bind;

	public boolean isHeld() {
		return InputUtil.isKeyPressed(net.minecraft.client.Minecraft.getInstance().window.getHandle(), ((IMixinKeyBinding) this.bind).getInput().getKeyCode());
	}

	public boolean isPressed() {
		return this.bind.isPressed();
	}

	public void setPressed(boolean state) {
		((IMixinKeyBinding) this.bind).setPressed(state);
	}

	MinecraftKeyBind(KeyBinding bind) {
		this.bind = bind;
	}

}
