package me.deftware.mixin.mixins.gui;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import me.deftware.client.framework.command.CommandRegister;
import net.minecraft.client.gui.GuiChat;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.ISuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiChat.class)
public abstract class MixinChatSuggestion  {

	@Shadow
	protected GuiTextField inputField;

	@Redirect(method = "updateSuggestion", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;peek()C", remap = false))
	private char onPeek(StringReader stringReader) {
		String trigger = CommandRegister.getCommandTrigger();
		if (inputField.getText().startsWith(trigger)) {
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

	@Redirect(method = "updateSuggestion", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetHandlerPlayClient;func_195515_i()Lcom/mojang/brigadier/CommandDispatcher;"))
	private CommandDispatcher<ISuggestionProvider> onGetDispatcher(NetHandlerPlayClient netHandlerPlayClient) {
		if (inputField.getText().startsWith(CommandRegister.getCommandTrigger())) {
			return CommandRegister.getDispatcher();
		}
		return netHandlerPlayClient.func_195515_i();
	}

}
