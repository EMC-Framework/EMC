package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityType.class)
public class MixinEntityType {

    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;register(Lnet/minecraft/registry/Registry;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", opcode = 182))
    private static <T> T registerRedirect(Registry<? super T> registry, String id, T entry) {
        T item = Registry.register(registry, id, entry);
        EntityType<?> type = (EntityType<?>) item;
        EntityRegistry.INSTANCE.register(id, type);
        return item;
    }

}
