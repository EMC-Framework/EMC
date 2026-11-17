package me.deftware.weaver;

import me.deftware.client.framework.util.path.LocationUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.launch.knot.Knot;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;

/**
 * Lightweight bootstrap entrypoint that points {@code fabric.modsFolder} to the
 * version-specific EMC framework directory (matching legacy Weaver behavior and
 * isolating it from {@code .minecraft/mods}) and delegates launch to {@link Knot}.
 */
public class Main implements PreLaunchEntrypoint {
    public static void main(String[] args) {
        try {
            File emcFile = LocationUtil.getEMC().toFile();
            if (emcFile != null && emcFile.getParentFile() != null) {
                System.setProperty("fabric.modsFolder", emcFile.getParentFile().getAbsolutePath());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        Knot.launch(args, EnvType.CLIENT);
    }

    @Override
    public void onPreLaunch() {
        if (Boolean.getBoolean("emc.auditMixins")) {
            System.out.println("=== RUNNING FULL SPONGE MIXIN AUDIT ===");
            MixinEnvironment.getCurrentEnvironment().audit();
            System.out.println("=== SPONGE MIXIN AUDIT FINISHED ===");
            net.minecraft.SharedConstants.tryDetectVersion();
            net.minecraft.server.Bootstrap.bootStrap();
            net.minecraft.core.registries.BuiltInRegistries.DATA_COMPONENT_INITIALIZERS
                    .build(net.minecraft.data.registries.VanillaRegistries.createWorldLookup())
                    .forEach(net.minecraft.core.component.DataComponentInitializers.PendingComponents::apply);
            System.out.println("APPLE isFood=" + ((me.deftware.client.framework.item.Item) net.minecraft.world.item.Items.APPLE).isFood()
                    + ", name=" + ((me.deftware.client.framework.item.Item) net.minecraft.world.item.Items.APPLE).getName().getString());
            System.exit(0);
        }
    }
}
