package me.deftware.client.framework.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Deftware
 */
public enum ScreenRegistry {

	Multiplayer(GuiMultiplayer.class),
	WorldSelection(GuiSelectWorld.class),
	Options(GuiOptions.class, args -> new GuiOptions((GuiScreen) args[0], Minecraft.getMinecraft().gameSettings)),
	CreateWorld(GuiCreateWorld.class),
	MainMenu(GuiMainMenu.class, parent -> new GuiMainMenu()),

	IngameMenu(GuiIngameMenu.class),
	Disconnected(GuiDisconnected.class),
	Container(GuiContainer.class),
	Chat(GuiChat.class),
	Death(GuiGameOver.class),

	Realms(null);

	private final Class<? extends GuiScreen> clazz;
	private final CatchableFunction supplier;

	ScreenRegistry(Class<? extends GuiScreen> clazz) {
		this.clazz = clazz;
		this.supplier = (args) ->
			this.clazz.getDeclaredConstructor(
					Arrays.stream(args).map(o -> {
						if (o instanceof GuiScreen)
							return GuiScreen.class;
						return o.getClass();
					}).toArray(Class[]::new)
			).newInstance(
					args
			);
	}

	ScreenRegistry(Class<? extends GuiScreen> clazz, CatchableFunction supplier) {
		this.clazz = clazz;
		this.supplier = supplier;
	}

	public void open(Object... params) {
		try {
			GuiScreen screen = this.supplier.apply(params);
			if (screen == null)
				throw new Exception("Null screen");
			Minecraft.getMinecraft().displayGuiScreen(screen);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isOpen() {
		GuiScreen current = Minecraft.getMinecraft().currentScreen;
		return current != null && this.clazz.isAssignableFrom(current.getClass());
	}

	public static Optional<ScreenRegistry> valueOf(Class<? extends GuiScreen> screen) {
		return Arrays.stream(ScreenRegistry.values())
				.filter(m -> m.getClazz() != null && m.getClazz().isAssignableFrom(screen))
				.findFirst();
	}

	public Class<? extends GuiScreen> getClazz() {
		return clazz;
	}

	@FunctionalInterface
	private interface CatchableFunction {

		GuiScreen apply(Object... args) throws Exception;

	}

}
