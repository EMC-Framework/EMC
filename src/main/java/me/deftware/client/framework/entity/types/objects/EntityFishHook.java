package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.entity.Entity;
import net.minecraft.client.Minecraft;

import java.util.Objects;

/**
 * @author Deftware
 */
public class EntityFishHook extends Entity {

	private static net.minecraft.entity.projectile.EntityFishHook fishHookPtr;
	private static EntityFishHook fishHook;

	public static boolean hasFish() {
		return Objects.requireNonNull(Minecraft.getMinecraft().player).fishEntity != null;
	}

	public static EntityFishHook getInstance() {
		if (fishHook == null || Objects.requireNonNull(Minecraft.getMinecraft().player).fishEntity != fishHookPtr) {
			fishHookPtr = Objects.requireNonNull(Minecraft.getMinecraft().player).fishEntity;
			fishHook = new EntityFishHook(fishHookPtr);
		}
		return fishHook;
	}

	public EntityFishHook(net.minecraft.entity.Entity entity) {
		super(entity);
	}

}
