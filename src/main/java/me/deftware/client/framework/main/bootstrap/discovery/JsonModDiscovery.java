package me.deftware.client.framework.main.bootstrap.discovery;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.FileChooser;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.path.OSUtils;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class JsonModDiscovery extends AbstractModDiscovery {

	private static String manualJsonLocation;
	private static boolean isFileDialogOpen = false;

	@Override
	public void discover() {
		File jsonFile = getEMCJsonFile();
		if (jsonFile == null || !jsonFile.exists()) {
			Bootstrap.logger.debug("Could not find Json file, is this a custom launcher?");
			return;
		}
		try {
			Bootstrap.logger.debug("Found Json file {}", jsonFile.getAbsolutePath());
			JsonObject json = new Gson().fromJson(FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8), JsonObject.class);
			if (!json.has("emcMods")) {
				return;
			}
			JsonArray mods = json.get("emcMods").getAsJsonArray();
			int i = 0;
			for (JsonElement e : mods) {
				JsonObject o = e.getAsJsonObject();
				String data = o.get("name").getAsString() + "," + o.get("maven").getAsString() + "," + o.get("url").getAsString();
				System.setProperty("emcMod" + i, data);
				i++;
			}
		} catch (Exception ignored) { }
	}

	public static File getEMCJsonFile() {
		String defaultJsonName = getRunningFolder() + getVersion() + ".json";
		File jsonFile = new File(Bootstrap.EMCSettings != null ? Bootstrap.EMCSettings.getString("EMC_JSON_LOCATION", defaultJsonName) : defaultJsonName);

		if (!jsonFile.exists()) {
			if (manualJsonLocation != null && new File(manualJsonLocation).exists()) {
				return new File(manualJsonLocation);
			} else {
				Bootstrap.logger.debug("Opening File Open Dialog, as JSON Cannot be found...");

				try {
					JFXPanel frame = new JFXPanel(); // Initialize JavaFX Environment
					isFileDialogOpen = true;
					Platform.runLater(() -> {
						FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Open EMC Json File (Required)");
						fileChooser.getExtensionFilters().addAll(
								new FileChooser.ExtensionFilter("Json", "*.json")
						);

						File resultFile = fileChooser.showOpenDialog(null);

						if (resultFile != null) {
							manualJsonLocation = resultFile.getAbsolutePath();
						} else {
							Bootstrap.logger.debug("JSON not found, things will break if other addons are using this!");
						}
						isFileDialogOpen = false;
					});
				} catch (Exception | Error ex) {
					Bootstrap.logger.debug("Error: EMC Json File Open Dialog failed to open, please Input your EMC Json Location manually in your EMC Config @ emcJsonLocation");
					ex.printStackTrace();
				}
			}
		} else {
			return jsonFile;
		}

		while (isFileDialogOpen) {
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (manualJsonLocation == null) {
			return null;
		}

		// Save as New Json Location if Settings Available
		if (Bootstrap.EMCSettings != null) {
			Bootstrap.EMCSettings.saveString("EMC_JSON_LOCATION", manualJsonLocation);
			Bootstrap.EMCSettings.saveConfig();
		}
		return new File(manualJsonLocation);
	}

	public static String getVersion() {
		return MinecraftClient.getInstance().getGameVersion();
	}

	public static String getRunningFolder() {
		if (new File(OSUtils.getMCDir() + "versions").exists()) {
			return OSUtils.getMCDir() + "versions" + File.separator + getVersion() + File.separator;
		} else {
			return MinecraftClient.getInstance().runDirectory.getAbsolutePath() + File.separator;
		}
	}

}
