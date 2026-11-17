package me.deftware.mixin.mixins.gui;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import me.deftware.client.framework.command.CommandRegister;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.SharedSuggestionProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Deftware
 */
@Mixin(CommandSuggestions.class)
public class MixinChatSuggestion {

    @Shadow
    @Final
    EditBox input;

    @Shadow @Final
    Screen screen;

    @Redirect(method = "updateCommandInfo", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;peek()C", remap = false))
    private char onPeek(StringReader stringReader) {
        String trigger = CommandRegister.getCommandTrigger();
        if (input.getValue().startsWith(trigger) && isDispatcherActive()) {
            if (trigger.length() > 1) {
                // Skip trigger length minus one, since
                // Minecraft already skips one character for /
                for (int i = 1; i < trigger.length(); i++) {
                    stringReader.skip();
                }
            }
            return '/';
        }
        return stringReader.peek();
    }

    @Redirect(method = "updateCommandInfo", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;parse(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)Lcom/mojang/brigadier/ParseResults;", remap = false))
    private ParseResults<SharedSuggestionProvider> onParse(CommandDispatcher<SharedSuggestionProvider> commandDispatcher, StringReader reader, Object source) {
        ClientSuggestionProvider clientCommandSource = (ClientSuggestionProvider) source;
        if (input.getValue().startsWith(CommandRegister.getCommandTrigger()) && isDispatcherActive()) {
            return CommandRegister.getDispatcher().parse(reader, clientCommandSource);
        }
        return commandDispatcher.parse(reader, clientCommandSource);
    }

    @Unique
    private boolean isDispatcherActive() {
        return this.screen instanceof ChatScreen;
    }

}
