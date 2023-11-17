package me.deftware.client.framework.item;

import net.minecraft.item.*;

import java.util.function.Predicate;

public enum ItemTypes {

    Tool(item -> item instanceof ItemTool),
    SplashPotion(item -> item instanceof ItemPotion), // TODO: isSplash(meta)
    Soup(item -> item instanceof ItemSoup),
    WritableBook(item -> item instanceof ItemWritableBook),
    Bow(item -> item instanceof ItemBow),
    Crossbow(item -> false),
    Potion(item -> item instanceof ItemPotion),
    FishingRod(item -> item instanceof ItemFishingRod),
    Trident(item -> false),
    ShulkerBox(item -> item instanceof ItemBlock && item.getTranslationKey().contains("shulker_box")),
    RangedWeapon(Bow.predicate),
    Throwable(item -> item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg
            || item instanceof ItemEnderPearl || item instanceof ItemFishingRod
    );

    private final Predicate<Item> predicate;

    ItemTypes(Predicate<Item> predicate) {
        this.predicate = predicate;
    }

    public boolean is(Item block) {
        return predicate.test(block);
    }

}
