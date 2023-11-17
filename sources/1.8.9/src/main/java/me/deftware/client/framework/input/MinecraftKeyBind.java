package me.deftware.client.framework.input;

import me.deftware.mixin.imp.IMixinKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * @author Deftware
 */
public enum MinecraftKeyBind {

	SNEAK(Minecraft.getMinecraft().gameSettings.keyBindSneak),
	USE_ITEM(Minecraft.getMinecraft().gameSettings.keyBindUseItem),
	JUMP(Minecraft.getMinecraft().gameSettings.keyBindJump),
	SPRINT(Minecraft.getMinecraft().gameSettings.keyBindSprint),
	FORWARD(Minecraft.getMinecraft().gameSettings.keyBindForward),
	BACK(Minecraft.getMinecraft().gameSettings.keyBindBack),
	LEFT(Minecraft.getMinecraft().gameSettings.keyBindLeft),
	RIGHT(Minecraft.getMinecraft().gameSettings.keyBindRight),
	ATTACK(Minecraft.getMinecraft().gameSettings.keyBindAttack);

	private final KeyBinding bind;

	public boolean isHeld() {
		return Keyboard.isKeyDown(((IMixinKeyBinding) this.bind).getKey());
	}

	public boolean isPressed() {
		return this.bind.isKeyDown();
	}

	public void setPressed(boolean state) {
		((IMixinKeyBinding) this.bind).setPressed(state);
	}

	MinecraftKeyBind(KeyBinding bind) {
		this.bind = bind;
	}

}
