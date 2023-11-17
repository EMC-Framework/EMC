package me.deftware.client.framework.world;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Biome {

	private net.minecraft.world.biome.Biome biome;

	public Biome setReference(net.minecraft.world.biome.Biome biome) {
		this.biome = biome;
		return this;
	}

	private Identifier getId() {
		return Registry.BIOME.getId(biome);
	}

	public String getKey() {
		Identifier id = getId();
		if (id == null)
			return null;
		return id.getPath();
	}

	public String getCatergory() {
		return biome.getCategory().getName();
	}

}
