package me.deftware.client.framework.world;

import me.deftware.client.framework.world.gen.BiomeDecorator;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

public class Biome {

	private Holder<net.minecraft.world.level.biome.Biome> biome;

	public Biome setReference(Holder<net.minecraft.world.level.biome.Biome> biome) {
		this.biome = biome;
		return this;
	}

	private Identifier getId() {
		var key = biome.unwrapKey().get();
		return key.identifier();
	}

	public String getKey() {
		Identifier id = getId();
		if (id == null)
			return null;
		return id.getPath();
	}

	public String getCatergory() {
		return this.biome.unwrap().map(b -> b.identifier().getPath(), b -> "[unregistered]");
	}

	public BiomeDecorator getDecorator() {
		return BiomeDecorator.BIOME_DECORATORS.get(this.getId());
	}

}
