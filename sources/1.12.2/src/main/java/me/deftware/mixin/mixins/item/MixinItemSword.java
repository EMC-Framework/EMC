package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.item.items.AttackItem;
import net.minecraft.item.ItemSword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemSword.class)
public class MixinItemSword extends MixinItem implements AttackItem {

    @Unique
    @Override
    public float getAttackDamage() {
        return ((ItemSword) (Object) this).getAttackDamage();
    }

}
