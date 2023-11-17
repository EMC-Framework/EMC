package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.widgets.properties.Nameable;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import net.minecraft.client.gui.widget.ButtonWidget;
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
		var button =
				ButtonWidget.builder((Text) buttonText, btn -> onClick.apply(0))
						.dimensions(x, y, widthIn, heightIn)
						.build();
		return (Button) button;
	}

	void click();

}
