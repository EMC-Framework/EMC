package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.item.items.AttackItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolItem.class)
public class MixinItemTool extends MixinItem implements AttackItem {

    @Final
    @Shadow
    private ToolMaterial material;

    @Unique
    @Override
    public float getAttackDamage() {
        return material.getAttackDamage();
    }

}
