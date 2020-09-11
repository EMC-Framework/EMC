package me.deftware.client.framework.item.types;


/**
 * @author Deftware
 */
public class WeaponItem extends ToolItem {

	public WeaponItem(net.minecraft.item.Item item) {
		super(item);
	}

	public float getAttackDamage() {
		/*if (item instanceof MiningToolItem) { FIXME
			final UUID MODIFIER_DAMAGE = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
			AtomicReference<Float> attackDamage = new AtomicReference<>((float) 0);
			item.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE).forEach(e -> {
				if (e.getId().equals(MODIFIER_DAMAGE)) {
					attackDamage.updateAndGet(v -> (float) (v + e.getAmount()));
				}
			});
			return attackDamage.get();
		}*/
		return 0;
	}

}
