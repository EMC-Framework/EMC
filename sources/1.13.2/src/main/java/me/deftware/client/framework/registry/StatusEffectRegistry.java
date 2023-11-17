package me.deftware.client.framework.registry;

import me.deftware.client.framework.entity.effect.Effect;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum StatusEffectRegistry implements IRegistry.IdentifiableRegistry<Effect, Void> {

	INSTANCE;

	@Override
	public Stream<Effect> stream() {
		return net.minecraft.util.registry.IRegistry.MOB_EFFECT
				.stream()
				.map(Effect.class::cast);
	}

}
