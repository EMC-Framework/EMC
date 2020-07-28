package me.deftware.client.framework.main;

import com.google.gson.JsonObject;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.path.LocationUtil;
import me.deftware.client.framework.utils.Settings;

import java.io.File;
import java.net.URLClassLoader;

/**
 * This is a parent class for all of the mods loaded by EMC.
 * Your mod must extend this class
 */
public abstract class EMCMod {

	public URLClassLoader classLoader;
	private Settings settings;
	public JsonObject modInfo;
	public File physicalFile;

	public void init(JsonObject json) {
		modInfo = json;
		try {
			settings = new Settings(modInfo.get("name").getAsString());
			settings.setupShutdownHook();
		} catch (Exception ex) {
			Bootstrap.logger.error("Could not load config file for {}", modInfo.get("name").getAsString());
		}
		physicalFile = LocationUtil.getClassPhysicalLocation(this.getClass()).toFile();
		Bootstrap.logger.debug("Physical jar of {} is {}", modInfo.get("name").getAsString(), physicalFile.getAbsolutePath());
		initialize();
	}

	/**
	 * Called before any events are sent to your mod, do your initialization here
	 */
	public abstract void initialize();

	/**
	 * Unloads your mod from EMC
	 */
	public void disable() {
		Bootstrap.getMods().remove(modInfo.get("name").getAsString());
	}

	/**
	 * Returns your main EMC mod settings handler
	 *
	 * @return Settings
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Called when Minecraft is closed, use this method to save anything in your mod
	 */
	public void onUnload() { }

	/**
	 * By implementing this function you can call functions in other EMC mods
	 *
	 * @param method The method the caller wants to call
	 * @param caller The EMC mod that is calling your function
	 */
	public void callMethod(String method, String caller, Object object) { }

	/**
	 * Called after Minecraft has been initialized, use this method to display an alternate main menu screen
	 */
	public void postInit() { }

}
