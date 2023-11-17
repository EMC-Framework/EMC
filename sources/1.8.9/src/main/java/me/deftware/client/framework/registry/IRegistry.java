package me.deftware.client.framework.registry;

import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.ResourceLocation;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

	static <T> Stream<T> streamOf(RegistryNamespaced<ResourceLocation, T> registry) {
		return streamOf(registry.iterator());
	}

	static <T> Stream<T> streamOf(Iterator<T> iterator) {
		Iterable<T> iterable = () -> iterator;
		return StreamSupport.stream(iterable.spliterator(), false);
	}

}
