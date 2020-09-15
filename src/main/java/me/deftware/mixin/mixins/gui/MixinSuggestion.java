package me.deftware.mixin.mixins.gui;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.chat.LiteralChatMessage;
import me.deftware.client.framework.chat.style.ChatColors;
import me.deftware.client.framework.command.CommandRegister;
import me.deftware.client.framework.command.CustomSuggestionProvider;
import me.deftware.client.framework.fonts.minecraft.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(GuiTextField.class)
public abstract class MixinSuggestion {

	@Shadow public abstract String getText();

	@Shadow public int x;

	@Shadow public int y;

	@Shadow @Final private int width;

	@Shadow public abstract void writeText(String textToWrite);

	@Shadow public abstract void setText(String textIn);

	private ParseResults<SuggestionProvider<?>> parse;

	private Suggestions suggestions;

	private String suggestion = "";

	@Inject(method = "drawTextBox", at = @At("RETURN"))
	public void drawTextFieldReturn(CallbackInfo ci) {
		if (suggestions != null && !getText().isEmpty() && getText().startsWith(CommandRegister.getCommandTrigger())) {
			int triggerLength = CommandRegister.getCommandTrigger().length();
			ChatMessage text = null;
			int x = this.x + 4 + FontRenderer.getStringWidth(this.getText().substring(triggerLength));
			if (!this.suggestions.getList().isEmpty()) {
				Suggestion suggestion = this.suggestions.getList().get(0);
				text = new LiteralChatMessage(this.suggestion = suggestion.apply(this.getText().substring(triggerLength)).substring(this.getText().length() - 1), ChatColors.DARK_GRAY);
				FontRenderer.drawStringWithShadow(text, x, y, 0xFFFFFF);
			} else {
				suggestion = "";
			}
		} else {
			suggestion = "";
		}
	}

	@Inject(method = "textboxKeyTyped", at = @At("HEAD"), cancellable = true)
	public void textboxKeyTyped(char typedChar, int keyCode, CallbackInfoReturnable<Boolean> info) {
		if (keyCode == Keyboard.KEY_TAB && !suggestion.isEmpty()) {
			setText(getText() + suggestion);
			updateSuggestions(getText());
			info.cancel();
		}
	}

	@Inject(method = "writeText", at = @At("TAIL"))
	public void textboxKeyTypedInject(String text, CallbackInfo info) {
		updateSuggestions(getText());
	}

	public void updateSuggestions(String text) {
		int triggerLength = CommandRegister.getCommandTrigger().length();
		suggestions = null;
		if (text.length() > triggerLength) {
			StringReader reader = new StringReader(text.substring(triggerLength));
			if (reader.canRead()) {
				this.parse = CommandRegister.getDispatcher().parse(reader, new CustomSuggestionProvider());
				CompletableFuture<Suggestions> pendingSuggestions = CommandRegister.getDispatcher().getCompletionSuggestions(parse);
				pendingSuggestions.thenRun(() -> {
					if (pendingSuggestions.isDone()) {
						suggestions = pendingSuggestions.join();
					}
				});
			}
		}
	}

}
