package me.deftware.client.framework.util.minecraft;

import net.minecraft.util.ResourceLocation;

/**
 * @author Deftware
 */
public class MinecraftIdentifier extends ResourceLocation {

	public MinecraftIdentifier(String id) {
		super(id);
	}

	public MinecraftIdentifier(String namespace, String path) {
		super(namespace, path);
	}

}
