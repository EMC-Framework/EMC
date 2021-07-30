package me.deftware.client.framework.world;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;

public class Biome {

	private net.minecraft.world.biome.Biome biome;

	public Biome setReference(net.minecraft.world.biome.Biome biome) {
		this.biome = biome;
		return this;
	}

	public String getKey() {
		ResourceLocation id = IRegistry.BIOME.getKey(biome);
		if (id == null)
			return null;
		return id.getPath();
	}

	public String getCatergory() {
		return biome.getCategory().name();
	}

}
