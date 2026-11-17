package me.deftware.mixin.mixins.world;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.event.events.EventEntityUpdated;
import me.deftware.client.framework.event.events.EventWorldLoad;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
@Mixin(ClientLevel.class)
public abstract class MixinWorldClient extends MixinWorld implements me.deftware.client.framework.world.ClientWorld {

    @Shadow
    public abstract void addParticle(ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);

    @Unique
    private final Int2ObjectMap<Entity> entities = new Int2ObjectOpenHashMap<>();

    @SuppressWarnings("ConstantConditions")
    @ModifyVariable(method = "doAnimateTick", at = @At("TAIL"))
    private BlockPos.MutableBlockPos onGetBlockParticle(BlockPos.MutableBlockPos pos) {
        boolean barrier = GameMap.INSTANCE.get(GameKeys.FULL_BARRIER_TEXTURE, false),
                light = GameMap.INSTANCE.get(GameKeys.FULL_LIGHT_TEXTURE, false);
        if (barrier || light) {
            BlockState blockState = ((Level) (Object) this).getBlockState(pos);
            Block block = blockState.getBlock();
            ParticleOptions effect = new BlockParticleOption(ParticleTypes.BLOCK_MARKER, blockState);
            if (barrier && block == Blocks.BARRIER || light && block == Blocks.LIGHT)
                this.addParticle(effect, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
        }
        return pos;
    }

    @Inject(method = "addEntity", at = @At("TAIL"))
    private void addEntityPrivate(net.minecraft.world.entity.Entity entity, CallbackInfo ci) {
        Entity e = Entity.newInstance(entity);
        entities.put(e.getEntityId(), e);
        new EventEntityUpdated(EventEntityUpdated.Change.Added, e).broadcast();
    }

    @Inject(method = "removeEntity", at = @At("TAIL"))
    public void removeEntity(int entityId, net.minecraft.world.entity.Entity.RemovalReason reason, CallbackInfo ci) {
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
        ((ClientLevel) (Object) this).addEntity(entity.getMinecraftEntity());
    }

    @Override
    public void _removeEntity(int id) {
        ((ClientLevel) (Object) this).removeEntity(id, net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> @Nullable T getEntityByReference(net.minecraft.world.entity.Entity reference) {
        if (reference != null) {
            return (T) entities.get(reference.getId());
        }
        return null;
    }

    @Override
    public float getTickRate() {
        var manager = ((ClientLevel) (Object) this).tickRateManager();
        if (manager.isFrozen()) {
            return 0;
        }
        return manager.tickrate();
    }

}
