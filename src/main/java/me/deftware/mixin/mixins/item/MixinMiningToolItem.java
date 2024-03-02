package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.items.AttackItem;
import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MiningToolItem.class)
public class MixinMiningToolItem extends MixinItemTool implements AttackItem {

    @Unique
    @Override
    public float getAttackDamage() {
        return Item.damage(this);
    }

}
