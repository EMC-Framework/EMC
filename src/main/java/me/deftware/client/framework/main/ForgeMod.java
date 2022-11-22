package me.deftware.client.framework.main;

import net.minecraft.client.MinecraftClient;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("emc")
public class ForgeMod {

    private final Logger logger = LoggerFactory.getLogger("EMC|Forge");

    public ForgeMod() {
        logger.info("Configuring Forge for EMC");
        MinecraftClient.getInstance().getFramebuffer().enableStencil();
    }

}
