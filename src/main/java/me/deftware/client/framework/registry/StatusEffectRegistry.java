package me.deftware.client.framework.registry;

import me.deftware.client.framework.entity.effect.Effect;
import net.minecraft.potion.Potion;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum StatusEffectRegistry implements IRegistry.IdentifiableRegistry<Effect, Void> {

	INSTANCE;

	@Override
	public Stream<Effect> stream() {
		return Stream.of(Potion.potionTypes)
				.filter(Objects::nonNull)
				.map(Effect.class::cast);
	}

}
