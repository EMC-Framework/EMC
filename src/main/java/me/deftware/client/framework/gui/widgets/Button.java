package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.gui.widgets.properties.Nameable;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.util.function.Function;

/**
 * @author Deftware
 */
@SuppressWarnings("ConstantConditions")
public interface Button extends Component, Nameable<Button>, Tooltipable<Button> {

	/**
	 * Creates a new button instance
	 *
	 * @param id The button ID
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param widthIn The width of the button
	 * @param heightIn The height of the button
	 * @param buttonText The label of the button
	 * @param shouldPlaySound If the button should player a sound when pressed
	 * @param onClick Button click handler
	 * @return A button component instance
	 */
	static Button create(int id, int x, int y, int widthIn, int heightIn, ChatMessage buttonText, boolean shouldPlaySound, Function<Integer, Boolean> onClick) {
		GuiButton widget = new GuiButton(id, x, y, widthIn, heightIn, buttonText.toString(true)) {

			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				if (super.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
					if (shouldPlaySound)
						this.playPressSound(Minecraft.getMinecraft().getSoundHandler());
					onClick.apply(0);
				}
			}

		};
		return (Button) widget;
	}

}
