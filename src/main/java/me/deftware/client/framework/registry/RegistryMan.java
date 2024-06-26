package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Itemizable;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public class RegistryMan {

	@Nullable
	public static Itemizable find(String id) {
		return Stream.concat(ItemRegistry.INSTANCE.stream(), BlockRegistry.INSTANCE.stream())
				.filter(e -> e.getIdentifierKey().equalsIgnoreCase(id))
				.findFirst().orElse(null);
	}

}
