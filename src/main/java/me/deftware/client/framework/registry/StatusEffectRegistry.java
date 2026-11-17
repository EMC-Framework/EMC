package me.deftware.client.framework.registry;

import me.deftware.client.framework.entity.effect.Effect;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum StatusEffectRegistry implements IRegistry.IdentifiableRegistry<Effect, Void> {

	INSTANCE;


	@Override
	public Stream<Effect> stream() {
		return BuiltInRegistries.MOB_EFFECT
				.stream()
				.map(Effect.class::cast);
	}

}
