package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventRenderPlayerModel;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinRenderLivingBase<T extends LivingEntity, S extends LivingEntityRenderState> {

    @Unique
    private final EventRenderPlayerModel eventRenderPlayerModel = new EventRenderPlayerModel();

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("RETURN"))
    private void onExtractRenderState(T entity, S state, float partialTick, CallbackInfo ci) {
        eventRenderPlayerModel.create(entity);
        eventRenderPlayerModel.broadcast();
        if (eventRenderPlayerModel.isShouldRender()) {
            state.isInvisible = false;
            state.isInvisibleToPlayer = false;
        }
        if (entity instanceof Player player) {
            String name = player.getGameProfile().name();
            String names = GameMap.INSTANCE.get(GameKeys.FLIP_USERNAMES, "");
            if (name != null && !names.isEmpty()) {
                boolean flip = false;
                if (names.contains(",")) {
                    for (String entry : names.split(",")) {
                        if (entry.equals(name)) {
                            flip = true;
                            break;
                        }
                    }
                } else {
                    flip = names.equals(name);
                }
                if (flip) {
                    state.isUpsideDown = true;
                }
            }
        }
    }

}
