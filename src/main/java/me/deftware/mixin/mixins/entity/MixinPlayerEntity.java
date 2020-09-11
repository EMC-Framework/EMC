package me.deftware.mixin.mixins.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class MixinPlayerEntity {

    @Redirect(method = "adjustMovementForSneaking", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F", opcode = 180))
    private float modifyStepHeight(Entity self) {
        return self == MinecraftClient.getInstance().player ? 0.6f : self.stepHeight;
    }

}
