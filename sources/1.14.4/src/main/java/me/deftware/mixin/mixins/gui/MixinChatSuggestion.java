package me.deftware.mixin.mixins.gui;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import me.deftware.client.framework.command.CommandRegister;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.server.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatScreen.class)
public abstract class MixinChatSuggestion  {

	@Shadow
	protected TextFieldWidget chatField;

	@Redirect(method = "updateCommand", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;peek()C", remap = false))
	private char onPeek(StringReader stringReader) {
		String trigger = CommandRegister.getCommandTrigger();
		if (chatField.getText().startsWith(trigger)) {
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

	@Redirect(method = "updateCommand", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;parse(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)Lcom/mojang/brigadier/ParseResults;", remap = false))
	private ParseResults<CommandSource> onParse(CommandDispatcher<CommandSource> commandDispatcher, StringReader reader, Object source) {
		ClientCommandSource clientCommandSource = (ClientCommandSource) source;
		if (chatField.getText().startsWith(CommandRegister.getCommandTrigger())) {
			return CommandRegister.getDispatcher().parse(reader, clientCommandSource);
		}
		return commandDispatcher.parse(reader, clientCommandSource);
	}

}
