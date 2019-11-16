package me.deftware.mixin.mixins;

import me.deftware.mixin.imp.IMixinChatSuggestion;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.CommandSuggestor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
 public class MixinGuiChat {

    @Shadow
    public CommandSuggestor commandSuggestor;

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        ((IMixinChatSuggestion) commandSuggestor).setInject(true);
    }

}