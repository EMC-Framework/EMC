package me.deftware.client.framework.world;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Biome {

	private net.minecraft.world.biome.Biome biome;

	public Biome setReference(net.minecraft.world.biome.Biome biome) {
		this.biome = biome;
		return this;
	}

	public String getKey() {
		Identifier id = Registry.BIOME.getId(biome);
		if (id == null)
			return null;
		return id.getPath();
	}

	public String getCatergory() {
		return biome.getCategory().getName();
	}

}
