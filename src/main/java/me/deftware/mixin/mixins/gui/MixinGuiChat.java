package me.deftware.mixin.mixins.gui;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import me.deftware.client.framework.command.CommandRegister;
import me.deftware.mixin.imp.IMixinGuiTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.command.ISuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat {

    @Shadow
    protected GuiTextField inputField;

    @Shadow
    private ParseResults<ISuggestionProvider> currentParse;

    @Shadow
    private boolean field_212338_z;

    @Shadow
    private CompletableFuture<Suggestions> pendingSuggestions;

    @Shadow protected abstract void updateSuggestion();

    @Shadow protected abstract void updateUsageInfo();

    @Inject(method = "initGui", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        ((IMixinGuiTextField) inputField).setOverlay(true);
    }

    @Inject(method = "updateSuggestion", at = @At("RETURN"))
    public void updateCommandAfter(CallbackInfo ci) {
        String string_1 = this.inputField.getText();
        StringReader stringReader_1 = new StringReader(string_1);
        if (stringReader_1.canRead() && string_1.startsWith(CommandRegister.getCommandTrigger())) {
            for (int triggerLength = 0; triggerLength < Math.min(CommandRegister.getCommandTrigger().length(), string_1.length()); triggerLength++) {
                stringReader_1.skip();
            }
            CommandDispatcher<ISuggestionProvider> commandDispatcher_1 = CommandRegister.getDispatcher();
            this.currentParse = commandDispatcher_1.parse(stringReader_1, Minecraft.getInstance().player.connection.getSuggestionProvider());
            if (!this.field_212338_z) {
                StringReader stringReader_2 = new StringReader(string_1.substring(0, Math.min(string_1.length(), this.inputField.getCursorPosition())));
                if (stringReader_2.canRead()) {
                    for (int triggerLength = 0; triggerLength < CommandRegister.getCommandTrigger().length(); triggerLength++) {
                        stringReader_2.skip();
                    }
                    ParseResults<ISuggestionProvider> parseResults_1 = commandDispatcher_1.parse(stringReader_2, Minecraft.getInstance().player.connection.getSuggestionProvider());
                    this.pendingSuggestions = commandDispatcher_1.getCompletionSuggestions(parseResults_1);
                    this.pendingSuggestions.thenRun(() -> {
                        if (this.pendingSuggestions.isDone()) {
                            this.updateUsageInfo();
                        }
                    });
                }
            }
        }
    }

}