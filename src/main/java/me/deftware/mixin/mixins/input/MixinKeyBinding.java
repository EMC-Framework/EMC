package me.deftware.mixin.mixins.input;

import me.deftware.mixin.imp.IMixinKeyBinding;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public class MixinKeyBinding implements IMixinKeyBinding {

    @Shadow
    private boolean pressed;

    @Shadow
    private int keyCode;

    @Override
    public void setPressed(boolean state) {
        pressed = state;
    }

    @Override
    public int getKey() {
        return keyCode;
    }

}