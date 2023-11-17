package me.deftware.client.framework.gui.widgets;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.widgets.properties.Nameable;
import me.deftware.client.framework.gui.widgets.properties.Tooltipable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.ITextComponent;

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
		GuiButton widget = new GuiButton(0, x, y, widthIn, heightIn, ((ITextComponent) buttonText).getFormattedText()) {

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

	@Override
	default boolean isMouseOverComponent(int mouseX, int mouseY) {
		return mouseX > this.getPositionX() && mouseX < this.getPositionX() + this.getComponentWidth() && mouseY > this.getPositionY() && mouseY < this.getPositionY() + this.getComponentHeight();
	}

	void click();

}
