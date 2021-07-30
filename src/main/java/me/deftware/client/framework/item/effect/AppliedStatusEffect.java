package me.deftware.client.framework.item.effect;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author Deftware
 */
public class AppliedStatusEffect {

	private final StatusEffect effect;
	private PotionEffect instance;

	public AppliedStatusEffect(PotionEffect instance) {
		this.effect = new StatusEffect(Potion.potionTypes[instance.getPotionID()]);
		this.instance = instance;
	}

	public AppliedStatusEffect(StatusEffect effect, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon) {
		this.effect = effect;
		this.instance = new PotionEffect(
				effect.getMinecraftStatusEffect().getId(), duration, amplifier, ambient, showParticles
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

	public boolean isPermanent() {
		return instance.getIsPotionDurationMax();
	}

	public boolean isAmbient() {
		return instance.getIsAmbient();
	}

	public AppliedStatusEffect setInstance(PotionEffect instance) {
		this.instance = instance;
		return this;
	}

}
