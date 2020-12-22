package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityList.class)
public class MixinEntityType {

    @SuppressWarnings("unchecked")
    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/RegistryNamespaced;register(ILjava/lang/Object;Ljava/lang/Object;)V", opcode = 182))
    private static <K extends ResourceLocation, V extends Class<? extends Entity>> void registerRedirect(RegistryNamespaced<K, V> registryNamespaced, int id, Object key, Object value) {
        registryNamespaced.register(id, (K) key, (V) value);
        EntityRegistry.INSTANCE.register(key.toString(), (V) value, (K) key);
    }

}
