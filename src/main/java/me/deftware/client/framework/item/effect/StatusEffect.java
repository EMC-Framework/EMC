package me.deftware.client.framework.item.effect;

import me.deftware.mixin.imp.IdentifiableResource;
import net.minecraft.potion.Potion;
import me.deftware.client.framework.registry.Identifiable;

/**
 * @author Deftware
 */
public class StatusEffect implements Identifiable {

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

	@Override
	public String getTranslationKey() {
		return statusEffect.getName();
	}

	@Override
	public String getIdentifierKey() {
		return ((IdentifiableResource) statusEffect).getResourceLocation().getResourcePath();
	}

}
