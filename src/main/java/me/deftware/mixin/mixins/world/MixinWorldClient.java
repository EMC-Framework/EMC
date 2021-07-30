package me.deftware.mixin.mixins.world;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.event.events.EventEntityUpdated;
import me.deftware.client.framework.event.events.EventWorldLoad;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.world.classifier.BlockClassifier;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

/**
 * @author Deftware
 */
@Mixin(WorldClient.class)
public abstract class MixinWorldClient extends MixinWorld implements me.deftware.client.framework.world.ClientWorld {

    @Unique
    private final Int2ObjectMap<Entity> entities = new Int2ObjectOpenHashMap<>();

    @ModifyVariable(method = "animateTick(IIIILjava/util/Random;ZLnet/minecraft/util/math/BlockPos$MutableBlockPos;)V", at = @At("HEAD"))
    public boolean randomBlockDisplayTick(boolean p_animateTick_6_) {
        if (GameMap.INSTANCE.get(GameKeys.FULL_BARRIER_TEXTURE, false))
            return true;
        return p_animateTick_6_;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructed(NetHandlerPlayClient p_i49845_1_, WorldSettings p_i49845_2_, DimensionType p_i49845_3_, EnumDifficulty p_i49845_4_, Profiler p_i49845_5_, CallbackInfo ci) {
        new EventWorldLoad().broadcast();
        BlockClassifier.getClassifiers().forEach(blockClassifier -> blockClassifier.getClassifiedBlocks().clear());
    }

    @Inject(method = "spawnEntity", at = @At("TAIL"))
    private void addEntityPrivate(net.minecraft.entity.Entity entity, CallbackInfoReturnable<Boolean> cir) {
        Entity e = Entity.newInstance(entity);
        entities.put(entity.getEntityId(), e);
        new EventEntityUpdated(EventEntityUpdated.Change.Added, e).broadcast();
    }

    @Inject(method = "removeEntityFromWorld", at = @At("TAIL"))
    public void removeEntity(int entityId, CallbackInfoReturnable<net.minecraft.entity.Entity> ci) {
        new EventEntityUpdated(EventEntityUpdated.Change.Removed, entities.remove(entityId)).broadcast();
    }

    @Override
    public Stream<Entity> getLoadedEntities() {
        return entities.values().stream();
    }

    @Override
    public Entity _getEntityById(int id) {
        return entities.get(id);
    }

    @Override
    public void _addEntity(int id, Entity entity) {
        ((WorldClient) (Object) this).addEntityToWorld(id, entity.getMinecraftEntity());
    }

    @Override
    public void _removeEntity(int id) {
        ((WorldClient) (Object) this).removeEntityFromWorld(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> @Nullable T getEntityByReference(net.minecraft.entity.Entity reference) {
        if (reference != null) {
            return (T) entities.get(reference.getEntityId());
        }
        return null;
    }

    @Override
    public Int2ObjectMap<Entity> getEntities() {
        return entities;
    }

}
