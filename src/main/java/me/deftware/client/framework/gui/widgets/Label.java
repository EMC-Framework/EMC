package me.deftware.client.framework.gui.widgets;

import lombok.Getter;
import me.deftware.client.framework.message.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @since 17.0.0
 * @author Deftware
 */
public class Label implements Renderable, GuiEventListener, GenericComponent, Tooltipable {

	private List<Component> text;
	private final Font textRenderer = Minecraft.getInstance().font;

	@Getter
	private int width, height, x, y;

	public Label(int x, int y, Message... text) {
		this.setText(text);
		this.x = x;
		this.y = y;
	}

	public void setText(Message... text) {
		this.text = Arrays.stream(text)
				.map(Component.class::cast)
				.collect(Collectors.toList());
		this.width = this.text.stream()
				.map(textRenderer::width)
				.max(Comparator.naturalOrder())
				.orElseThrow(() -> new RuntimeException("Found no text, this shouldn't happen!"));
		this.height = text.length * textRenderer.lineHeight;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor matrices, int mouseX, int mouseY, float delta) {
		int x = this.x, y = this.y;
		for (Component line : this.text) {
			int center = x + this.width / 2 - textRenderer.width(line) / 2;
			matrices.text(textRenderer, line, center, y, 0xFFFFFFFF);
			y += textRenderer.lineHeight;
		}
	}

	private final List<FormattedCharSequence> tooltipComponents = new ArrayList<>();

	@Override
	public List<FormattedCharSequence> getTooltipComponents(int mouseX, int mouseY) {
		return this.tooltipComponents;
	}

	@Override
	public boolean isMouseOverComponent(int mouseX, int mouseY) {
		return mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
	}

	@Override
	public void setFocused(boolean focused) { }

	@Override
	public boolean isFocused() {
		return false;
	}
}
