package me.deftware.client.framework.gui.screens;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.event.events.EventScreen;
import me.deftware.client.framework.gui.ScreenRegistry;
import me.deftware.client.framework.gui.widgets.GenericComponent;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Minecraft screen instance
 *
 * @since 17.0.0
 * @author Deftware
 */
public interface MinecraftScreen extends GenericScreen {

	/**
	 * @return The type of the current screen
	 */
	default ScreenRegistry getScreenType() {
		return ScreenRegistry.valueOf(
				((Screen) this).getClass()
		).orElse(null);
	}

	/**
	 * Closes the current screen
	 */
	default void close() {
		Minecraft.getInstance().gui.setScreen(null);
	}

	/**
	 * Closes a screen handler
	 */
	static void closeHandledScreen(int syncId) {
		LocalPlayer entity = Minecraft.getInstance().player;
		if (entity != null && entity.connection != null) {
			entity.connection.send(new ServerboundContainerClosePacket(syncId));
		}
	}

	/**
	 * @return The first component of a specific type
	 */
	default <T extends GenericComponent> T getFirstOfType(Class<T> clazz) {
		List<T> children = this.getChildren(clazz);
		if (!children.isEmpty())
			return children.get(0);
		return null;
	}

	/**
	 * @return A list of all components of a given class
	 */
	<T extends GenericComponent> List<T> getChildren(Class<T> clazz);

	/**
	 * Clears all children from the screen
	 */
	void _clearChildren();

	/**
	 * Adds a custom EMC component
	 *
	 * @param component EMC component
	 * @param index Array index
	 */
	void addScreenComponent(GenericComponent component, int index);

	/**
	 * Convenience method for addScreenComponent,
	 * with default index of {@link List#size()}
	 *
	 * @param component EMC component
	 */
	default void addScreenComponent(GenericComponent component) {
		this.addScreenComponent(component, -1);
	}

	/**
	 * @return The associated screen event handler
	 */
	EventScreen getEventScreen();

	/**
	 * Renders a tooltip onscreen
	 * @param tooltip Tooltip lines
	 */
	default void renderTooltip(GLX context, int x, int y, Message... tooltip) {
		this.renderTooltip(context, x, y, getTooltipList(tooltip));
	}

	@ApiStatus.Internal
	static List<FormattedCharSequence> getTooltipList(Message... tooltip) {
		return Arrays.stream(tooltip)
				.map(Component.class::cast)
				.map(Component::getVisualOrderText)
				.collect(Collectors.toList());
	}

	@ApiStatus.Internal
	default void renderTooltip(GLX context, int x, int y, List<FormattedCharSequence> tooltipComponents) {
		context.getContext().setTooltipForNextFrame(
				Minecraft.getInstance().font,
				tooltipComponents, DefaultTooltipPositioner.INSTANCE, x, y, true
		);
	}

}
