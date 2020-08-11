package me.deftware.mixin.mixins;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import me.deftware.client.framework.command.CommandRegister;
import me.deftware.mixin.imp.IMixinGuiTextField;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ingame.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(ChatScreen.class)
public abstract class MixinGuiChat {

    @Shadow
    protected TextFieldWidget chatField;

    @Shadow
    private ParseResults<ServerCommandSource> parseResults;

    @Shadow
    private boolean completingSuggestion;

    @Shadow
    private CompletableFuture<Suggestions> suggestionsFuture;

    @Shadow protected abstract void updateCommandFeedback();

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        ((IMixinGuiTextField) chatField).setOverlay(true);
    }

    @Inject(method = "updateCommand", at = @At("RETURN"))
    public void updateCommandAfter(CallbackInfo ci) {
        String string_1 = this.chatField.getText();
        StringReader stringReader_1 = new StringReader(string_1);
        if (stringReader_1.canRead() && string_1.startsWith(CommandRegister.getCommandTrigger())) {
            for (int triggerLength = 0; triggerLength < Math.min(CommandRegister.getCommandTrigger().length(), string_1.length()); triggerLength++) {
                stringReader_1.skip();
            }
            CommandDispatcher<ServerCommandSource> commandDispatcher_1 = CommandRegister.getDispatcher();
            this.parseResults = commandDispatcher_1.parse(stringReader_1, MinecraftClient.getInstance().player.getCommandSource());
            if (!this.completingSuggestion) {
                StringReader stringReader_2 = new StringReader(string_1.substring(0, Math.min(string_1.length(), this.chatField.getCursor())));
                if (stringReader_2.canRead()) {
                    for (int triggerLength = 0; triggerLength < CommandRegister.getCommandTrigger().length(); triggerLength++) {
                        stringReader_2.skip();
                    }
                    ParseResults<ServerCommandSource> parseResults_1 = commandDispatcher_1.parse(stringReader_2, MinecraftClient.getInstance().player.getCommandSource());
                    this.suggestionsFuture = commandDispatcher_1.getCompletionSuggestions(parseResults_1);
                    this.suggestionsFuture.thenRun(() -> {
                        if (this.suggestionsFuture.isDone()) {
                            this.updateCommandFeedback();
                        }
                    });
                }
            }
        }

    }


}
