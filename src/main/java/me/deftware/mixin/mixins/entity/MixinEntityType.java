package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.registry.EntityRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.world.entity.EntityTypes.class)
public class MixinEntityType {

    @Redirect(method = "register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType$Builder;build(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/entity/EntityType;", opcode = 182))
    private static <T extends Entity> EntityType<T> registerRedirect(EntityType.Builder<T> instance, ResourceKey<EntityType<?>> registryKey) {
        var type = instance.build(registryKey);
        var id = registryKey.identifier().getPath();
        EntityRegistry.INSTANCE.register(id, type);
        return type;
    }

}
