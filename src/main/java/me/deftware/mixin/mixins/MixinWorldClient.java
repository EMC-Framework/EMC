package me.deftware.mixin.mixins;

import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.wrappers.entity.IEntity;
import me.deftware.mixin.imp.IMixinWorldClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(WorldClient.class)
public class MixinWorldClient implements IMixinWorldClient {

    @Unique
    private final HashMap<Integer, IEntity> entities = new HashMap<>();

    /*@ModifyVariable(method = "showBarrierParticles", at = @At("HEAD"))
    public boolean isHoldingBarrierBlock(boolean holdingBarrier) {
        if ((boolean) SettingsMap.getValue(SettingsMap.MapKeys.BLOCKS, "render_barrier_blocks", false)) {
            return true;
        }
        return holdingBarrier;
    }*/

    @Inject(method = "spawnEntityInWorld", at = @At("TAIL"))
    public void spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        entities.put(entity.getEntityId(), IEntity.fromEntity(entity));
    }

    @Inject(method = "removeEntity", at = @At("TAIL"))
    public void removeEntity(Entity entity, CallbackInfo ci) {
        entities.remove(entity.getEntityId());
    }

    @Override
    public HashMap<Integer, IEntity> getIEntities() {
        return entities;
    }

}
