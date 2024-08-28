package me.deftware.client.framework.item;

import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.item.*;

import java.util.function.Predicate;

public enum ItemTypes {

    Tool(item -> item instanceof MiningToolItem),
    SplashPotion(item -> item instanceof SplashPotionItem),
    Soup(item -> item == Items.BEETROOT_SOUP || item == Items.MUSHROOM_STEW || item == Items.SUSPICIOUS_STEW),
    WritableBook(item -> item instanceof WritableBookItem),
    Bow(item -> item instanceof BowItem),
    Crossbow(item -> item instanceof CrossbowItem),
    Potion(item -> item instanceof PotionItem),
    FishingRod(item -> item instanceof FishingRodItem),
    Trident(item -> item instanceof TridentItem),
    ShulkerBox(item -> item instanceof BlockItem && item.getTranslationKey().contains("shulker_box")),
    RangedWeapon(item -> item instanceof RangedWeaponItem),
    Throwable(item -> item instanceof BowItem || item instanceof CrossbowItem || item instanceof SnowballItem
            || item instanceof EggItem || item instanceof EnderPearlItem || item instanceof SplashPotionItem
            || item instanceof LingeringPotionItem || item instanceof FishingRodItem
            || item instanceof TridentItem
    );

    private final Predicate<Item> predicate;

    ItemTypes(Predicate<Item> predicate) {
        this.predicate = predicate;
    }

    public boolean is(Item block) {
        return predicate.test(block);
    }

}
