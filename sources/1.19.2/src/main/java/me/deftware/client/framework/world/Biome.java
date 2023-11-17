package me.deftware.client.framework.world;

import me.deftware.client.framework.world.gen.BiomeDecorator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

public class Biome {

	private RegistryEntry<net.minecraft.world.biome.Biome> biome;

	public Biome setReference(RegistryEntry<net.minecraft.world.biome.Biome> biome) {
		this.biome = biome;
		return this;
	}

	private Identifier getId() {
		return MinecraftClient.getInstance().world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome.value());
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
