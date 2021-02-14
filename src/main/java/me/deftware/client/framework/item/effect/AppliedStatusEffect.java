package me.deftware.client.framework.item.effect;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author Deftware
 */
public class AppliedStatusEffect {

	private final StatusEffect effect;
	private final PotionEffect instance;

	public AppliedStatusEffect(PotionEffect instance) {
		this.effect = new StatusEffect(Potion.potionTypes[instance.getPotionID()]);
		this.instance = instance;
	}

	public AppliedStatusEffect(StatusEffect effect, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon) {
		this.effect = effect;
		this.instance = new PotionEffect(
				effect.getMinecraftStatusEffect(), duration, amplifier, ambient, showParticles
		);
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
		return instance.getIsAmbient();
	}

}
