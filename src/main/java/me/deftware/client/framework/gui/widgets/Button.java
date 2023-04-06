package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.widgets.properties.Nameable;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.function.Function;

/**
 * @author Deftware
 */
@SuppressWarnings("ConstantConditions")
public interface Button extends Component, Nameable<Button>, Tooltipable {

	@Deprecated
	static Button create(int id, int x, int y, int widthIn, int heightIn, Message buttonText, boolean shouldPlaySound, Function<Integer, Boolean> onClick) {
		return create(x, y, widthIn, heightIn, buttonText, shouldPlaySound, onClick);
	}

	/**
	 * Creates a new button instance
	 *
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param widthIn The width of the button
	 * @param heightIn The height of the button
	 * @param buttonText The label of the button
	 * @param shouldPlaySound If the button should player a sound when pressed
	 * @param onClick Button click handler
	 * @return A button component instance
	 */
	static Button create(int x, int y, int widthIn, int heightIn, Message buttonText, boolean shouldPlaySound, Function<Integer, Boolean> onClick) {
		ClickableWidget widget = new ClickableWidget(x, y, widthIn, heightIn, (Text) buttonText) {

			@Override
			public boolean mouseClicked(double mouseX, double mouseY, int button) {
				if (this.clicked(mouseX, mouseY)) {
					if (shouldPlaySound)
						this.playDownSound(MinecraftClient.getInstance().getSoundManager());
					return onClick.apply(button);
				}
				return false;
			}

		};
		return (Button) widget;
	}

	@Override
	default boolean isMouseOverComponent(int mouseX, int mouseY) {
		return ((ClickableWidget) this).isHovered();
	}

}
