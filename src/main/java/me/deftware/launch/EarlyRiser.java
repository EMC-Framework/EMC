package me.deftware.launch;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.spongepowered.asm.mixin.Mixins;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class EarlyRiser implements Runnable {

    public static final Map<String, String> MIXIN_CONFIGS = new HashMap<>();
    public static final String EMC_MIXIN = "mixins.emc.json";

    static {
        MIXIN_CONFIGS.put("optifabric", "mixins.optifine.json");
        MIXIN_CONFIGS.put("optifine", "mixins.optifine.json");
        MIXIN_CONFIGS.put("sodium", "mixins.sodium.json");
    }

    @Override
    public void run() {
        Mixins.addConfiguration(EMC_MIXIN);
        for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
           String name = modContainer.getMetadata().getName().toLowerCase();
           if (MIXIN_CONFIGS.containsKey(name)) {
               Mixins.addConfiguration(MIXIN_CONFIGS.get(name));
           }
        }
    }

}
