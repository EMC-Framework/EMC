package me.deftware.mixin.mixins.gui;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import me.deftware.client.framework.command.CommandRegister;
import me.deftware.mixin.imp.IMixinChatSuggestion;
import net.minecraft.client.Minecraft;
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
public abstract class MixinChatSuggestion implements IMixinChatSuggestion {

    @Shadow
    private boolean completingSuggestion;
    
    @Shadow
    protected TextFieldWidget chatField;
    
    @Shadow
    private ParseResults<ServerCommandSource> parseResults;
    
    @Shadow
    private CompletableFuture<Suggestions> suggestionsFuture;

    @Shadow
    protected abstract void updateCommandFeedback();

    private boolean inject = false;

    @Override
    public void setInject(boolean status) {
        inject = status;
    }

    @Inject(method = "updateCommand", at = @At("RETURN"), cancellable = true)
    public void refresh(CallbackInfo ci) {
        if (inject) {
            String text = this.chatField.getText();
            StringReader reader = new StringReader(text);
            if (reader.canRead() && text.startsWith(CommandRegister.getCommandTrigger())) {
                for (int triggerLength = 0; triggerLength < Math.min(CommandRegister.getCommandTrigger().length(), text.length()); triggerLength++) {
                    reader.skip();
                }
                CommandDispatcher<ServerCommandSource> dispatcher = CommandRegister.getDispatcher();
                this.parseResults = dispatcher.parse(reader, net.minecraft.client.Minecraft.getInstance().player.getCommandSource());
                if (!this.completingSuggestion) {
                    StringReader subReader = new StringReader(text.substring(0, Math.min(text.length(), this.chatField.getCursor())));
                    if (subReader.canRead()) {
                        for (int triggerLength = 0; triggerLength < CommandRegister.getCommandTrigger().length(); triggerLength++) {
                            subReader.skip();
                        }
                        ParseResults<ServerCommandSource> parseResults = dispatcher.parse(subReader, net.minecraft.client.Minecraft.getInstance().player.getCommandSource());
                        this.suggestionsFuture = dispatcher.getCompletionSuggestions(parseResults);
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
    
}
