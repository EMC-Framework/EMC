package me.deftware.client.framework.resource;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import me.deftware.client.framework.main.EMCMod;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Deftware
 */
public class ModResourceManager implements ResourceManager {

    private final ZipFile zipFile;
    private final String type;

    private BiFunction<String, InputStream, InputStream> transformer = (path, stream) -> stream;

    @Getter
    private final TextureManager textureManager;

    @Getter
    private final ShaderManager shaderLoader;

    private final String namespace;

    public ModResourceManager(EMCMod mod, String type) throws IOException {
        this.zipFile = getZipFile(mod);
        namespace = mod.getMeta().getName().toLowerCase();
        this.type = type;
        textureManager = new TextureManager(this);
        shaderLoader = new ShaderManager(textureManager, ex -> {
            ex.printStackTrace();
        });
        //RenderSystem.recordRenderCall(this::reload);
    }

    private void reload() {
        /*
        shaderLoader.reload(
                CompletableFuture::completedFuture,
                this,
                Util.backgroundExecutor(),
                Minecraft.getInstance()
        );
        */
    }

    public void setTransformer(BiFunction<String, InputStream, InputStream> transformer) {
        this.transformer = transformer;
    }

    public ZipFile getZipFile(EMCMod mod) throws IOException {
        return new ZipFile(mod.physicalFile);
    }

    public ZipEntry getEntry(String name) {
        return zipFile.getEntry(type + "/" + name);
    }

    private InputStream getResourceStream(ZipEntry entry) throws Exception {
        return zipFile.getInputStream(entry);
    }

    /* Superclass methods */

    @Override
    public Set<String> getNamespaces() {
        return Set.of(namespace);
    }

    @Override
    public List<Resource> getResourceStack(Identifier id) {
        return Collections.emptyList();
    }

    @Override
    public Optional<Resource> getResource(Identifier id) {
        ZipEntry entry = getEntry(id.getPath());
        if (entry == null) {
            return Minecraft.getInstance().getResourceManager().getResource(id);
        }
        try {
            return Optional.of(new ModResource(transformer.apply(id.getPath(), getResourceStream(entry)), id));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public Map<Identifier, Resource> listResources(String startingPath, ResourceManager.Selector selector) {
        var map = new HashMap<Identifier, Resource>();
        var root = type + "/" + startingPath;
        var entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            var entry = entries.nextElement();
            var name = entry.getName();
            if (!name.startsWith(root)) {
                continue;
            }
            var identifier = Identifier.fromNamespaceAndPath(namespace, name.substring(type.length() + 1));
            if (!selector.isIncluded(identifier)) {
                continue;
            }
            try {
                var stream = getResourceStream(entry);
                var resource = new ModResource(stream, identifier);
                map.put(identifier, resource);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        // Also make Minecraft resources available
        var manager = Minecraft.getInstance().getResourceManager();
        var result = manager.listResources(startingPath, selector);
        map.putAll(result);
        return map;
    }

    @Override
    public Map<Identifier, List<Resource>> listResourceStacks(String startingPath, ResourceManager.Selector selector) {
        return null;
    }

    @Override
    public Stream<PackResources> listPacks() {
        return null;
    }

}
