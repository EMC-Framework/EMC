package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventNametagRender;
import me.deftware.client.framework.render.shader.EntityShader;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinRender<T extends Entity, S extends EntityRenderState> {

    @Unique
    private final EventNametagRender eventNametagRender = new EventNametagRender();

    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void onExtractRenderState(T entity, S state, float partialTick, CallbackInfo ci) {
        int color = EntityShader.getMatchingOutlineColor(entity);
        if (color != 0) {
            state.outlineColor = color;
        }
    }

    /*
     * Nametag rendering hook
     */
    @Inject(method = "extractNameTags(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;FDD)V", at = @At("HEAD"), cancellable = true)
    private void renderEntityLabel(T entity, S state, float partialTick, double maxDist, double maxDistSneaking, CallbackInfo ci) {
        eventNametagRender.create(entity);
        eventNametagRender.broadcast();
        if (eventNametagRender.isCanceled()) {
            ci.cancel();
        }
    }

}
