package me.deftware.client.framework.item.effect;

import net.minecraft.potion.Potion;

/**
 * @author Deftware
 */
public class StatusEffect {

	private final Potion statusEffect;
	private final EffectType effectType;

	public StatusEffect(Potion statusEffect) {
		this.statusEffect = statusEffect;
		this.effectType = !statusEffect.isBadEffect() ?
				EffectType.BENEFICIAL : statusEffect.isBadEffect() ?
				EffectType.HARMFUL : EffectType.NEUTRAL;
	}

	public int getId() {
		return statusEffect.getId();
	}

	public Potion getMinecraftStatusEffect() {
		return statusEffect;
	}

	public EffectType getEffectType() {
		return effectType;
	}

	public String getUnlocalizedName() {
		return statusEffect.getName();
	}

}
