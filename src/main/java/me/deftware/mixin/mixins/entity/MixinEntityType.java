package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityType.class)
public class MixinEntityType {

    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/IRegistry;put(Lnet/minecraft/util/ResourceLocation;Ljava/lang/Object;)V", opcode = 182))
    private static <T> void registerRedirect(IRegistry<T> iRegistry, ResourceLocation key, T entityType) {
        iRegistry.put(key, entityType);
        EntityRegistry.INSTANCE.register(key.toString(), (EntityType<?>) entityType);
    }

}
