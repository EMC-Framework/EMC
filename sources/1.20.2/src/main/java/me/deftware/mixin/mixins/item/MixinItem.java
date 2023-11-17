package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.message.Message;
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
    @Override
    public boolean isFood() {
        return ((Item) (Object) this).isFood();
    }

    @Unique
    @Override
    public int getHunger() {
        return ((Item) (Object) this).getFoodComponent().getHunger();
    }

    @Unique
    @Override
    public float getSaturation() {
        return ((Item) (Object) this).getFoodComponent().getSaturationModifier();
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
