package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.message.Message;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.class)
public class MixinItem implements me.deftware.client.framework.item.Item {

    @Unique
    @Override
    public int getID() {
        return Item.getRawId((Item) (Object) this);
    }

    @Unique
    @Override
    public Message getName() {
        return (Message) ((Item) (Object) this).getName();
    }

    @Unique
    private FoodComponent foodComponent() {
        return ((Item) (Object) this).getComponents().get(DataComponentTypes.FOOD);
    }

    @Unique
    @Override
    public boolean isFood() {
        return foodComponent() != null;
    }

    @Unique
    @Override
    public int getHunger() {
        return foodComponent().nutrition();
    }

    @Unique
    @Override
    public float getSaturation() {
        return foodComponent().saturationModifier();
    }

    @Unique
    @Override
    public String getTranslationKey() {
        return ((Item) (Object) this).getTranslationKey();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return Registries.ITEM.getId((Item) (Object) this).getPath();
    }

    @Unique
    @Override
    public me.deftware.client.framework.item.Item getItem() {
        return this;
    }

}
