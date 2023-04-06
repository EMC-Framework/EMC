package me.deftware.launch;

import net.minecraftforge.fml.loading.FMLLoader;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfiguration("mixins.emc.json");

        if (FMLLoader.getLoadingModList().getModFileById("optifine") != null) {
            Mixins.addConfiguration("mixins.optifine.json");
        } else if (FMLLoader.getLoadingModList().getModFileById("sodium") != null) {
            Mixins.addConfiguration("mixins.sodium.json");
        }
    }

}
