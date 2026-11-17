package me.deftware.client.framework.entity.types.objects;

import me.deftware.client.framework.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.projectile.FishingHook;
import java.util.Objects;

/**
 * @author Deftware
 */
public class EntityFishHook extends Entity {

	private static FishingHook fishHookPtr;
	private static EntityFishHook fishHook;

	public static boolean hasFish() {
		return Objects.requireNonNull(Minecraft.getInstance().player).fishing != null;
	}

	public static EntityFishHook getInstance() {
		if (fishHook == null || Objects.requireNonNull(Minecraft.getInstance().player).fishing != fishHookPtr) {
			fishHookPtr = Objects.requireNonNull(Minecraft.getInstance().player).fishing;
			fishHook = new EntityFishHook(fishHookPtr);
		}
		return fishHook;
	}

	public EntityFishHook(net.minecraft.world.entity.Entity entity) {
		super(entity);
	}

}
