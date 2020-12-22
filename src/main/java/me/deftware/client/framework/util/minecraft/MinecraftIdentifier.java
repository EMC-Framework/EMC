package me.deftware.client.framework.util.minecraft;

import net.minecraft.util.ResourceLocation;

/**
 * @author Deftware
 */
public class MinecraftIdentifier extends ResourceLocation {

	public MinecraftIdentifier(String id) {
		super(id);
	}

	public MinecraftIdentifier(ResourceLocation identifier) {
		super(identifier.getNamespace(), identifier.getPath());
	}

	public MinecraftIdentifier(String namespace, String path) {
		super(namespace, path);
	}

}
