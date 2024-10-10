package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityType.class)
public class MixinEntityType {

    @Redirect(method = "register(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType$Builder;build(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/entity/EntityType;", opcode = 182))
    private static <T extends Entity> EntityType<T> registerRedirect(EntityType.Builder<T> instance, RegistryKey<EntityType<?>> registryKey) {
        var type = instance.build(registryKey);
        var id = registryKey.getValue().getPath();
        EntityRegistry.INSTANCE.register(id, type);
        return type;
    }

}
