package me.deftware.client.framework.item.effect;

import net.minecraft.potion.PotionEffect;

/**
 * @author Deftware
 */
public class AppliedStatusEffect {

	private final StatusEffect effect;
	private final PotionEffect instance;

	public AppliedStatusEffect(PotionEffect instance) {
		this.effect = new StatusEffect(instance.getPotion());
		this.instance = instance;
	}

	public PotionEffect getMinecraftStatusEffectInstance() {
		return instance;
	}

	public StatusEffect getEffect() {
		return effect;
	}

	public int getDuration() {
		return instance.getDuration();
	}

	public int getAmplifier() {
		return instance.getAmplifier();
	}

	public boolean isAmbient() {
		return instance.isAmbient();
	}

}
