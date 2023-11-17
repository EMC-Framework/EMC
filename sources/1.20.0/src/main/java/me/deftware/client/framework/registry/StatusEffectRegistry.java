package me.deftware.client.framework.registry;

import me.deftware.client.framework.entity.effect.Effect;
import net.minecraft.registry.Registries;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum StatusEffectRegistry implements IRegistry.IdentifiableRegistry<Effect, Void> {

	INSTANCE;


	@Override
	public Stream<Effect> stream() {
		return Registries.STATUS_EFFECT
				.stream()
				.map(Effect.class::cast);
	}

}
