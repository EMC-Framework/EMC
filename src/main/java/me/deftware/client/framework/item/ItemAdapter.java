package me.deftware.client.framework.item;

import me.deftware.client.framework.item.items.ArmorItem;
import me.deftware.client.framework.item.items.AttackItem;
import me.deftware.client.framework.message.Message;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adapts data-component-based Minecraft 26.3 items to EMC's ArmorItem and AttackItem interfaces.
 */
public final class ItemAdapter {

    private static final Map<net.minecraft.world.item.Item, Item> CACHE = new ConcurrentHashMap<>();

    private ItemAdapter() {
    }

    public static Item wrap(net.minecraft.world.item.Item mcItem) {
        if (mcItem == null) {
            return null;
        }
        return CACHE.computeIfAbsent(mcItem, item -> {
            if (item.builtInRegistryHolder().areComponentsBound()) {
                Equippable equippable = item.components().get(DataComponents.EQUIPPABLE);
                if (equippable != null && equippable.slot().isArmor()) {
                    return new ArmorAdapter(item, equippable);
                }
                if (Item.damage((Item) item) > 0.0f) {
                    return new AttackAdapter(item);
                }
            }
            return (Item) item;
        });
    }

    private static double sumAttribute(net.minecraft.world.item.Item item, Holder<Attribute> target) {
        ItemAttributeModifiers component = item.components().get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (component == null) {
            return 0.0;
        }
        double sum = 0.0;
        for (ItemAttributeModifiers.Entry entry : component.modifiers()) {
            if (entry.attribute().equals(target)) {
                sum += entry.modifier().amount();
            }
        }
        return sum;
    }

    private static class BaseAdapter implements Item {
        protected final net.minecraft.world.item.Item mcItem;
        protected final Item emcItem;

        BaseAdapter(net.minecraft.world.item.Item mcItem) {
            this.mcItem = mcItem;
            this.emcItem = (Item) mcItem;
        }

        @Override
        public net.minecraft.world.item.Item asMinecraftItem() {
            return mcItem;
        }

        @Override
        public int getID() {
            return emcItem.getID();
        }

        @Override
        public Message getName() {
            return emcItem.getName();
        }

        @Override
        public boolean isFood() {
            return emcItem.isFood();
        }

        @Override
        public int getHunger() {
            return emcItem.getHunger();
        }

        @Override
        public float getSaturation() {
            return emcItem.getSaturation();
        }

        @Override
        public String getTranslationKey() {
            return emcItem.getTranslationKey();
        }

        @Override
        public String getIdentifierKey() {
            return emcItem.getIdentifierKey();
        }

        @Override
        public Item getItem() {
            return this;
        }
    }

    private static final class ArmorAdapter extends BaseAdapter implements ArmorItem {
        private final Equippable equippable;

        ArmorAdapter(net.minecraft.world.item.Item mcItem, Equippable equippable) {
            super(mcItem);
            this.equippable = equippable;
        }

        @Override
        public int getDamageReduceAmount() {
            return (int) Math.round(sumAttribute(mcItem, Attributes.ARMOR));
        }

        @Override
        public float getToughness() {
            return (float) sumAttribute(mcItem, Attributes.ARMOR_TOUGHNESS);
        }

        @Override
        public int getTypeOrdinal() {
            return equippable.slot().ordinal();
        }
    }

    private static final class AttackAdapter extends BaseAdapter implements AttackItem {
        AttackAdapter(net.minecraft.world.item.Item mcItem) {
            super(mcItem);
        }

        @Override
        public float getAttackDamage() {
            return Item.damage(emcItem);
        }
    }

}
