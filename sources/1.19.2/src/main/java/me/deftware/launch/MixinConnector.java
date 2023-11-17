package me.deftware.launch;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

import java.util.Map;

public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfiguration(EarlyRiser.EMC_MIXIN);
        for (Map.Entry<String, String> entry : EarlyRiser.MIXIN_CONFIGS.entrySet()) {
            ModFileInfo modFileInfo = FMLLoader.getLoadingModList().getModFileById(entry.getKey());
            if (modFileInfo != null) {
                Mixins.addConfiguration(entry.getValue());
            }
        }
    }

}
