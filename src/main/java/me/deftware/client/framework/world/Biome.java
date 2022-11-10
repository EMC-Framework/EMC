package me.deftware.client.framework.world;

import me.deftware.client.framework.world.gen.BiomeDecorator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

public class Biome {

	private RegistryEntry<net.minecraft.world.biome.Biome> biome;

	public Biome setReference(RegistryEntry<net.minecraft.world.biome.Biome> biome) {
		this.biome = biome;
		return this;
	}

	private Identifier getId() {
		var key = biome.getKey().get();
		return key.getValue();
	}

	public String getKey() {
		Identifier id = getId();
		if (id == null)
			return null;
		return id.getPath();
	}

	public String getCatergory() {
		return this.biome.getKeyOrValue().map(b -> b.getValue().getPath(), b -> "[unregistered]");
	}

	public BiomeDecorator getDecorator() {
		return BiomeDecorator.BIOME_DECORATORS.get(this.getId());
	}

}
