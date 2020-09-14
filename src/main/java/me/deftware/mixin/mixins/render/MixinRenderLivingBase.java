package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventRenderPlayerModel;
import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLivingBase.class)
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> {

    @Inject(method = "isVisible", at = @At("HEAD"), cancellable = true)
    private void isVisible(T entity, CallbackInfoReturnable<Boolean> ci) {
        EventRenderPlayerModel event = new EventRenderPlayerModel(entity);
        event.broadcast();
        if (event.isShouldRender()) {
            ci.setReturnValue(true);
        }
    }

    @Inject(method = "renderLivingAt", at = @At("RETURN"))
    protected void setupTransforms(T entity, double x, double y, double z, CallbackInfo ci) {
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        String s = ((EntityPlayer) entity).getGameProfile().getName(); //TextFormatting.getTextWithoutFormattingCodes(((EntityPlayer) entityLivingBaseIn).getGameProfile().getName());
        String names = (String) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "FLIP_USERNAMES", "");
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
