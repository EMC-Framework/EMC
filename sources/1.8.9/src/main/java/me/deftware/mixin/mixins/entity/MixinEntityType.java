package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityList.class)
public class MixinEntityType {

    @Inject(method = "addMapping(Ljava/lang/Class;Ljava/lang/String;I)V", at = @At("RETURN"))
    private static void addMapping(Class<? extends Entity> entityClass, String entityName, int id, CallbackInfo ci) {
        EntityRegistry.INSTANCE.register(entityName, entityClass, entityName);
    }

}
