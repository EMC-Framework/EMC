package me.deftware.mixin.mixins.input;

import com.mojang.blaze3d.platform.InputConstants;
import me.deftware.mixin.imp.IMixinKeyBinding;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyMapping.class)
public class MixinKeyBinding implements IMixinKeyBinding {

    @Shadow
    private boolean isDown;

    @Shadow
    private InputConstants.Key key;

    @Override
    public void emcSetPressed(boolean state) {
        isDown = state;
    }

    @Override
    public InputConstants.Key getInput() {
        return key;
    }


}
