package me.deftware.client.framework.gui;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import me.deftware.client.framework.global.GameCategory;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @author Deftware
 */
@ApiStatus.Internal
public class ChatSuggestion<T> {

	private final Supplier<T> commandSource;
	private final CommandDispatcher<T> dispatcher;
	private final GuiScreen parentScreen;
	private final GuiTextField textField;
	private final String trigger;

	private CompletableFuture<Suggestions> pendingSuggestions;
	private ParseResults<T> parse;

	private final Window window;

	private boolean suggestingWhenEmpty = true, completingSuggestions = false;

	public ChatSuggestion(CommandDispatcher<T> dispatcher, Supplier<T> commandSource, String trigger, GuiTextField textField, GuiScreen parentScreen) {
		this.dispatcher = dispatcher;
		this.commandSource = commandSource;
		this.trigger = trigger;
		this.textField = textField;
		this.parentScreen = parentScreen;
		this.window = new Window();
	}

	public void updateSuggestion(String text) {
		if (this.parse != null && !this.parse.getReader().getString().equals(text)) {
			this.parse = null;
		}

		StringReader stringReader = new StringReader(text);
		if (stringReader.canRead() && text.startsWith(trigger)) {
			for (int i = 0; i < trigger.length(); i++)
				stringReader.skip();

			if (this.parse == null) {
				this.parse = dispatcher.parse(stringReader, this.commandSource.get());
			}

			int j = this.suggestingWhenEmpty ? stringReader.getCursor() : 1;
			if (textField.getCursorPosition() >= j && !this.completingSuggestions) {
				this.pendingSuggestions = dispatcher.getCompletionSuggestions(this.parse, textField.getCursorPosition());
				this.pendingSuggestions.thenRun(() -> {
					if (this.pendingSuggestions.isDone()) {
						this.show();
					}
				});
			}
		} else {
			this.window.clear();
		}
	}

	private void show() {
		if (this.textField.getCursorPosition() == this.textField.getText().length()) {
			this.window.setSuggestions(pendingSuggestions.join().getList());
		}
	}

	public Window getWindow() {
		return window;
	}

	public class Window implements Drawable {

		private final List<Suggestion> suggestions = new ArrayList<>();

		private final int maxItems = GameMap.INSTANCE.get(GameCategory.Render, "max_brigadier_results", 5);
		private int selectedIndex = 0;

		public void setSuggestions(List<Suggestion> list) {
			this.clear();
			this.suggestions.addAll(list);
		}

		public void clear() {
			this.selectedIndex = 0;
			this.suggestions.clear();
		}

		@Override
		public void onRender(int mouseX, int mouseY, float delta) {
			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
			String text = textField.getText();
			int textboxHeight = 12, x = 4, y = parentScreen.height - textboxHeight;
			if (text.length() > 0 && !this.suggestions.isEmpty()) {
				int start = 0, end = this.suggestions.size();
				if (end > maxItems) {
					start = MathHelper.clamp(this.selectedIndex, 0, this.suggestions.size() - maxItems);
					end = Math.min(this.selectedIndex + maxItems, this.suggestions.size());
				}
				int size = Math.min(maxItems, this.suggestions.size());
				// Draw background
				int relativePosition = x;
				List<ParsedCommandNode<T>> nodes = parse.getContext().getNodes();
				for (ParsedCommandNode<T> node : nodes) {
					StringRange range = node.getRange();
					relativePosition += fontRenderer.getStringWidth(text.substring(range.getStart(), range.getEnd()) + " ");
				}
				Gui.drawRect(relativePosition - 2, y - size * fontRenderer.FONT_HEIGHT - 6, relativePosition + 100, y - 2, -2147483648);
				// Draw text
				y -= textboxHeight;
				for (int i = start; i < end; i++) {
					boolean selected = i == selectedIndex;
					Suggestion localSuggestion = this.suggestions.get(i);
					fontRenderer.drawStringWithShadow(localSuggestion.getText(), relativePosition, y, selected ? Color.yellow.getRGB() : Color.white.getRGB());
					y -= fontRenderer.FONT_HEIGHT;
				}
				// Draw overlay
				Suggestion suggestion = this.suggestions.get(this.selectedIndex);
				String overlay = suggestion.apply(text).substring(text.length());
				fontRenderer.drawStringWithShadow(
					overlay, x + fontRenderer.getStringWidth(text), parentScreen.height - 12, Color.gray.getRGB()
				);
			}
		}

		private void setSelectedIndex(int offset) {
			this.selectedIndex = MathHelper.clamp(this.selectedIndex + offset, 0, this.suggestions.size() - 1);
		}

		public boolean onKeyTyped(char typedChar, int keyCode) {
			if (!this.suggestions.isEmpty()) {
				if (keyCode == Keyboard.KEY_UP || keyCode == Keyboard.KEY_DOWN) {
					setSelectedIndex(
							keyCode == Keyboard.KEY_UP ? 1 : -1
					);
					return true;
				} else if (keyCode == Keyboard.KEY_TAB) {
					Suggestion suggestion = this.suggestions.get(this.selectedIndex);
					textField.setText(
							suggestion.apply(textField.getText())
					);
				}
			}
			return false;
		}

	}

}
