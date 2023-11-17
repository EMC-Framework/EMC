package me.deftware.client.framework.main;

import net.minecraft.client.MinecraftClient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("emc")
public class ForgeMod {

    private final Logger logger = LoggerFactory.getLogger("EMC|Forge");

    public ForgeMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoad);
    }

    private void onLoad(FMLClientSetupEvent event) {
        logger.info("Configuring Forge for EMC");
        MinecraftClient.getInstance().getFramebuffer().enableStencil();
    }

}
