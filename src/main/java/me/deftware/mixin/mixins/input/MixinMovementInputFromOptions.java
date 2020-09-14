package me.deftware.mixin.mixins.input;

import me.deftware.client.framework.event.events.EventSlowdown;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MovementInputFromOptions.class)
public class MixinMovementInputFromOptions {

    @Redirect(method = "updatePlayerMoveState", at = @At(value = "FIELD", target = "Lnet/minecraft/util/MovementInputFromOptions;sneak:Z", opcode = 180))
    private boolean isSneaking(MovementInputFromOptions self) {
        EventSlowdown event = new EventSlowdown(EventSlowdown.SlowdownType.Sneak);
        event.broadcast();
        if (event.isCanceled()) {
            return false;
        }
        return Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown();
    }

}
