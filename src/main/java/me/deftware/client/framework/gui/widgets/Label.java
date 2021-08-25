package me.deftware.client.framework.gui.widgets;

import lombok.Getter;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @since 17.0.0
 * @author Deftware
 */
public class Label implements Drawable, Element, GenericComponent, Tooltipable {

	private List<String> text;
	private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

	@Getter
	private int width, height, x, y;

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
		this.height = text.length * textRenderer.fontHeight;
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		int x = this.x, y = this.y;
		for (String line : this.text) {
			int center = x + this.width / 2 - textRenderer.getStringWidth(line) / 2;
			textRenderer.drawWithShadow(line, center, y, 0xFFFFFF);
			y += textRenderer.fontHeight;
		}
	}

	private final List<String> tooltipComponents = new ArrayList<>();

	@Override
	public List<String> getTooltipComponents(int mouseX, int mouseY) {
		return this.tooltipComponents;
	}

	@Override
	public boolean isMouseOverComponent(int mouseX, int mouseY) {
		return mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
	}

}
