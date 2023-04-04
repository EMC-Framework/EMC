package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.gui.Drawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import net.minecraft.client.gui.IGuiEventListener;
import me.deftware.client.framework.message.Message;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @since 17.0.0
 * @author Deftware
 */
public class Label implements Drawable, IGuiEventListener, GenericComponent, Tooltipable {

	private List<String> text;
	private int width, height, x, y;
	private final FontRenderer textRenderer = Minecraft.getInstance().fontRenderer;

	public Label(int x, int y, Message... text) {
		this.setText(text);
		this.x = x;
		this.y = y;
	}

	public void setText(Message... text) {
		this.text = Arrays.stream(text)
				.map(ITextComponent.class::cast)
				.map(ITextComponent::getFormattedText)
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

	private final List<String> tooltipComponents = new ArrayList<>();

	@Override
	public List<String> getTooltipComponents(int mouseX, int mouseY) {
		return this.tooltipComponents;
	}

	@Override
	public boolean isMouseOverComponent(int mouseX, int mouseY) {
		return mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
