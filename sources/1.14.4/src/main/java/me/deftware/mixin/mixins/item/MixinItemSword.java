package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.item.items.AttackItem;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SwordItem.class)
public class MixinItemSword extends MixinItem implements AttackItem {

    @Unique
    @Override
    public float getAttackDamage() {
        return ((SwordItem) (Object) this).getAttackDamage();
    }

}
