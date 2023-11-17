package me.deftware.client.framework.input;

import me.deftware.mixin.imp.IMixinKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;

/**
 * @author Deftware
 */
public enum MinecraftKeyBind {

	SNEAK(Minecraft.getInstance().gameSettings.keyBindSneak),
	USE_ITEM(Minecraft.getInstance().gameSettings.keyBindUseItem),
	JUMP(Minecraft.getInstance().gameSettings.keyBindJump),
	SPRINT(Minecraft.getInstance().gameSettings.keyBindSprint),
	FORWARD(Minecraft.getInstance().gameSettings.keyBindForward),
	BACK(Minecraft.getInstance().gameSettings.keyBindBack),
	LEFT(Minecraft.getInstance().gameSettings.keyBindLeft),
	RIGHT(Minecraft.getInstance().gameSettings.keyBindRight),
	ATTACK(Minecraft.getInstance().gameSettings.keyBindAttack);

	private final KeyBinding bind;

	public boolean isHeld() {
		return InputMappings.isKeyDown(((IMixinKeyBinding) this.bind).getInput().getKeyCode());
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
