package me.deftware.client.framework.world;

import net.minecraft.world.biome.BiomeGenBase;

public class Biome {

	private BiomeGenBase biome;

	public Biome setReference(BiomeGenBase biome) {
		this.biome = biome;
		return this;
	}

	public String getKey() {
		// TODO
		return "plains";
	}

	public String getCatergory() {
		return biome.biomeName;
	}

}
