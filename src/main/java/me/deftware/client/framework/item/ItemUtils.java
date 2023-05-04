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
            if (stack.getItem() instanceof ArmorItem armor) {
                amount.add(armor.getProtection());
            }
        });
        return amount.intValue();
    }

    public static float getDamage(ItemStack stack) {
        MutableFloat amount = new MutableFloat();
        stack.enchantments((level, enchantment) -> {
            amount.add(enchantment.getDamage(level));
        });
        if (stack.getItem() instanceof AttackItem item) {
            amount.add(item.getAttackDamage());
        }
        return amount.floatValue();
    }

    public static class Crossbow {

        public static boolean isCharged(ItemStack stack) {
            if (stack.getItem() instanceof CrossbowItem) {
                return CrossbowItem.isCharged((net.minecraft.item.ItemStack) stack);
            }
            return false;
        }

    }

}
