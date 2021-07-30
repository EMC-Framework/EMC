package me.deftware.client.framework.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

import java.util.function.Predicate;

/**
 * @author Deftware
 */
@SuppressWarnings("ConstantConditions")
public interface TextField extends Component {

	/**
	 * Creates a new TextField instance
	 *
	 * @param id Unique ID
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param width The width
	 * @param height The height
	 * @return A TextField instance
	 */
	static TextField create(int id, int x, int y, int width, int height) {
		return (TextField) new GuiTextField(id, Minecraft.getMinecraft().fontRenderer, x, y, width, height);
	}

	/**
	 * Sets the current text
	 *
	 * @param text Some text
	 */
	void _setText(String text);

	/**
	 * @return The current text
	 */
	String _getText();

	/**
	 * Toggles password mode, which renders
	 * text written with asterisks
	 */
	void _setPasswordMode(boolean state);

	/**
	 * Sets the max text length
	 */
	void _setMaxLength(int length);

	/**
	 * @param text Text to be drawn on top of the textbox
	 */
	void _setOverlay(String text);

	/**
	 * Sets a text predicate
	 *
	 * @param predicate A string predicate
	 */
	void _setPredicate(Predicate<String> predicate);

}
