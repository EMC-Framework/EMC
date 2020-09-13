package me.deftware.client.framework.item.types;

import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Deftware
 */
public class WeaponItem extends ToolItem {

	public WeaponItem(net.minecraft.item.Item item) {
		super(item);
	}

	public float getAttackDamage() {
		final UUID MODIFIER_DAMAGE = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
		AtomicReference<Float> attackDamage = new AtomicReference<>((float) 0);
		item.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).forEach((key, value) -> {
			if (value.getID().equals(MODIFIER_DAMAGE)) {
				attackDamage.updateAndGet(v -> (float) (v + value.getAmount()));
			}
		});
		return attackDamage.get();
	}

}
