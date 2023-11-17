package me.deftware.client.framework.item;

import net.minecraft.item.*;

import java.util.function.Predicate;

public enum ItemTypes {

    Tool(item -> item instanceof ItemTool),
    SplashPotion(item -> item instanceof ItemSplashPotion),
    Soup(item -> item instanceof ItemSoup),
    WritableBook(item -> item instanceof ItemWritableBook),
    Bow(item -> item instanceof ItemBow),
    Crossbow(item -> false),
    Potion(item -> item instanceof ItemPotion),
    FishingRod(item -> item instanceof ItemFishingRod),
    Trident(item -> item instanceof ItemTrident),
    ShulkerBox(item -> item instanceof ItemBlock && item.getTranslationKey().contains("shulker_box")),
    RangedWeapon(Bow.predicate),
    Throwable(item -> item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg
            || item instanceof ItemEnderPearl || item instanceof ItemSplashPotion
            || item instanceof ItemLingeringPotion || item instanceof ItemFishingRod
    );

    private final Predicate<Item> predicate;

    ItemTypes(Predicate<Item> predicate) {
        this.predicate = predicate;
    }

    public boolean is(Item block) {
        return predicate.test(block);
    }

}
