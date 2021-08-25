package me.deftware.client.framework.gui.widgets.properties;

import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.text.OrderedText;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * @since 17.0.0
 * @author Deftware
 */
@ApiStatus.Internal
public interface Tooltipable {

	/**
	 * Sets a tooltip
	 *
	 * @param tooltip List of lines
	 */
	default void _setTooltip(List<OrderedText> list, ChatMessage... tooltip) {
		list.clear();
		list.addAll(MinecraftScreen.getTooltipList(tooltip));
	}

	default void _setTooltip(ChatMessage... tooltip) {
		this._setTooltip(this.getTooltipComponents(0, 0), tooltip);
	}

	@ApiStatus.Internal
	List<OrderedText> getTooltipComponents(int mouseX, int mouseY);

	boolean isMouseOverComponent(int mouseX, int mouseY);

}
