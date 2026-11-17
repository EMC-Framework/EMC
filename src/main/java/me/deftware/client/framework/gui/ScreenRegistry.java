package me.deftware.client.framework.gui;

import lombok.Getter;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import com.mojang.realmsclient.RealmsMainScreen;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Deftware
 */
public enum ScreenRegistry {

	Multiplayer(JoinMultiplayerScreen.class),
	WorldSelection(SelectWorldScreen.class),
	CreateWorld(CreateWorldScreen.class),
	Options(OptionsScreen.class, args -> new OptionsScreen((Screen) args[0], net.minecraft.client.Minecraft.getInstance().options)),
	MainMenu(TitleScreen.class, parent -> new TitleScreen()),

	IngameMenu(PauseScreen.class),
	Disconnected(DisconnectedScreen.class, args -> new DisconnectedScreen(
			(Screen) args[0], (Component) args[1], (Component) args[2]
	)),
	Container(AbstractContainerScreen.class),
	Chat(ChatScreen.class),
	Death(DeathScreen.class),

	Realms(RealmsMainScreen.class);

	@Getter
	private final Class<? extends Screen> clazz;
	private final CatchableFunction supplier;

	ScreenRegistry(Class<? extends Screen> clazz) {
		this.clazz = clazz;
		this.supplier = (args) ->
			this.clazz.getDeclaredConstructor(
					Arrays.stream(args).map(o -> {
						if (o instanceof Screen)
							return Screen.class;
						return o.getClass();
					}).toArray(Class[]::new)
			).newInstance(
					args
			);
	}

	ScreenRegistry(Class<? extends Screen> clazz, CatchableFunction supplier) {
		this.clazz = clazz;
		this.supplier = supplier;
	}

	public MinecraftScreen create(Object... params) {
		try {
			Screen screen = this.supplier.apply(params);
			if (screen == null)
				throw new Exception("Null screen");
			return (MinecraftScreen) screen;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void open(Object... params) {
		MinecraftScreen screen = this.create(params);
		if (screen != null) {
			Minecraft.getMinecraftGame().openScreen(screen);
		}
	}

	public boolean isOpen() {
		Screen current = net.minecraft.client.Minecraft.getInstance().gui.screen();
		return current != null && this.clazz.isAssignableFrom(current.getClass());
	}

	public static Optional<ScreenRegistry> valueOf(Class<? extends Screen> screen) {
		return Arrays.stream(ScreenRegistry.values())
				.filter(m -> m.getClazz().isAssignableFrom(screen))
				.findFirst();
	}

	@FunctionalInterface
	private interface CatchableFunction {

		Screen apply(Object... args) throws Exception;

	}

}
