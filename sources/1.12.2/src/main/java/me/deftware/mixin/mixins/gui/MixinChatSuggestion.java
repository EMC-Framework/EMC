package me.deftware.mixin.mixins.gui;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import me.deftware.client.framework.command.CommandRegister;
import me.deftware.client.framework.command.CustomSuggestionProvider;
import me.deftware.client.framework.gui.ChatSuggestion;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public abstract class MixinChatSuggestion  {

	@Shadow
	protected GuiTextField inputField;

	@Unique
	private ChatSuggestion<SuggestionProvider<?>> suggestionsProvider;

	@Inject(method = "initGui", at = @At("RETURN"))
	private void onInit(CallbackInfo ci) {
		CustomSuggestionProvider provider = new CustomSuggestionProvider();
		this.suggestionsProvider = new ChatSuggestion<>(
				CommandRegister.getDispatcher(), () -> provider, CommandRegister.getCommandTrigger(), inputField, (GuiScreen) (Object) this
		);
	}

	@Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
	private void onKeyType(char typedChar, int keyCode, CallbackInfo ci) {
		if (this.suggestionsProvider.getWindow().onKeyTyped(typedChar, keyCode))
			ci.cancel();
	}

	@Inject(method = "keyTyped", at = @At("RETURN"), cancellable = true)
	private void onKeyTypeReturn(char typedChar, int keyCode, CallbackInfo ci) {
		this.suggestionsProvider.updateSuggestion(inputField.getText());
	}

	@Inject(method = "drawScreen", at = @At("HEAD"))
	private void onDraw(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		this.suggestionsProvider.getWindow().onRender(mouseX, mouseY, partialTicks);
	}

}
