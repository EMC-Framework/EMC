package me.deftware.mixin.mixins;

import me.deftware.mixin.imp.IMixinSignBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SignBlockEntity.class)
public class MixinSignBlockEntity implements IMixinSignBlockEntity {

    @Shadow
    @Final
    private Text[] text;

    @Override
    public Text[] getTextRows() { return text; }
}
