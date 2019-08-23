package me.deftware.mixin.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import me.deftware.client.framework.event.events.EventRenderPlayerModel;
import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinRenderLivingBase<T extends LivingEntity> {

    @Inject(method = "method_4056", at = @At("HEAD"), cancellable = true)
    private void isVisible(T p_193115_1_, CallbackInfoReturnable<Boolean> ci) {
        EventRenderPlayerModel event = new EventRenderPlayerModel(p_193115_1_);
        event.broadcast();
        if (event.isShouldRender()) {
            ci.setReturnValue(true);
        }
    }

    /**
     * @Author Deftware
     * @reason
     */
    @Overwrite
    public void method_4048(T entityLivingBaseIn, double x, double y, double z) {
        RenderSystem.translatef((float) x, (float) y, (float) z);
        if (!(entityLivingBaseIn instanceof PlayerEntity)) {
            return;
        }
        String s = ((PlayerEntity) entityLivingBaseIn).getGameProfile().getName(); //TextFormatting.getTextWithoutFormattingCodes(((EntityPlayer) entityLivingBaseIn).getGameProfile().getName());
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
                RenderSystem.translatef(0.0F, entityLivingBaseIn.getHeight() + 0.1F, 0.0F);
                RenderSystem.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

}
