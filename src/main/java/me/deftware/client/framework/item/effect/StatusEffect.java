package me.deftware.client.framework.item.effect;

import net.minecraft.potion.Potion;
import me.deftware.client.framework.registry.Identifiable;
import net.minecraft.util.registry.IRegistry;

/**
 * @author Deftware
 */
public class StatusEffect implements Identifiable {

	private final Potion statusEffect;
	private final EffectType effectType;

	public StatusEffect(Potion statusEffect) {
		this.statusEffect = statusEffect;
		this.effectType = statusEffect.isBeneficial() ?
				EffectType.BENEFICIAL : statusEffect.isBadEffect() ?
				EffectType.HARMFUL : EffectType.NEUTRAL;
	}

	public Potion getMinecraftStatusEffect() {
		return statusEffect;
	}

	public EffectType getEffectType() {
		return effectType;
	}

	@Override
	public String getTranslationKey() {
		return statusEffect.getName();
	}

	@Override
	public String getIdentifierKey() {
		return IRegistry.MOB_EFFECT.getKey(statusEffect).getPath();
	}

}
