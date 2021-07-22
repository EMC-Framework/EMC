package me.deftware.client.framework.resource;

import com.google.common.collect.Sets;
import me.deftware.client.framework.main.EMCMod;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Deftware
 */
public class ModResourceManager implements IResourceManager {

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
    public List<IResource> getAllResources(ResourceLocation id) throws IOException {
        return Collections.singletonList(getResource(id));
    }

    @Override
    public Collection<ResourceLocation> getAllResourceLocations(String pathIn, Predicate<String> filter) {
        return null;
    }

    @Override
    public Set<String> getResourceNamespaces() {
        return namespaces;
    }

    @Override
    public IResource getResource(ResourceLocation id) throws IOException {
        ZipEntry entry = getEntry(id.getPath());
        if (entry == null) {
            return Minecraft.getInstance().getResourceManager().getResource(id);
        }
        return new ModResource(transformer.apply(id.getPath(), zipFile.getInputStream(entry)), id);
    }

}
