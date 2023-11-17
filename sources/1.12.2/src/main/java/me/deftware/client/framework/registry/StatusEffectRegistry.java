package me.deftware.client.framework.registry;

import com.google.common.collect.Streams;
import me.deftware.client.framework.entity.effect.Effect;
import net.minecraft.potion.Potion;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum StatusEffectRegistry implements IRegistry.IdentifiableRegistry<Effect, Void> {

	INSTANCE;

	@Override
	public Stream<Effect> stream() {
		return Streams.stream(Potion.REGISTRY.iterator())
				.map(Effect.class::cast);
	}

}
