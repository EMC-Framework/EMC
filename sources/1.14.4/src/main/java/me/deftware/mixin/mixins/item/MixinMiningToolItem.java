package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.item.items.AttackItem;
import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MiningToolItem.class)
public class MixinMiningToolItem extends MixinItemTool implements AttackItem {

    @Shadow
    @Final
    private float attackDamage;

    @Unique
    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

}
