package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventRenderPlayerModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T> {

    @Shadow
    protected boolean renderOutlines;

    @Unique
    private final EventRenderPlayerModel eventRenderPlayerModel = new EventRenderPlayerModel();

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(renderManager);
    }

    private boolean isVisible(T entity) {
        eventRenderPlayerModel.create(entity);
        eventRenderPlayerModel.broadcast();
        if (eventRenderPlayerModel.isShouldRender()) {
            return true;
        }
        return !entity.isInvisible() || this.renderOutlines;
    }

    @Inject(method = "renderLivingAt", at = @At("RETURN"))
    protected void setupTransforms(T entity, double x, double y, double z, CallbackInfo ci) {
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        String s = ((EntityPlayer) entity).getGameProfile().getName(); //TextFormatting.getTextWithoutFormattingCodes(((EntityPlayer) entityLivingBaseIn).getGameProfile().getName());
        String names = GameMap.INSTANCE.get(GameKeys.FLIP_USERNAMES, "");
        if (s != null && !names.equals("")) {
            boolean flip = false;
            if (names.contains(",")) {
                for (String name : names.split(",")) {
                    if (name.equals(s)) {
                        flip = true;
                        break;
                    }
                }
            } else {
                flip = names.equals(s);
            }
            if (flip) {
                GlStateManager.translate(0.0F, entity.height + 0.1F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }


}
