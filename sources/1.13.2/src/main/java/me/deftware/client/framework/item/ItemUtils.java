package me.deftware.client.framework.item;

import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.item.items.AttackItem;
import net.minecraft.item.*;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Collection;

public class ItemUtils {

    public static boolean hasEffect(ItemStack stack, Effect effect) {
        MutableBoolean hasEffect = new MutableBoolean(false);
        stack.effects(appliedEffect -> {
            if (appliedEffect.getEffect() == effect) {
                hasEffect.setTrue();
            }
        });
        return hasEffect.booleanValue();
    }

    public static int getProtection(Collection<ItemStack> equipment) {
        MutableInt amount = new MutableInt();
        equipment.forEach(stack -> {
            stack.enchantments((level, enchantment) -> {
                amount.add(enchantment.getProtection(level));
            });
            if (stack.getItem() instanceof ItemArmor) {
                amount.add(((ItemArmor) stack.getItem()).getDamageReduceAmount());
            }
        });
        return amount.intValue();
    }

    public static float getDamage(ItemStack stack) {
        MutableFloat amount = new MutableFloat();
        stack.enchantments((level, enchantment) -> {
            amount.add(enchantment.getDamage(level));
        });
        if (stack.getItem() instanceof AttackItem) {
            amount.add(((AttackItem) stack.getItem()).getAttackDamage());
        }
        return amount.floatValue();
    }

    /**
     * Dummy class
     */
    public static class Crossbow {

        public static boolean isCharged(ItemStack stack) {
            return false;
        }

    }

}
