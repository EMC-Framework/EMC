package me.deftware.client.framework.resource;

import com.google.common.collect.Sets;
import me.deftware.client.framework.main.EMCMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Deftware
 */
public class ModResourceManager implements ResourceManager {

    private final Set<String> namespaces = Sets.newLinkedHashSet();
    private final ZipFile zipFile;
    private final String type;

    private BiFunction<String, InputStream, InputStream> transformer = (path, stream) -> stream;

    public ModResourceManager(EMCMod mod, String type) throws IOException {
        this.zipFile = getZipFile(mod);
        this.namespaces.add(mod.getMeta().getName().toLowerCase());
        this.type = type;
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

    @Override
    public Set<String> getAllNamespaces() {
        return namespaces;
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

    private InputStream getResourceStream(ZipEntry entry) throws Exception {
        return zipFile.getInputStream(entry);
    }

    @Override
    public Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        return null;
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
