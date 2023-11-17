package me.deftware.mixin.mixins.item;

import net.minecraft.item.ItemTool;
import me.deftware.client.framework.item.items.AttackItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemTool.class)
public class MixinItemTool extends MixinItem implements AttackItem {

    @Shadow
    protected float attackDamage;

    @Unique
    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

}
