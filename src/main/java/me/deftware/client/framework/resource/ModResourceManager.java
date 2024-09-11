package me.deftware.client.framework.resource;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import me.deftware.client.framework.main.EMCMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderLoader;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

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
    private final ShaderLoader shaderLoader;

    private final String namespace;

    public ModResourceManager(EMCMod mod, String type) throws IOException {
        this.zipFile = getZipFile(mod);
        namespace = mod.getMeta().getName().toLowerCase();
        this.type = type;
        textureManager = new TextureManager(this);
        shaderLoader = new ShaderLoader(textureManager, ex -> {
            ex.printStackTrace();
        });
        RenderSystem.recordRenderCall(this::reload);
    }

    private void reload() {
        shaderLoader.reload(
                CompletableFuture::completedFuture,
                this,
                Util.getMainWorkerExecutor(),
                MinecraftClient.getInstance()
        );
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
    public Set<String> getAllNamespaces() {
        return Set.of(namespace);
    }

    @Override
    public List<Resource> getAllResources(Identifier id) {
        return Collections.emptyList();
    }

    @Override
    public Optional<Resource> getResource(Identifier id) {
        ZipEntry entry = getEntry(id.getPath());
        if (entry == null) {
            return MinecraftClient.getInstance().getResourceManager().getResource(id);
        }
        try {
            return Optional.of(new ModResource(transformer.apply(id.getPath(), getResourceStream(entry)), id));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        var map = new HashMap<Identifier, Resource>();
        var root = type + "/" + startingPath;
        var entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            var entry = entries.nextElement();
            var name = entry.getName();
            if (!name.startsWith(root)) {
                continue;
            }
            var identifier = Identifier.of(namespace, name.substring(type.length() + 1));
            if (!allowedPathPredicate.test(identifier)) {
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
        var manager = MinecraftClient.getInstance().getResourceManager();
        var result = manager.findResources(startingPath, allowedPathPredicate);
        map.putAll(result);
        return map;
    }

    @Override
    public Map<Identifier, List<Resource>> findAllResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        return null;
    }

    @Override
    public Stream<ResourcePack> streamResourcePacks() {
        return null;
    }

}
