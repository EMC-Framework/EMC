package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.message.Message;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.class)
public class MixinItem implements me.deftware.client.framework.item.Item {

    @Unique
    @Override
    public int getID() {
        return Item.getId((Item) (Object) this);
    }

    @Unique
    @Override
    public Message getName() {
        Item self = (Item) (Object) this;
        if (!self.builtInRegistryHolder().areComponentsBound()) {
            return (Message) (net.minecraft.network.chat.Component) net.minecraft.network.chat.Component.translatable(self.getDescriptionId());
        }
        return (Message) self.getName(new net.minecraft.world.item.ItemStack(self));
    }

    @Unique
    private FoodProperties foodComponent() {
        Item self = (Item) (Object) this;
        if (!self.builtInRegistryHolder().areComponentsBound()) {
            return null;
        }
        return self.components().get(DataComponents.FOOD);
    }

    @Unique
    @Override
    public boolean isFood() {
        return foodComponent() != null;
    }

    @Unique
    @Override
    public int getHunger() {
        FoodProperties food = foodComponent();
        return food != null ? food.nutrition() : 0;
    }

    @Unique
    @Override
    public float getSaturation() {
        FoodProperties food = foodComponent();
        return food != null ? food.saturation() : 0.0f;
    }

    @Unique
    @Override
    public String getTranslationKey() {
        return ((Item) (Object) this).getDescriptionId();
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return BuiltInRegistries.ITEM.getKey((Item) (Object) this).getPath();
    }

    @Unique
    @Override
    public me.deftware.client.framework.item.Item getItem() {
        return this;
    }

}
