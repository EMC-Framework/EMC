package me.deftware.client.framework.gui;

import me.deftware.client.framework.helper.ScreenHelper;
import me.deftware.client.framework.util.ResourceUtils;
import me.deftware.client.framework.util.types.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.realms.RealmsBridge;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Deftware
 */
public enum MinecraftScreens {

	Multiplayer(GuiMultiplayer::new), WorldSelection(GuiWorldSelection::new),
	Options(parent -> new GuiOptions(parent, Minecraft.getInstance().gameSettings)), MainMenu(parent -> new GuiMainMenu()),
	Mods(parent -> {
		// Important: Change "modmenu" to what's relevant on this mc version
		if (ResourceUtils.hasSpecificMod("modmenu")) {
			return Objects.requireNonNull(ScreenHelper.createScreenInstance("io.github.prospector.modmenu.gui.ModListScreen", new Pair<>(net.minecraft.client.gui.GuiScreen.class, parent)));
		}
		return null;
	});

	private final Function<GuiScreen, net.minecraft.client.gui.GuiScreen> screenFunction;

	MinecraftScreens(Function<GuiScreen, net.minecraft.client.gui.GuiScreen> screenFunction) {
		this.screenFunction = screenFunction;
	}

	public void open(GuiScreen parent) {
		net.minecraft.client.Minecraft.getInstance().displayGuiScreen(screenFunction.apply(parent));
	}

	@SuppressWarnings("ConstantConditions")
	public static void openRealms() {
		new RealmsBridge().switchToRealms(Minecraft.getInstance().currentScreen);
	}

}
