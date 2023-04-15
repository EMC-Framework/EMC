package me.deftware.client.framework.registry;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public interface IRegistry<Type, InternalType> {

	Optional<Type> find(String id);

	Stream<Type> stream();

	default void register(String id, InternalType object) {
		throw new RuntimeException("Not implemented");
	}

	interface IdentifiableRegistry<T extends Identifiable, I> extends IRegistry<T, I> {

		@Override
		default Optional<T> find(String id) {
			return stream()
					.filter(i -> i.getTranslationKey().equalsIgnoreCase(id) || i.getIdentifierKey().equalsIgnoreCase(id))
					.findFirst();
		}

	}

}
