package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.gui.Drawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @since 17.0.0
 * @author Deftware
 */
public class Label implements Drawable, GenericComponent {

	private List<String> text;
	private int width, height, x, y;
	private final FontRenderer textRenderer = Minecraft.getMinecraft().fontRendererObj;

	public Label(int x, int y, ChatMessage... text) {
		this.setText(text);
		this.x = x;
		this.y = y;
	}

	public void setText(ChatMessage... text) {
		this.text = Arrays.stream(text)
				.map(m -> m.toString(true))
				.collect(Collectors.toList());
		this.width = this.text.stream()
				.map(textRenderer::getStringWidth)
				.max(Comparator.naturalOrder())
				.orElseThrow(() -> new RuntimeException("Found no text, this shouldn't happen!"));
		this.height = text.length * textRenderer.FONT_HEIGHT;
	}

	@Override
	public void onRender(int mouseX, int mouseY, float delta) {
		int x = this.x, y = this.y;
		for (String line : this.text) {
			int center = x + this.width / 2 - textRenderer.getStringWidth(line) / 2;
			textRenderer.drawStringWithShadow(line, center, y, 0xFFFFFF);
			y += textRenderer.FONT_HEIGHT;
		}
	}

}
